package com.example.myleafdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyEasyProgressView extends View {

    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;

    private static final int LEFT_MARGIN=9;
    private static final int RIGTH_MARGIN=9;
    private static final int ALL_MARGIN=9;

    private static final int MAX_PROGRESS_POSITION=100;

    //用于获取资源文件
    Resources mResources;
    private Bitmap mBitmap;

    private int mBitmapHeight,mBitmapWidth;

    private Paint mBitmapPaint,mWhitePaint,mOrangePaint;
    private RectF mWhiteRectF,mOrangeRectF;

    private int mTotalHeight,mTotalWidth;

    private int mLeftMargin,mRightMargin;

    private int mProgressWidth;

    private int mCurrentProgressPosition;
    // 当前进度
    private int mProgress;

    private int mBitmapLeft,mBitmapTop;

    private Bitmap mOuterBitmap;
    private Rect mOuterSrcRect, mOuterDestRect;


    public MyEasyProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        mLeftMargin=UiUtils.dipToPx(context,LEFT_MARGIN);
        mRightMargin=UiUtils.dipToPx(context,RIGTH_MARGIN);

        initBitMap();
        initPaint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawBitmap(canvas);
//        canvas.drawBitmap(mBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint);
        postInvalidate();
    }

    private void initPaint(){
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);

        mWhitePaint=new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);

        mOrangePaint=new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);

    }

    private void initBitMap(){
        mBitmap=((BitmapDrawable) mResources.getDrawable(R.mipmap.cs2)).getBitmap();
        mBitmapWidth=mBitmap.getWidth();
        mBitmapHeight=mBitmap.getHeight();
    }

    private void drawProgress(Canvas canvas){
        if (mProgress>MAX_PROGRESS_POSITION){
            mProgress=0;
        }
        mCurrentProgressPosition=mProgressWidth*mProgress/MAX_PROGRESS_POSITION;
        mWhiteRectF.left=mCurrentProgressPosition;
        mWhiteRectF.top=(mTotalHeight-3)/2;
        mWhiteRectF.bottom=mWhiteRectF.top+3;
        canvas.drawRect(mWhiteRectF,mWhitePaint);

        mOrangeRectF.left=0;
        mOrangeRectF.top=(mTotalHeight-3)/2;
        mOrangeRectF.bottom=mWhiteRectF.top+3;
        mOrangeRectF.right=mCurrentProgressPosition;
        canvas.drawRect(mOrangeRectF,mOrangePaint);
    }

    private void drawBitmap(Canvas canvas){
        mBitmapLeft=mCurrentProgressPosition-(mBitmapWidth/2);
        mBitmapTop=mLeftMargin+mBitmapHeight;
        canvas.drawBitmap(mBitmap,mBitmapLeft,mBitmapTop,mBitmapPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth=w;
        mTotalHeight=h;
//        mOuterSrcRect = new Rect(0, 0, mTotalWidth, mTotalHeight+100);
//        mOuterDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight+100);
        mProgressWidth=mTotalWidth-mLeftMargin-mRightMargin;
        mWhiteRectF=new RectF(mLeftMargin+mCurrentProgressPosition,mLeftMargin,mTotalWidth-mRightMargin,mTotalHeight-mRightMargin);
        mOrangeRectF=new RectF(mLeftMargin,mLeftMargin,mCurrentProgressPosition,mTotalHeight-mRightMargin);
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }
}
