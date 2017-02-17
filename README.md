# SectorDiagram
This is a View to draw sector diagram
## Appearance
![](https://github.com/CHNicelee/SectorDiagram/blob/master/pic_1.jpg)
![](https://github.com/CHNicelee/SectorDiagram/blob/master/pic_2.jpg)

## Intrduction
You can add some data ,and then the ArcView will draw the sector diagram for you!

## Usage
At xml file use the ArcView:
```
    <com.ice.timecollector.view.ArcView
        android:background="#e6f1b0"
        android:id="@+id/arc"
        android:layout_width="match_parent"
        android:layout_height="300dp" />
```
At Activity:
```
        ArcView arcView = (ArcView) findViewById(R.id.arc);
        List<Times> times = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            Times t = new Times();
            t.hour = i;
            t.text = "Number"+i;
            times.add(t);
        }

        //must set adapter!
        ArcView.ArcViewAdapter myAdapter = arcView.new ArcViewAdapter<Times>(){
            @Override
            public double getValue(Times times) {
                return times.hour;
            }

            @Override
            public String getText(Times times) {
                return times.text;
            }
        };
        myAdapter.setData(times);//must set adapter's data
        arcView.setMaxNum(5);//the max piece of sector  optional
        arcView.setOthersText("Others");//the text of others  optional
        arcView.setRadius(150);//set radius  optional
		arcView.setColors(new int[]{getResources().getColor(R.color.green),getResources().getColor(R.color.colorAccent)});//set colors  optional
```