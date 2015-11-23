# CustomIndicatorWidget
自定义的Indicator控件，可以自动显示当前View的位置

在XML中：
<com.bupt.enniu.indicatorcanvasdemo.CustomIndicatorView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        CustomIndicatorView:indicator_count="4"
        CustomIndicatorView:indicator_radius="15px"
        CustomIndicatorView:indicator_background_color="@android:color/darker_gray"
        CustomIndicatorView:indicator_color="@android:color/holo_red_dark"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="100dp"
        android:id="@+id/customindicator"
        />

代码：
customIndicatorView = (CustomIndicatorView)findViewById(R.id.customindicator);

设置监听：
customIndicatorView.setOnIndicatorChangeListener(new CustomIndicatorView.OnIndicatorChangeListener() {
            @Override
            public void onChangde(int currentPosition) {
                Log.d("current postion",String.valueOf(currentPosition));
            }
        });
        

接收手指滑动：
Activity or ViewGroup
@Override
    public boolean onTouchEvent(MotionEvent event) {
        customIndicatorView.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
