package com.davidmedenjak.pagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class PagerIndicatorView extends View {

    private PagerIndicator indicator = new PagerIndicator();
    private PagerListener listener;

    private PagerCallback pagerCallback = new PagerCallback() {
        @Override
        public void setItemCount(int itemCount) {
            PagerIndicatorView.this.setItemCount(itemCount);
        }

        @Override
        public void setPageScrolled(int position, float positionOffset) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            invalidate();
        }
    };

    public PagerIndicatorView(Context context) {
        this(context, null);
    }

    public PagerIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.PagerIndicator_Line);

        if (isInEditMode()) {
            itemCount = indicator.getMaxDisplayedItems();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PagerIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (isInEditMode()) {
            itemCount = 3;
        }
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PagerIndicatorView, defStyleAttr, defStyleRes);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float dp2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);
        final int size = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_size, (int) dp2);
        indicator.setItemSize(size);

        float dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);
        final int padding = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_padding, (int) dp4);
        indicator.setItemPadding(padding);

        float dp12 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, dm);
        final int length = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_length, (int) dp12);
        indicator.setItemLength(length);

        final int edgeAnimationFlags = a.getInt(R.styleable.PagerIndicatorView_pi_edgeAnimation, PagerIndicator.FLAG_ALPHA);
        indicator.setEdgeAnimationFlags(edgeAnimationFlags);

        final int type = a.getInt(R.styleable.PagerIndicatorView_pi_type, PagerIndicator.TYPE_LINE);
        indicator.setType(type);

        final int colorBackground = a.getColor(R.styleable.PagerIndicatorView_android_colorBackground, Color.GRAY);
        indicator.setColorBackground(colorBackground);

        final int colorFocused = a.getColor(R.styleable.PagerIndicatorView_android_colorFocusedHighlight, Color.WHITE);
        indicator.setColorFocused(colorFocused);

        final int maxDisplayedItems = a.getInt(R.styleable.PagerIndicatorView_pi_maxVisibleItems, 10);
        indicator.setMaxDisplayedItems(maxDisplayedItems);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int width;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = Math.min(
                        indicator.getWidth(itemCount) + getPaddingLeft() + getPaddingRight(),
                        MeasureSpec.getSize(widthMeasureSpec)
                );
                break;
            default:
                throw new IllegalArgumentException();
        }
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int height;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = Math.min(
                        indicator.getHeight(itemCount) + getPaddingTop() + getPaddingBottom(),
                        MeasureSpec.getSize(heightMeasureSpec)
                );
                break;
            default:
                throw new IllegalArgumentException();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        indicator.setBounds(
                getWidth() - getPaddingLeft() - getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom()
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int save = canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        indicator.draw(canvas, itemCount, currentPosition, currentPositionOffset);
        canvas.restoreToCount(save);
    }

    private int itemCount = 0;
    private int currentPosition = 0;
    private float currentPositionOffset = 0F;

    public void setPager(@Nullable PagerListener pager) {
        if (listener != null) {
            listener.setPagerCallback(null);
        }
        this.listener = pager;
        if (listener != null) {
            listener.setPagerCallback(pagerCallback);
        }
    }

    private void setItemCount(int itemCount) {
        if (this.itemCount == itemCount) {
            return;
        }
        this.itemCount = itemCount;
        requestLayout();
    }

    public interface PagerCallback {
        void setItemCount(int itemCount);

        void setPageScrolled(int position, float positionOffset);
    }

    public interface PagerListener {
        void setPagerCallback(@Nullable PagerCallback callback);
    }

}
