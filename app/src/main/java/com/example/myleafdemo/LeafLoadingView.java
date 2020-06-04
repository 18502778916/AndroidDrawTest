package com.example.myleafdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LeafLoadingView extends View {
    private static final String TAG = "LeafLoadingView";
    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    //中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;

    //用于获取资源文件
    Resources mResources;
    //用于控制绘制进度条距离左/上/下的距离
    private static final int LEFT_MARGIN = 9;
    //用于控制绘制进度条距离右的距离
    private static final int RIGHT_MARGIN = 25;
    private int mLeftMargin, mRightMargin;

    // 当前进度
    private int mProgress;
    // 总进度
    private static final int TOTAL_PROGRESS = 100;
    // 当前所在的绘制的进度条的位置
    private int mCurrentProgressPosition;
    // arc的右上角的x坐标，也是矩形x坐标的起始点
    private int mArcRightLocation;

    //中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    // 弧形的半径
    private int mArcRadius;

    //叶子飘动一个周期要花费的时间
    private static final long LEAF_FLOAT_TIME = 3000;
    private long mLeafFloatTime;
    //叶子旋转一周要花费的时间
    private static final long LEAF_ROTATE_TIME = 2000;
    private long mLeafRotateTime;

    //叶子图片
    private Bitmap mLeafBitmap;
    private int mLeafWidth, mLeafHeight;

    //画笔
    private Paint mBitmapPaint, mWhitePaint, mOrangePaint;

    private RectF mWhiteRectF, mOrangeRectF, mArcRectF;

    //用于控制随机增加的时间不报团
    private int mAddTime;

    //用于产生叶子信息
    private LeafFactory mLeafFactory;
    //产生出的叶子信息
    private List<Leaf> mLeafInfos;

    private int mTotalWidth, mTotalHeight;
    // 所绘制的进度条宽度
    private int mProgressWidth;

    private Bitmap mOuterBitmap;
    private Rect mOuterSrcRect, mOuterDestRect;
    private int mOuterWidth, mOuterHeight;


    public LeafLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        mLeftMargin = UiUtils.dipToPx(context, LEFT_MARGIN);
        mRightMargin = UiUtils.dipToPx(context, RIGHT_MARGIN);

        mLeafFloatTime = LEAF_FLOAT_TIME;
        mLeafRotateTime = LEAF_ROTATE_TIME;

        initBitmap();
        initPaint();
        mLeafFactory = new LeafFactory();
        mLeafInfos = mLeafFactory.generateLeafs();

    }

    private void initBitmap() {
        //获取叶子资源文件
        mLeafBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.leaf)).getBitmap();
        //获取叶子宽高
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mOuterBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.leaf_kuang)).getBitmap();
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();

    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);//设置抗锯齿
        mBitmapPaint.setDither(true);//设置防抖动
        mBitmapPaint.setFilterBitmap(true);//设置图片在动画过程中过滤图片优化，加快显示速度

        //初始化白色画笔
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);
        //初始化橙色画笔
        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
//        drawLeafs(canvas);
        canvas.drawBitmap(mOuterBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint);
        postInvalidate();
    }

    //叶子起始状态
    private enum StartType {
        LITTLE, MIDDLE, BIG
    }

    //叶子对象，用于记录叶子的主要数据
    private class Leaf {
        //在绘制部分的位置
        float x, y;
        //控制叶子飘动的幅度
        StartType type;
        //叶子旋转角度
        int rotateAndle;
        //叶子旋转方向----0：顺时针，1：逆时针
        int rotateDirection;
        //起始时间
        long startTime;

    }

    //叶子信息
    private class LeafFactory {
        //最大叶子数
        private static final int MAX_LEAFS = 8;
        Random random = new Random();

        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);//随机一个叶子状态
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    type = StartType.MIDDLE;
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
            }
            leaf.type = type;
            leaf.rotateAndle = random.nextInt(360);//随机一个叶子角度
            leaf.rotateDirection = random.nextInt(2);//随机一个叶子旋转方向
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;//让开始时间有一定随机性，产生交错感
            mAddTime = random.nextInt((int) mLeafFloatTime * 2);
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }

        //根据最大叶子数产生叶子信息
        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        //根据传入的叶子数量产生叶子信息
        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new ArrayList<>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }

    // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis() + new Random().nextInt((int) mLeafFloatTime);
        }
        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (mProgressWidth - mProgressWidth * fraction);
        leaf.y = getLocationY(leaf);
    }

    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / mProgressWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 大振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
        }
        return (int) (a * (Math.sin(w * leaf.x))) + mArcRadius * 2 / 3;
    }

    private void drawProgress(Canvas canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }
        // mProgressWidth为进度条的宽度，根据当前进度算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;
        if (mCurrentProgressPosition < mArcRadius) {
            // 1.绘制白色ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);
            // 2.绘制白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            // 绘制叶子
            drawLeafs(canvas);

            // 1.绘制橙色ARC
            int angle = (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition) / (float) mArcRadius));
            //起始角度
            int startAngle = 180 - angle;
            //扫过角度
            int sweepAngle = 2 * angle;
            // 2.绘制橙色ARC
            canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mOrangePaint);
        } else {
            // 1.绘制白色 RECT
            mWhiteRectF.left = mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            // 绘制叶子
            drawLeafs(canvas);

            // 2.绘制橙色ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);
            // 3.绘制橙色RECT
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);

        }

    }

    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Leaf leaf = mLeafInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime); // 根据时间计算旋转角度
                canvas.save();//保存叶子状态
                // 通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargin + leaf.x;
                float transY = mLeftMargin + leaf.y;
                matrix.postTranslate(transX, transY);

                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime) / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);

                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAndle : -angle + leaf.rotateAndle;

                matrix.postRotate(rotate, transX + mLeafWidth / 2, transY + mLeafHeight / 2);//绕某个点旋转角度度，这里选择的原点是图片的中心点

                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();//恢复叶子状态，与save()合用
            } else {
                continue;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h; //当前view高度
        mTotalWidth = w;  //当前view宽度
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin;//计算进度条宽度
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2;//计算弧形半径

        mOuterSrcRect = new Rect(0, 0, mOuterWidth, mOuterHeight);//绘制进度框绘制矩形
        mOuterDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);//根据当前view绘制矩形

        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin, mTotalWidth
                - mRightMargin,
                mTotalHeight - mLeftMargin);

        mOrangeRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin,
                mCurrentProgressPosition
                , mTotalHeight - mLeftMargin);

        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius,
                mTotalHeight - mLeftMargin);
        mArcRightLocation = mLeftMargin + mArcRadius;
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
