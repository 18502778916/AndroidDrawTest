package com.example.myleafdemo;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class MyAnimationUtil {

    /**
     * isClockWise:设置动画结束角度
     * duration：动画播放时长
     * isFillAfter：动画结束后，动画是否保持最后的结束时状态
     * repeatCount：动画重复计数
     */
    public static RotateAnimation initMyAnimation(boolean isClockWise, long duration, boolean isFillAfter, int repeatCount) {
        int endAngle;//结束角度。即动画结束后图片的旋转角度
        if (isClockWise) {
            endAngle = 360;
        } else {
            endAngle = -360;
        }
        //pivotXValue为旋转中心在控件自身水平位置百分比，如果X和Y的Value都设置为0.5，则该控件以自身中心旋转。
        /*
         * float fromDegrees  旋转起始点
         * float toDegrees    旋转结束点
         * int pivotXType     X旋转类型
         * float pivotXValue  旋转中心在控件上X轴百分百(0.5则为控件X轴中心点)
         * int pivotYType     Y旋转类型
         * float pivotYValue  旋转中心在控件上Y轴百分百(0.5则为控件Y轴中心点)
         */
        RotateAnimation rotateAnimation = new RotateAnimation(0, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator linearInterpolator = new LinearInterpolator();//插值器（LinearInterpolator为线性均匀改变）
        rotateAnimation.setInterpolator(linearInterpolator);//设置插值器
        rotateAnimation.setDuration(duration);//设置动画播放时长，即动画用时多少播放完
        rotateAnimation.setFillAfter(isFillAfter);//设置动画结束后，动画是否保持最后的结束时状态
        rotateAnimation.setRepeatCount(repeatCount);//设置动画重复计数
        rotateAnimation.setRepeatMode(Animation.RESTART);//设置动画重复方式，结合setRepeatCount实现动画无限播放
        return rotateAnimation;

    }
}
