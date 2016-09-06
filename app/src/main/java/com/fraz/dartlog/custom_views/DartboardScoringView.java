package com.fraz.dartlog.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DartboardScoringView extends View {

    private final static int BOARD_STROKE_SIZE = 5;
    private final static int NUMBER_DIAMETER_DIFF = 40;
    private final static int DEGREES_PER_NUMBER = 360 / 20;
    private final static String[] NUMBER_ORDER = new String[]{"20", "1", "18", "4", "13", "6", "10",
                                                              "15", "2", "17", "3", "19", "7", "16",
                                                              "8", "11", "14", "9", "12", "5"};

    private Paint mBoardPaint;
    private Paint mBoardNumberPaint;
    private RectF mBoardBounds;
    private Path[] mScoreTextPaths = new Path[20];

    public DartboardScoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBoardNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoardNumberPaint.setColor(Color.WHITE);
        mBoardNumberPaint.setTextSize(40);
        mBoardNumberPaint.setTextAlign(Paint.Align.CENTER);

        mBoardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoardPaint.setStyle(Paint.Style.FILL);
        mBoardPaint.setColor(Color.WHITE);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setStrokeWidth(BOARD_STROKE_SIZE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xPadding = (float)(getPaddingLeft() + getPaddingRight());
        float yPadding = (float)(getPaddingTop() + getPaddingBottom());

        float maxBoardWidth = (float)w - xPadding;
        float maxBoardHeight = (float)h - yPadding;

        float boardDiameter = Math.min(maxBoardWidth, maxBoardHeight) - BOARD_STROKE_SIZE;
        mBoardBounds = new RectF(getPaddingLeft() + BOARD_STROKE_SIZE,
                                 getPaddingTop() + BOARD_STROKE_SIZE,
                                 boardDiameter, boardDiameter);

        for (int i = 0; i < 20; ++i)
        {
            Path path = new Path();
            float startingAngle = -90 - (DEGREES_PER_NUMBER / 2);
            path.arcTo(mBoardBounds, startingAngle + i * DEGREES_PER_NUMBER, DEGREES_PER_NUMBER);
            mScoreTextPaths[i] = path;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minWidth, widthMeasureSpec, 1);

        int minHeight = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the board
        canvas.drawOval(
                mBoardBounds,
                mBoardPaint
        );

        // Draw the numbers
        for (int i = 0; i < mScoreTextPaths.length; i++) {
            canvas.drawTextOnPath(NUMBER_ORDER[i], mScoreTextPaths[i],
                    0, NUMBER_DIAMETER_DIFF, mBoardNumberPaint);
        }
    }
}