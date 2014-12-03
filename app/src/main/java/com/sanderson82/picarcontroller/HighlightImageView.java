package com.sanderson82.picarcontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * A customized image view that shows a highlighted circle
 */
public class HighlightImageView extends ImageView {

    /**
     * The x location to center the highlight circle
     */
    private float x = 0;

    /**
     * The y location to center the highlight circle
     */
    private float y = 0;

    /**
     * Flag to indicate if highlight circle should be drawn
     */
    private boolean drawCircle = false;

    // TODO:  Change these to use attributes that can be set within the XML
    /**
     * The circle highlight color
     */
    private int highlightColor = Color.BLUE;

    /**
     * The size of the highlight circle
     */
    private int circleSize = 100;

    /**
     * The alpha of the circle
     */
    private int alpha = 60;

    public HighlightImageView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public HighlightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HighlightImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawCircle) {
            Paint paint = new Paint();
            paint.setColor(highlightColor);
            paint.setAlpha(alpha);
            canvas.drawCircle(x, y, circleSize, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                drawCircle = true;
                invalidate();
                break;
            default:
                drawCircle = false;
                invalidate();
                break;
        }
        return true;
    }
}
