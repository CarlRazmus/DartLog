package com.fraz.dartlog.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.fraz.dartlog.R;

public class DartboardScoringView extends View {

    private final static int OUTLINE_STROKE_SIZE = 5;
    private final static int TOP_HALF_NUMBER_OFFSET = 10;
    private final static int BOTTOM_HALF_NUMBER_OFFSET = -TOP_HALF_NUMBER_OFFSET - 10;
    private final static int DEGREES_PER_NUMBER = 360 / 20;

    private static final int CENTER_TEXT_SIZE = 125;
    private static final int CENTER_TEXT_SIZE_SAFETY_MARGIN = 30;

    private final static int DEFAULT_TEXT_SIZE = 40;
    private final static int DEFAULT_COLOR = Color.WHITE;
    private final static float INITIAL_ANGLE = -90 - (DEGREES_PER_NUMBER / 2);
    private final static String[] NUMBER_ORDER = new String[]{"20", "1", "18", "4", "13", "6", "10",
                                                              "15", "2", "17", "3", "19", "7", "16",
                                                              "8", "11", "14", "9", "12", "5"};
    private final GestureDetector mGestureDetector;

    private Paint mOutlinePaint;
    private Paint mBoardNumberPaint;
    private Paint mCenterTextPaint;
    private RectF mBoardBounds;

    private Path[] mScoreTextPaths = new Path[20];
    private int mTextSize;
    private int mTextColor;
    private int mOutlineColor;
    private boolean mShowOutline;
    private String mScoreText;


    public DartboardScoringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
        mGestureDetector = new GestureDetector(context, new DartboardScoringGestureListener());
        mScoreText = "0";
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DartboardScoringView,
                0, 0);
        try {
            mTextSize = a.getInteger(R.styleable.DartboardScoringView_textSize, DEFAULT_TEXT_SIZE);
            mTextColor = a.getColor(R.styleable.DartboardScoringView_textColor, DEFAULT_COLOR);

            mOutlineColor = a.getColor(R.styleable.DartboardScoringView_outlineColor, DEFAULT_COLOR);
            mShowOutline = a.getBoolean(R.styleable.DartboardScoringView_showOutline, true);
        } finally {
            a.recycle();
        }
    }

    private void initPaint() {
        mBoardNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoardNumberPaint.setColor(mTextColor);
        mBoardNumberPaint.setTextSize(mTextSize);
        mBoardNumberPaint.setTextAlign(Paint.Align.CENTER);

        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setStyle(Paint.Style.FILL);
        mOutlinePaint.setColor(mOutlineColor);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(OUTLINE_STROKE_SIZE);

        mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setColor(DEFAULT_COLOR);
        mCenterTextPaint.setTextSize(CENTER_TEXT_SIZE);
        mCenterTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xPadding = (float)(getPaddingLeft() + getPaddingRight());
        float yPadding = (float)(getPaddingTop() + getPaddingBottom());

        float maxBoardWidth = (float)w - xPadding;
        float maxBoardHeight = (float)h - yPadding;

        float boardDiameter = Math.min(maxBoardWidth, maxBoardHeight) - OUTLINE_STROKE_SIZE;
        mBoardBounds = new RectF(getPaddingLeft() + OUTLINE_STROKE_SIZE,
                                 getPaddingTop() + OUTLINE_STROKE_SIZE,
                                 boardDiameter, boardDiameter);

        float startAngle;
        float sweepAngle;
        for (int i = 0; i < 20; ++i)
        {
            Path path = new Path();
            if (isOnTopHalfOfBoard(i)) {
                startAngle = INITIAL_ANGLE + i * DEGREES_PER_NUMBER;
                sweepAngle = DEGREES_PER_NUMBER;
            } else {
                startAngle = INITIAL_ANGLE + (i + 1) * DEGREES_PER_NUMBER;
                sweepAngle = -DEGREES_PER_NUMBER;
            }

            path.arcTo(mBoardBounds, startAngle, sweepAngle);
            mScoreTextPaths[i] = path;
        }
    }

    /**
     * Returns if the index points the a number at the top half of the board.
     * @param numberIdx Index of a number in the NUMBER_ORDER list.
     * @return True if the index points to a number at the top half of the board, otherwise False.
     */
    private boolean isOnTopHalfOfBoard(int numberIdx) {
        return numberIdx < 6 || numberIdx > 14;
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

        // Draw the outline
        if (mShowOutline) {
            canvas.drawOval(mBoardBounds, mOutlinePaint);
        }

        // Draw the board numbers
        for (int i = 0; i < mScoreTextPaths.length; i++) {
            if (isOnTopHalfOfBoard(i)) {
                canvas.drawTextOnPath(NUMBER_ORDER[i], mScoreTextPaths[i],
                        0, mTextSize + TOP_HALF_NUMBER_OFFSET, mBoardNumberPaint);
            } else {
                canvas.drawTextOnPath(NUMBER_ORDER[i], mScoreTextPaths[i],
                        0, BOTTOM_HALF_NUMBER_OFFSET, mBoardNumberPaint);
            }
        }

        // Draw the center score
        canvas.drawText(mScoreText, mBoardBounds.centerX(),
                        mBoardBounds.centerY() + CENTER_TEXT_SIZE/3, mCenterTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
                float xDistanceToCenter = event.getX() - mBoardBounds.centerX();
                float yDistanceToCenter = event.getY() - mBoardBounds.centerY();
                double distanceToCenter = Math.sqrt(Math.pow(xDistanceToCenter, 2) +
                                                    Math.pow(yDistanceToCenter, 2));
                String scoreText = mScoreText;
                if (distanceToCenter > CENTER_TEXT_SIZE + CENTER_TEXT_SIZE_SAFETY_MARGIN) {
                    int numberIdx = getNumberIdxAtCoordinate(xDistanceToCenter, yDistanceToCenter,
                            distanceToCenter);
                    scoreText = NUMBER_ORDER[numberIdx];
                }
                if (!scoreText.equals(mScoreText)) {
                    mScoreText = scoreText;
                    invalidate();
                }
                result = true;
            }
        }
        return result;
    }

    private int getNumberIdxAtCoordinate(double xDistanceToCenter, double yDistanceToCenter,
                                         double distanceToCenter) {
        double angle = Math.acos(xDistanceToCenter / distanceToCenter);
        int idxOffset = (int) Math.round(angle / Math.PI * 10);
        int numberIdx;
        if (yDistanceToCenter > 0) {
            numberIdx = 5 + idxOffset;
        } else {
            numberIdx = (5 - idxOffset) % 20;
            if (numberIdx < 0)
                numberIdx += 20;
        }
        return numberIdx;
    }

    class DartboardScoringGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    }
}