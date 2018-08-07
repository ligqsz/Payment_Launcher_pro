package com.pax.paylauncher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.pax.paylauncher.R;
import com.pax.paylauncher.utils.DensityUtils;

/**
 * @author ligq
 * @date 2018/8/7
 */

public class RedPointImageView extends AppCompatImageView {

    private int pointColor;
    private String pointText;
    private float pointRadius;
    private int width;
    private Paint mPaint;
    private Rect bound;

    public RedPointImageView(Context context) {
        this(context, null);
    }

    public RedPointImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPointImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RedPointImageView);
        pointColor = ta.getColor(R.styleable.RedPointImageView_point_color, Color.WHITE);
        pointText = ta.getString(R.styleable.RedPointImageView_point_content);
        pointRadius = ta.getDimension(R.styleable.RedPointImageView_point_size, DensityUtils.dp2px(context, 15));
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        bound = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(pointText)) {
            float pointX = (width - pointRadius);
            float pointY = pointRadius;
            mPaint.setColor(pointColor);
            canvas.drawCircle(pointX, pointY, pointRadius, mPaint);
            mPaint.getTextBounds(pointText, 0, pointText.length(), bound);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(pointRadius * 0.8f);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(pointText, pointX, pointY + bound.height() / 2.0f, mPaint);
        }
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public void setPointText(String pointText) {
        this.pointText = pointText;
    }
}
