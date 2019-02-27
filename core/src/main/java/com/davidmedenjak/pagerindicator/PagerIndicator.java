package com.davidmedenjak.pagerindicator;

import android.graphics.Canvas;
import android.graphics.Rect;

abstract class PagerIndicator {

    public static final int TYPE_LINE = 0;
    public static final int TYPE_CIRCLE = 1;

    public final static int FLAG_SIZE = 1;
    public final static int FLAG_ALPHA = 2;

    private final Rect bounds = new Rect();

    private int edgeAnimationFlags;

    private float indicatorLength;
    private float indicatorPadding;

    private float indicatorHeight;

    private int maxDisplayedItems = 3;

    protected int colorFocused;
    protected int colorBackground;

    public PagerIndicator() {
    }

    public int getWidth(int items) {
        final int actualItems = Math.min(maxDisplayedItems + 2, items);
        if (actualItems == 0) return 0;

        final float totalLength = actualItems * indicatorLength;
        final float totalPadding = (actualItems - 1) * indicatorPadding;
        return (int) (totalLength + totalPadding);
    }

    public int getHeight() {
        return (int) indicatorHeight;
    }

    public void setBounds(int width, int height) {
        bounds.set(0, 0, width, height);
    }

    public Rect getBounds() {
        return bounds;
    }

    public void draw(Canvas canvas, int items, int active, float progress) {
        final int nonAnimatedOffset = maxDisplayedItems / 2;

        int indexOffset = maxDisplayedItems % 2 == 0 ? 0 : 1;
        final boolean isAnimatingVisibleItems = maxDisplayedItems < items && active >= nonAnimatedOffset && active < items - nonAnimatedOffset - indexOffset;

        final float animationOffset;
        if (isAnimatingVisibleItems) {
            animationOffset = (indicatorLength + indicatorPadding) * -progress;
        } else {
            animationOffset = 0F;
        }

        final Rect bounds = getBounds();
        final int maxPossibleItems = (int) (1 + (bounds.width() - indicatorLength) / (indicatorLength + indicatorPadding));
        final int itemsToDraw = Math.min(maxPossibleItems, Math.min(maxDisplayedItems, items));

        final int itemMeasuredWidth = getWidth(itemsToDraw);
        final int itemMeasuredHeight = getHeight();
        final int offsetX = (bounds.width() - itemMeasuredWidth) / 2;
        final int offsetY = (bounds.height() - itemMeasuredHeight);

        final int saveBackground = canvas.save();
        canvas.translate(offsetX + animationOffset, offsetY);
        drawBackground(canvas, progress, isAnimatingVisibleItems, itemsToDraw);
        canvas.restoreToCount(saveBackground);

        final int saveActive = canvas.save();
        canvas.translate(offsetX + animationOffset, offsetY);
        int activeItem = getNormalizedActiveItem(items, active, nonAnimatedOffset);
        drawActiveIndicator(canvas, progress, activeItem);
        canvas.restoreToCount(saveActive);
    }

    private void drawBackground(Canvas canvas, float progress, boolean isAnimating, int itemsToDraw) {
        // animate the first item if scrolling
        drawIndicator(canvas, isAnimating ? 1 - progress : 1F);
        canvas.translate(indicatorLength + indicatorPadding, 0F);

        for (int i = 1; i < itemsToDraw; i++) {
            drawIndicator(canvas, 1F);
            canvas.translate(indicatorLength + indicatorPadding, 0F);
        }

        if (isAnimating && progress > 0F) {
            // animate the next item
            drawIndicator(canvas, progress);
        }
    }

    private int getNormalizedActiveItem(int items, int active, int nonAnimatedOffset) {
        final int normalizedActiveItem;
        if (items <= maxDisplayedItems || active < nonAnimatedOffset) {
            normalizedActiveItem = active;
        } else if (items - nonAnimatedOffset <= active) {
            normalizedActiveItem = maxDisplayedItems - (items - active);
        } else {
            normalizedActiveItem = nonAnimatedOffset;
        }
        return normalizedActiveItem;
    }

    private void drawActiveIndicator(Canvas canvas, float progress, int activeItem) {
        canvas.translate(activeItem * indicatorLength + activeItem * indicatorPadding, 0F);
        drawActiveIndicator(canvas, progress, 1F);
        if (progress != 0F) {
            canvas.translate(indicatorLength + indicatorPadding, 0F);
            drawActiveIndicator(canvas, -progress, progress);
        }
    }

    /**
     * Draw the indicator background.
     *
     * @param canvas     the canvas to draw to
     * @param visibility a value [0, 1] indicating an ongoing animation.
     *                   Use this to scale / fade indicators on the edges.
     */
    abstract protected void drawIndicator(Canvas canvas, float visibility);

    /**
     * Draw the active indicators. This will be called after {@link #drawIndicator(Canvas, float)}
     * to highlight the active item.
     *
     * @param canvas     the canvas to draw to
     * @param progress   the progress of the animation [-1, 1]. Negative values indicate that
     *                   the previous value is selected and selection is moving to this item.
     *                   {@code 0} indicates that the item is fully selected, positive values
     *                   that the selection is moving forward.
     * @param visibility a value [0, 1] indicating an ongoing animation.
     *                   Use this to scale / fade indicators on the edges.
     */
    abstract protected void drawActiveIndicator(Canvas canvas, float progress, float visibility);

    public void setIndicatorHeight(int size) {
        this.indicatorHeight = size;
    }

    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    public float getIndicatorLength() {
        return indicatorLength;
    }

    public void setIndicatorLength(float length) {
        this.indicatorLength = length;
    }

    public float getIndicatorPadding() {
        return indicatorPadding;
    }

    public void setIndicatorPadding(float padding) {
        this.indicatorPadding = padding;
    }

    public int getMaxDisplayedItems() {
        return maxDisplayedItems;
    }

    public void setMaxDisplayedItems(int max) {
        this.maxDisplayedItems = max;
    }

    public void setEdgeAnimationFlags(int flags) {
        edgeAnimationFlags = flags;
    }

    public int getEdgeAnimationFlags() {
        return edgeAnimationFlags;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public void setColorFocused(int colorFocused) {
        this.colorFocused = colorFocused;
    }
}
