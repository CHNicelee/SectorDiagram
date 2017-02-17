package com.ice.timecollector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by asd on 2/17/2017.
 */

public class ArcView extends View {

    private int mHeight, mWidth;//宽高
    private Paint mPaint;//扇形的画笔
    private Paint mTextPaint;//画文字的画笔

    private int centerX, centerY;//中心坐标

    //"其他"的value
    //扇形图分成太多快 所以要合并一部分为其他 即图中灰色部分
    private double rest;

    private int maxNum;//扇形图的最大块数 超过的item就合并到其他

    String others = "其他";//“其他”块要显示的文字
    double total;//数据的总和
    double[] datas;//数据集
    String[] texts;//每个数据对应的文字集

    //颜色 默认的颜色
    private int[] mColors = {
            Color.parseColor("#FF4081"), Color.parseColor("#ffc0cb"),
            Color.parseColor("#00ff00"), Color.parseColor("#0066ff"), Color.parseColor("#ffee00")
    };

    private int mTextSize;//文字大小

    private int radius = 1000;//半径

    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    //初始化
    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(40);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);

        mTextSize = 30;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高 不要设置wrap_content
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //无数据
        if (datas == null || datas.length == 0) return;

        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;
        int min = mHeight > mWidth ? mWidth : mHeight;
        if (radius > min / 2) {
            radius = (int) ((min - getPaddingTop() - getPaddingBottom()) / 3.5);
        }

        //画扇形
        canvas.save();
        drawCircle(canvas);
        canvas.restore();


        //线与文字
        canvas.save();
        drawLineAndText(canvas);
        canvas.restore();

    }


    //画线与文字
    private void drawLineAndText(Canvas canvas) {
        int start = 0;
        canvas.translate(centerX, centerY);//平移画布到中心
        mPaint.setStrokeWidth(4);
        for (int i = 0; i < (maxNum < datas.length ? maxNum : datas.length); i++) {

            float angles = (float) ((datas[i] * 1.0f / total) * 360);
            drawLine(canvas, start, angles, texts[i], mColors[i % mColors.length]);
            start += angles;
        }
        //画其他
        if (start < 359)
            drawLine(canvas, start, 360 - start, others, Color.GRAY);

    }

    private void drawLine(Canvas canvas, int start, float angles, String text, int color) {
        mPaint.setColor(color);
        float stopX, stopY;
        stopX = (float) ((radius + 40) * Math.cos((2 * start + angles) / 2 * Math.PI / 180));
        stopY = (float) ((radius + 40) * Math.sin((2 * start + angles) / 2 * Math.PI / 180));

        canvas.drawLine((float) ((radius - 20) * Math.cos((2 * start + angles) / 2 * Math.PI / 180)),
                (float) ((radius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180)),
                stopX, stopY, mPaint
        );
        //画横线
        int dx;//判断横线是画在左边还是右边
        int endX;
        if (stopX > 0) {
            endX = (centerX - getPaddingRight() - 20);
        } else {
            endX = (-centerX + getPaddingLeft() + 20);
        }
        //画横线
        canvas.drawLine(stopX, stopY,
                endX, stopY, mPaint
        );
        dx = (int) (endX - stopX);

        //测量文字大小
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        int w = rect.width();
        int h = rect.height();
        int offset = 20;//文字在横线的偏移量
        //画文字
        canvas.drawText(text, 0, text.length(), dx > 0 ? stopX + offset : stopX - w - offset, stopY + h, mTextPaint);

        //测量百分比大小
        String percentage = angles / 3.60 + "";
        percentage = percentage.substring(0, percentage.length() > 4 ? 4 : percentage.length()) + "%";
        mTextPaint.getTextBounds(percentage, 0, percentage.length(), rect);
        w = rect.width() - 10;
        //画百分比
        canvas.drawText(percentage, 0, percentage.length(), dx > 0 ? stopX + offset : stopX - w - offset, stopY - 5, mTextPaint);

    }

    //画扇形
    private void drawCircle(Canvas canvas) {
        RectF rect = new RectF((float) (centerX - radius), centerY - radius,
                centerX + radius, centerY + radius);

        int start = 0;
        for (int i = 0; i < (maxNum < datas.length ? maxNum : datas.length); i++) {
            float angles = (float) ((datas[i] * 1.0f / total) * 360);
            mPaint.setColor(mColors[i % mColors.length]);
            canvas.drawArc(rect, start, angles, true, mPaint);
            start += angles;
        }
        //画"其他"
        rest = 0;
        for (int i = maxNum; i < datas.length; i++) {
            rest += datas[i];
        }
        float angles = (float) 360 - start;
        mPaint.setColor(Color.GRAY);
        canvas.drawArc(rect, start, angles, true, mPaint);

    }



    //setter
    public void setColors(int[] mColors) {
        this.mColors = mColors;
        invalidate();
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        setTextSize(radius / 6);
        invalidate();
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        invalidate();
    }

    public void setOthersText(String others) {
        this.others = others;
    }

    public abstract class ArcViewAdapter<T> {

        public void setData(List<T> list) {
            datas = new double[list.size()];
            texts = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                total += getValue(list.get(i));
                datas[i] = getValue(list.get(i));
                texts[i] = getText(list.get(i));
            }

        }

        //通过传来的数据集的某个元素  得到具体的数字
        public abstract double getValue(T t);

        //通过传来的数据集的某个元素  得到具体的描述
        public abstract String getText(T t);
    }

}

