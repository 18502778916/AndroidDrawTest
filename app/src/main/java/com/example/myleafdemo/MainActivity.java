package com.example.myleafdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_fs;

    private Handler mHandler;

    private LeafLoadingView mLeafLoadingView;
    private MyEasyProgressView mEasyProgressView;
    private RadarView radarView;

    private static final int REFRESH_PROGRESS = 0x10;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leaf);
//        initView();
//        initEasyProgress();
        initRadarView();
//        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);
    }

    private void initRadarView(){
        radarView=findViewById(R.id.my_radar_view);
    }

    private void initEasyProgress(){
        mEasyProgressView=findViewById(R.id.my_easy_progress_view);
        mHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_PROGRESS:
                        if (mProgress < 40) {
                            mProgress += 1;
                            mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, new Random().nextInt(800));
                            mEasyProgressView.setProgress(mProgress);
                        } else {
                            mProgress += 1;
                            mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, new Random().nextInt(1200));
                            mEasyProgressView.setProgress(mProgress);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initView() {
        iv_fs = findViewById(R.id.iv_fs);
        mLeafLoadingView = findViewById(R.id.my_leaf_loading_view);
        RotateAnimation rotateAnimation = MyAnimationUtil.initMyAnimation(false, 1500, true, Animation.INFINITE);
        iv_fs.startAnimation(rotateAnimation);
        mHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_PROGRESS:
                        if (mProgress < 40) {
                            mProgress += 1;
                            mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, new Random().nextInt(800));
                            mLeafLoadingView.setProgress(mProgress);
                        } else {
                            mProgress += 1;
                            mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, new Random().nextInt(1200));
                            mLeafLoadingView.setProgress(mProgress);
                        }
                        break;
                    default:
                        break;
                }
            }
        };

    }


}