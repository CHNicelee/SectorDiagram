
package com.ice.timecollector;

import android.os.Bundle;

import com.ice.timecollector.baseActivity.BaseActivity;
import com.ice.timecollector.view.ArcView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ArcView arcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testArcView();
    }

    private void testArcView() {
        arcView = f(R.id.arc);

        List<Times> times = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            Times t = new Times();
            t.hour = i;
            t.text = "Number"+i;
            times.add(t);
        }

        ArcView.ArcViewAdapter myAdapter = arcView.new ArcViewAdapter<Times>(){
            @Override
            public double getValue(Times times) {
                return times.hour;
            }

            @Override
            public String getText(Times times) {
                return times.text;
            }
        };//设置adapter
        myAdapter.setData(times);//设置数据集
        arcView.setMaxNum(5);//设置可以显示的最大数值 该数值之后的会合并为其他
      //  arcView.setRadius(150);//设置圆盘半径
    }

    class Times {
        double hour;
        String text;
    }
}
