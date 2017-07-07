package com.example.rhong.musictest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.rhong.musictest.R;

/**
 * Created by rhong on 2017/7/6.
 */

public class CircleSeekBar extends View {

    private final boolean DEBUG = true;
    private final String TAG = "CircleSeekBar";

    private Context mContext = null;
    private AttributeSet mAttrs = null;

    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;

    private int mSeekBarMax = 0;
    private Paint mSeekBarBackgroundPaint = null;
    private Paint mSeekbarProgressPaint = null;
    private RectF mArcRectF = null;

    private boolean mIsShowProgressText = false;
    private Paint mProgressTextPaint = null;
    private int mProgressTextSize = 0;

    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private int mSeekBarSize = 0;
    private int mSeekBarRadius = 0;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private float mThumbLeft = 0;
    private float mThumbTop = 0;

    private float mSeekBarDegree = 0;
    private int mCurrentProgress = 0;

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    public CircleSeekBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {

//        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs,
//                R.styleable.CircleSeekBar);
//        mThumbDrawable = localTypedArray
//                .getDrawable(R.styleable.CircleSeekBar_android_thumb);
//        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
//        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
//        float progressWidth = localTypedArray.getDimension(
//                R.styleable.CircleSeekBar_progress_width, 5);
//        int progressBackgroundColor = localTypedArray.getColor(
//                R.styleable.CircleSeekBar_progress_background, Color.GRAY);
//        int progressFrontColor = localTypedArray.getColor(
//                R.styleable.CircleSeekBar_progress_front, Color.BLUE);
//        mSeekBarMax = localTypedArray.getInteger(
//                R.styleable.CircleSeekBar_progress_max, 100);
        //        mIsShowProgressText = localTypedArray.getBoolean(
//                R.styleable.CircleSeekBar_show_progress_text, false);
//        int progressTextStroke = (int) localTypedArray.getDimension(
//                R.styleable.CircleSeekBar_progress_text_stroke_width, 5);
//        int progressTextColor = localTypedArray.getColor(
//                R.styleable.CircleSeekBar_progress_text_color, Color.GREEN);
//        mProgressTextSize = (int) localTypedArray.getDimension(
//                R.styleable.CircleSeekBar_progress_text_size, 50);
        //localTypedArray.recycle();
        //
        mThumbDrawable = mContext.getResources().getDrawable(R.drawable.thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        mThumbNormal = new int[]{-android.R.attr.state_focused,
                -android.R.attr.state_pressed, -android.R.attr.state_selected,
                -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused,
                android.R.attr.state_pressed, android.R.attr.state_selected,
                android.R.attr.state_checked};

        mSeekBarMax = 1000;
        float progressWidth = 10.0f;
        int progressBackgroundColor = Color.GRAY;
        int progressFrontColor = Color.BLUE;

        mSeekbarProgressPaint = new Paint();
        mSeekBarBackgroundPaint = new Paint();

        mSeekbarProgressPaint.setColor(progressFrontColor);
        mSeekBarBackgroundPaint.setColor(progressBackgroundColor);

        mSeekbarProgressPaint.setAntiAlias(true);
        mSeekBarBackgroundPaint.setAntiAlias(true);

        mSeekbarProgressPaint.setStyle(Paint.Style.STROKE);
        mSeekBarBackgroundPaint.setStyle(Paint.Style.STROKE);

        mSeekbarProgressPaint.setStrokeWidth(progressWidth);
        mSeekBarBackgroundPaint.setStrokeWidth(progressWidth);

        mArcRectF = new RectF();

        mProgressTextPaint = new Paint();
        mProgressTextPaint.setColor(Color.BLUE);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setStrokeWidth(5);
        mProgressTextPaint.setTextSize(50);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // if(DEBUG) Log.d(TAG, "onMeasure");//TODO
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        Log.i("onMeasure:", mViewWidth + " " + mViewHeight);

        mSeekBarSize = mViewWidth > mViewHeight ? mViewHeight : mViewWidth;

        mSeekBarCenterX = mViewWidth / 2;
        mSeekBarCenterY = mViewHeight / 2;

        mSeekBarRadius = mSeekBarSize / 2;

        int left = mSeekBarCenterX - mSeekBarRadius;
        int right = mSeekBarCenterX + mSeekBarRadius;
        int top = mSeekBarCenterY - mSeekBarRadius;
        int bottom = mSeekBarCenterY + mSeekBarRadius;
        mArcRectF.set(left, top, right, bottom);

        // 起始位置设置TODO
        double s = Math.toRadians(mSeekBarDegree);
        // Log.i("todo", "取得的值  s ="+s);
        setThumbPosition(Math.toRadians(mSeekBarDegree));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius,
                mSeekBarBackgroundPaint);
        canvas.drawArc(this.mArcRectF, 0.0F, mSeekBarDegree, false,
                mSeekbarProgressPaint);
        drawThumbBitmap(canvas);
        //drawProgressText(canvas);

        super.onDraw(canvas);
    }

