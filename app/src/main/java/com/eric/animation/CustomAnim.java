package com.eric.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

/**
 * Created by aiyang on 2018/5/28.
 */
public class CustomAnim extends Animation {
    private int mCenterWidth;
    private int mCenterHeight;
    private Camera mCamera = new Camera();
    private float mRotateY = 0.0f;
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(3000);
        setFillAfter(true);
        setRepeatCount(3);
        setInterpolator(new BounceInterpolator());
        mCenterWidth = width / 2;
        mCenterHeight = height / 2;
    }
    //暴露接口，设置旋转角度
    public void setRotateY(float rotateY){
        mRotateY=rotateY;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix=t.getMatrix();
        mCamera.save();
        //使用Camera设置旋转的角度
        mCamera.rotateY(mRotateY*interpolatedTime);
        //将旋转变换作用到Matrix上
        mCamera.getMatrix(matrix);
        mCamera.restore();
        //通过pre方法设置矩阵作用前的偏移量来改变旋转中心
        matrix.preTranslate(mCenterWidth,mCenterHeight);
        matrix.postTranslate(-mCenterWidth,-mCenterHeight);
    }
}