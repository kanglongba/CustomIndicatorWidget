package com.bupt.enniu.indicatorcanvasdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    CustomIndicatorView customIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customIndicatorView = (CustomIndicatorView)findViewById(R.id.customindicator);
        //设置监听函数
        customIndicatorView.setOnIndicatorChangeListener(new CustomIndicatorView.OnIndicatorChangeListener() {
            @Override
            public void onChangde(int currentPosition) {
                Log.d("current postion",String.valueOf(currentPosition));
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        customIndicatorView.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
