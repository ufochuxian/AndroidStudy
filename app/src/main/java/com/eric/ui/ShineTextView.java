package com.eric.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.eric.androidstudy.R;

import java.util.ArrayList;
 

/**
 * Created by xzx on 18-4-4.
 */
 
public class ShineTextView extends AppCompatTextView {
    private ArrayList<ShineTextView.Shadow> outerShadows;
 
    public ShineTextView(Context context) {
        super(context);
        init(null);
    }
 
    public ShineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
 
    public ShineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
 
    public void init(AttributeSet attrs) {
        outerShadows = new ArrayList<ShineTextView.Shadow>();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ShineTextView);
            if (a.hasValue(R.styleable.ShineTextView_bgShadowColor)) {
                float bgRadius = a.getFloat(R.styleable.ShineTextView_bgShadowRadius, 0);
                float bgDx = a.getFloat(R.styleable.ShineTextView_bgShadowDx, 0);
                float bgDy = a.getFloat(R.styleable.ShineTextView_bgShadowDy, 0);
                int bgColor = a.getColor(R.styleable.ShineTextView_bgShadowColor, 0xff000000);
                this.addOuterShadow(bgRadius, bgDx, bgDy, bgColor);
            }
            if (a.hasValue(R.styleable.ShineTextView_outerShadowColor)) {
                float radius = a.getFloat(R.styleable.ShineTextView_outerShadowRadius, 0);
                float dx = a.getFloat(R.styleable.ShineTextView_outerShadowDx, 0);
                float dy = a.getFloat(R.styleable.ShineTextView_outerShadowDy, 0);
                int color = a.getColor(R.styleable.ShineTextView_outerShadowColor, 0xff2b88f6);
                this.addOuterShadow(radius, dx, dy, color);
            }
            a.recycle();
        }
    }
 
    public void updateShadow() {
 
    }
 
    public void addOuterShadow(float r, float dx, float dy, int color) {
        outerShadows.add(new ShineTextView.Shadow(r, dx, dy, color));
    }
 
    public void clearOuterShadows() {
        outerShadows.clear();
    }
 
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setCompoundDrawables(null, null, null, null);
        for (ShineTextView.Shadow shadow : outerShadows) {
            this.setShadowLayer(shadow.r, shadow.dx, shadow.dy, shadow.color);
            super.onDraw(canvas);
        }
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
 
        } else if (widthMode == MeasureSpec.AT_MOST) {
 
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
 
        }
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
 
    public static class Shadow {
        float r;
        float dx;
        float dy;
        int color;
 
        public Shadow(float r, float dx, float dy, int color) {
            this.r = r;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
    }
}