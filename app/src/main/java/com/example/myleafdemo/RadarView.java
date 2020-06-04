package com.example.myleafdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RadarView extends View {

    private int count = 6;                //数据个数
    private float angle = (float) (Math.PI*2/count);
    private float radius;                   //网格最大半径
    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private String[] titles = {"a","b","c","d","e","f"};
    private double[] data = {100,60,60,60,100,50,10,20}; //各维度分值
    private float maxValue = 100;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        radius=UiUtils.dipToPx(context,100);
        initPaint();
    }

    private void initPaint(){
        mainPaint=new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(getResources().getColor(R.color.colorAccent));

        valuePaint=new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(getResources().getColor(R.color.colorPrimary));

        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX=w/2;
        centerY=h/2;
        postInvalidate();

    }

    private void drawPolygon(Canvas canvas){
        Path path = new Path();
        float r = radius/(count-1);//r是蜘蛛丝之间的间距

        for (int i = 0; i < count; i++) {
            float curR=r*i;
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j==0){
                    path.moveTo(centerX+curR,centerY);
                }else {
                    float x= (float) (centerX+curR*Math.cos(angle*j));
                    float y= (float) (centerY+curR*Math.sin(angle*j));
                    path.lineTo(x,y);
                }
            }
        }
        path.close();
        canvas.drawPath(path,mainPaint);
    }
}
