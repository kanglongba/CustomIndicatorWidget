package com.bupt.enniu.indicatorcanvasdemo;

import android.content.Context;
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
    int count =5;  //指示点的个数,默认是5

    public CustomIndicatorView(Context context){
        super(context);
        paintGray = new Paint();
        paintGray.setColor(Color.GRAY);
        paintGray.setStrokeWidth(5);
        paintGray.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(5);
        paintRed.setAntiAlias(true);

        this.mContext =  context;
    }

    public CustomIndicatorView(Context context, int count){
        this(context);
        this.count = count;
    }

    public CustomIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int finalWidth = 0,finalHeight = 0;

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.EXACTLY){
            finalWidth = widthSpecSize;
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            finalWidth = mContext.getResources().getDisplayMetrics().widthPixels/5; //屏幕宽度的1/5
        }

        if(heightSpecMode == MeasureSpec.EXACTLY){
            finalHeight = heightSpecSize;
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            finalHeight = 14;
        }

        setMeasuredDimension(finalWidth,finalHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        int height = canvas.getHeight();
        startX = width*2/5;  //从屏幕的2/5处到3/5处为绘制指示点的区域
        int endX = width*3/5;
        spaceX = width/(5*count);  //指示点的间距
        int startY = height/5;  //指示点的纵坐标
        for(int i=0;i<count;i++){
            canvas.drawCircle(startX+i*spaceX,startY,7,paintGray); //绘制背景指示点
        }

        canvas.drawCircle(startX+changeX+index*spaceX,startY,7, paintRed);  //绘制当前指示点

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downStartX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                changeX =(int) (event.getX()-downStartX)*spaceX/(width); //按比例计算指示点应该移动的距离,默认手指移动一屏,指示点移动一格
                break;
            case MotionEvent.ACTION_UP:
                //当手指移动了超过半屏的距离,就默认过度到下一个/上一个位置
                if((event.getX()-downStartX) > width/2){
                    index = (index+1)%count;
                }else if((event.getX()-downStartX) < -width/2){
                    index = (index-1)%count;
                    if(index ==-1){
                        index = count-1;
                    }
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
    }
}
