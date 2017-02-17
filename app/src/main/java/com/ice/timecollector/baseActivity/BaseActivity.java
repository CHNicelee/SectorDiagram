package com.ice.timecollector.baseActivity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by asd on 2/17/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public  <T extends View> T f(int id){
        return (T) findViewById(id);
    }

}
