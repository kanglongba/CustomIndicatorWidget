package com.bupt.enniu.indicatorcanvasdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by enniu on 15/11/23.
 */
public class CustomIndicatorView extends View {

    Paint paintGray;
    Paint paintRed;
    Context mContext;

    int width;  //画布的宽度
    int startX; //指示点得初始位置
    int spaceX;  //指示点之间的距离
    int changeX = 0;  //根据手指滑动的距离而计算出的指示点该移动的距离
    int downStartX;  //手指按下时的坐标
    int index=0;  //当前指示点的位置,从0开始
    int Indicator_Count =5;  //指示点的个数,默认是5

    int Indicator_color; //指示点的颜色
    int Indicator_background_color;  //背景指示点的颜色
    int Indicator_radius;  //指示点的半径

    OnIndicatorChangeListener listener;

    int DisplayWidth; //屏幕的宽度

    public CustomIndicatorView(Context context){
        super(context);
        init();
    }

    public CustomIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext =  context;
        TypedArray typedArray = mContext.getTheme()
                .obtainStyledAttributes(attrs,
                        R.styleable.CustomIndicatorView,
                        0,
                        0);

        try{
            Indicator_Count = typedArray.getInt(R.styleable.CustomIndicatorView_indicator_count,5);
            Indicator_radius = (int) typedArray.getDimension(R.styleable.CustomIndicatorView_indicator_radius,10.0f);
            Indicator_color = typedArray.getColor(R.styleable.CustomIndicatorView_indicator_color,Color.RED);
            Indicator_background_color = typedArray.getColor(R.styleable.CustomIndicatorView_indicator_background_color,Color.GRAY);

        }finally {
            typedArray.recycle();
        }

        init();

    }

    void init(){
        paintGray = new Paint();
        paintGray.setColor(Indicator_background_color);
        paintGray.setStrokeWidth(5);
        paintGray.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(Indicator_color);
        paintRed.setStrokeWidth(5);
        paintRed.setAntiAlias(true);

        DisplayWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth,finalHeight;

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.EXACTLY){
            finalWidth = widthSpecSize;
        }else {
            finalWidth = mContext.getResources().getDisplayMetrics().widthPixels/5; //屏幕宽度的1/5
        }

        if(heightSpecMode == MeasureSpec.EXACTLY){
            finalHeight = heightSpecSize;

            //如果控件的高度小于指示点的直径,那么就把高度设置为指示点的直径
            if(finalHeight < Indicator_radius*2){
                finalHeight = Indicator_radius*2;
            }
        }else {
            finalHeight = Indicator_radius*2;  //指示点的直径
        }

        setMeasuredDimension(finalWidth, finalHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        int height = getMeasuredHeight();
        startX = Indicator_radius;  //从指示点的半径处开始
        int endX = width-Indicator_radius; //结束位置
        spaceX = (width-2*Indicator_radius)/(Indicator_Count-1);  //指示点的间距
        int startY = height/2;  //指示点的纵坐标,从控件的中间位置开始绘制
        for(int i=0;i< Indicator_Count;i++){
            canvas.drawCircle(startX+i*spaceX,startY,Indicator_radius,paintGray); //绘制背景指示点
        }

        canvas.drawCircle(startX + changeX + index * spaceX, startY, Indicator_radius, paintRed);  //绘制当前指示点

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downStartX = (int) event.getRawX(); //getX(),相对于空间的坐标;getRawX(),相对与屏幕的坐标
                break;
            case MotionEvent.ACTION_MOVE:
                changeX =(int) (event.getRawX()-downStartX)*spaceX/(DisplayWidth); //按比例计算指示点应该移动的距离,默认手指移动一屏,指示点移动一格
                break;
            case MotionEvent.ACTION_UP:
                //当手指移动了超过半屏的距离,就默认过度到下一个/上一个位置
                if((event.getX()-downStartX) > DisplayWidth/2){
                    index = (index+1)% Indicator_Count;

                    //添加回调
                    addIndicatorChangeListner(index);
                }else if((event.getX()-downStartX) < -DisplayWidth/2){
                    index = (index-1)% Indicator_Count;
                    if(index ==-1){
                        index = Indicator_Count -1;
                    }

                    //添加回调
                    addIndicatorChangeListner(index);
                }

                changeX = 0; //重置
                break;
            default:
                break;

        }

        invalidate();

        return true;
    }

    //获取到当前的指示位置
    public int getIndex(){
        return this.index;
    }

    //设置当前的指示位置
    public void setIndex(int index){
        this.index = index;
        invalidate();

        //添加回调
        addIndicatorChangeListner(index);
    }

    //设置指示点变化时的监听
    public void setOnIndicatorChangeListener(OnIndicatorChangeListener listener){
        this.listener = listener;
    }

    //回调
    public void addIndicatorChangeListner(int currectPosition) {
        if (listener != null){
            listener.onChangde(currectPosition);
        }
    }

    public interface OnIndicatorChangeListener{
        public void onChangde(int currectPosition);
    }
}