    private void drawThumbBitmap(Canvas canvas) {
        this.mThumbDrawable.setBounds((int) mThumbLeft, (int) mThumbTop,
                (int) (mThumbLeft + mThumbWidth),
                (int) (mThumbTop + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        if (true == mIsShowProgressText) {
            float textWidth = mProgressTextPaint.measureText(""
                    + mCurrentProgress);
            canvas.drawText("" + mCurrentProgress, mSeekBarCenterX - textWidth
                            / 2, mSeekBarCenterY + mProgressTextSize / 2,
                    mProgressTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.i("todo","执行onTouchEvent");
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seekTo(eventX, eventY, false);
                break;

            case MotionEvent.ACTION_MOVE:
                seekTo(eventX, eventY, false);
                break;

            case MotionEvent.ACTION_UP:
                seekTo(eventX, eventY, true);
                break;
        }
        return true;
    }

    public void seekTo(float eventX, float eventY, boolean isUp) {
        // Log.i("todo","执行seekTo");//TODO
        if (true == isPointOnThumb(eventX, eventY) && false == isUp) {
            mThumbDrawable.setState(mThumbPressed);
            double radian = Math.atan2(eventY - mSeekBarCenterY, eventX
                    - mSeekBarCenterX);
            /*
			 * 鐢变簬atan2杩斿洖鐨勫�间负[-pi,pi] 鍥犳闇�瑕佸皢寮у害鍊艰浆鎹竴涓嬶紝浣垮緱鍖洪棿涓篬0,2*pi]
			 */
            if (radian < 0) {
                radian = radian + 2 * Math.PI;
            }
            // if(DEBUG) Log.e(TAG, "seekTo radian = " + radian);
            setThumbPosition(radian);

            mSeekBarDegree = (float) Math.round(Math.toDegrees(radian));
            mCurrentProgress = (int) (mSeekBarMax * mSeekBarDegree / 360);
            invalidate();
        } else {
            mThumbDrawable.setState(mThumbNormal);
            invalidate();
        }
    }

    private boolean isPointOnThumb(float eventX, float eventY) {
        boolean result = false;
        double distance = Math.sqrt(Math.pow(eventX - mSeekBarCenterX, 2)
                + Math.pow(eventY - mSeekBarCenterY, 2));
        if (distance < mSeekBarSize
                && distance > (mSeekBarSize / 2 - mThumbWidth)) {
            result = true;
        }
        return result;
    }

    private void setThumbPosition(double radian) {
        // if(DEBUG)
        // Log.d("todo", "setThumbPosition radian = " + radian);
        double x = mSeekBarCenterX + mSeekBarRadius * Math.cos(radian);
        double y = mSeekBarCenterY + mSeekBarRadius * Math.sin(radian);
        mThumbLeft = (float) (x - mThumbWidth / 2);
        mThumbTop = (float) (y - mThumbHeight / 2);
    }

    public int getProgress() {
        return mCurrentProgress;
    }

    /*
     * set
     */
    public void setProgress(int progress) {
        // Log.e("todo", "setProgress-->progress="+progress);
        // if(DEBUG) Log.v(TAG, "setProgress progress = " + progress);
        if (progress > mSeekBarMax) {
            progress = mSeekBarMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        mCurrentProgress = progress;
        mSeekBarDegree = (progress * 360 / mSeekBarMax);
        // if(DEBUG) Log.d(TAG, "setProgress mSeekBarDegree = " +
        // mSeekBarDegree);
        setThumbPosition(Math.toRadians(mSeekBarDegree));

        invalidate();//TODO
    }

    public int getProgressMax() {
        return mSeekBarMax;
    }

    public void setProgressMax(int max) {
        // if(DEBUG) Log.v(TAG, "setProgressMax max = " + max);
        mSeekBarMax = max;
    }

    public void setProgressThumb(int thumbId) {
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }

    public void setProgressWidth(int width) {
        // if(DEBUG) Log.v(TAG, "setProgressWidth width = " + width);
        mSeekbarProgressPaint.setStrokeWidth(width);
        mSeekBarBackgroundPaint.setStrokeWidth(width);
    }

    public void setProgressBackgroundColor(int color) {
        mSeekBarBackgroundPaint.setColor(color);
    }

    public void setProgressFrontColor(int color) {
        mSeekbarProgressPaint.setColor(color);
    }

    public void setProgressTextColor(int color) {
        mProgressTextPaint.setColor(color);
    }

    public void setProgressTextSize(int size) {
        // if(DEBUG) Log.v(TAG, "setProgressTextSize size = " + size);
        mProgressTextPaint.setTextSize(size);
    }

    public void setProgressTextStrokeWidth(int width) {
        // if(DEBUG) Log.v(TAG, "setProgressTextStrokeWidth width = " + width);
        mProgressTextPaint.setStrokeWidth(width);
    }

    public void setIsShowProgressText(boolean isShow) {
        mIsShowProgressText = isShow;
    }

}
