package com.example.sensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OrientationView extends View {
    private Paint paint;
    private float x, y;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;

    public OrientationView(Context context) {
        super(context);
    }

    public OrientationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        x = 0f;
        y = 0f;
    }

    public OrientationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        float x0 = contentWidth / 2f;
        float y0 = contentHeight / 2f;

        paint.setColor(getResources().getColor(R.color.black));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x0 + x, y0 + y, (float) 0.03 * contentWidth, paint);

    }
}