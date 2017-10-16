package com.koidev.paint.view.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.koidev.paint.data.ParsableHelper;

import java.util.ArrayList;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintView extends View {

    private ArrayList<MotionEvent> cachedEvents = new ArrayList<>();
    private ArrayList<MotionEvent> eventList = new ArrayList<MotionEvent>();

    // To hold the path that will be drawn.
    private Path drawPath;
    // Paint object to draw drawPath and drawCanvas.
    private Paint drawPaint, canvasPaint;
    // initial color
    private int paintColor = 0xff000000;
    // canvas on which drawing takes place.
    private Canvas drawCanvas;
    // canvas bitmap
    private Bitmap canvasBitmap;
    // Brush stroke width
    private float brushSize;
    private float mX, mY;
    private boolean mDrawPoint;
    private static final float TOUCH_TOLERANCE = 4;

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(true);
        setUpDrawing();
    }

    private void setUpDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        // Making drawing smooth.
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = 14;
        drawPaint.setStrokeWidth(brushSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

        for (MotionEvent event : cachedEvents) {
            performTouchEvent(event);
        }
        cachedEvents.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                performTouchEvent(event);
        }
        return true;
    }

    private void touch_start(float x, float y) {
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
        mDrawPoint = true;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mDrawPoint = false;
            drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        if (mDrawPoint) {
            drawCanvas.drawPoint(mX, mY, drawPaint);
        } else {
            drawPath.lineTo(mX, mY);
            // commit the path to our offscreen
            drawCanvas.drawPath(drawPath, drawPaint);
            // kill this so we don't double draw
            drawPath.reset();
        }
    }

    private void performTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                break;
        }
        invalidate();
        eventList.add(MotionEvent.obtain(event));
    }

    public void clearCanvas() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        eventList.clear();
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return new ParsableHelper(eventList, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        ParsableHelper helper = (ParsableHelper) state;
        cachedEvents = helper.events;
        super.onRestoreInstanceState(helper.parcelable);
    }

    public boolean isEventListEmpty() {
        return eventList == null || eventList.isEmpty();
    }

    public Bitmap getCanvasBitmap(){
        return canvasBitmap;
    }
}