package com.davidmedenjak.pagerindicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class PagerIndicator {

    public final static int FLAG_SIZE = 1;
    public final static int FLAG_ALPHA = 2;

    public static final int TYPE_LINE = 0;
    public static final int TYPE_CIRCLE = 1;

    private int edgeAnimationFlags;
    private int type;

    private float itemLength;
    private float itemSize;
    private float itemPadding;

    private Rect bounds = new Rect();
    private int maxDisplayedItems = 3;

    private Paint paint = new Paint();
    private int colorFocused;
    private int colorBackground;

    public PagerIndicator() {
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    public int getWidth(int items) {
        final int actualItems = Math.min(maxDisplayedItems, items);
        if (actualItems == 0) return 0;
        return (int) (actualItems * itemLength + (actualItems - 1) * itemPadding);
    }

    public int getHeight(int items) {
        return (int) itemSize;
    }

    public void setBounds(int width, int height) {
        bounds.set(0, 0, width, height);
    }

    public Rect getBounds() {
        return bounds;
    }

    public void draw(Canvas canvas, int items, int active, float progress) {
        final int nonAnimatedOffset = maxDisplayedItems / 2;

        final boolean isAnimating = maxDisplayedItems < items && active >= nonAnimatedOffset && active < items - nonAnimatedOffset - 1;
        final float animationOffset;
        if (isAnimating) {
            animationOffset = (itemLength + itemPadding) * -progress;
        } else {
            animationOffset = 0F;
        }

        final Rect bounds = getBounds();
        final int maxPossibleItems = (int) (1 + (bounds.width() - itemLength) / (itemLength + itemPadding));
        final int itemsToDraw = Math.min(maxPossibleItems, Math.min(maxDisplayedItems, items));

        final int itemMeasuredWidth = getWidth(itemsToDraw);
        final int itemMeasuredHeight = getHeight(itemsToDraw);
        final int offsetX = (bounds.width() - itemMeasuredWidth) / 2;
        final int offsetY = (bounds.height() - itemMeasuredHeight);

        final int saveBackground = canvas.save();
        canvas.translate(offsetX + animationOffset, offsetY);
        drawBackground(canvas, progress, isAnimating, itemsToDraw);
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
        canvas.translate(itemLength + itemPadding, 0F);

        for (int i = 1; i < itemsToDraw; i++) {
            drawIndicator(canvas, 1F);
            canvas.translate(itemLength + itemPadding, 0F);
        }

        if (isAnimating && progress > 0F) {
            // animate the next item
            drawIndicator(canvas, progress);
        }
    }

    private int getNormalizedActiveItem(int items, int active, int nonAnimatedOffset) {
        final int normalizedActiveItem;
        if (active < nonAnimatedOffset) {
            normalizedActiveItem = active;
        } else if (items - nonAnimatedOffset <= active) {
            normalizedActiveItem = maxDisplayedItems - (items - active);
        } else {
            normalizedActiveItem = nonAnimatedOffset;
        }
        return normalizedActiveItem;
    }

    private void drawActiveIndicator(Canvas canvas, float progress, int activeItem) {
        canvas.translate(activeItem * itemLength + activeItem * itemPadding, 0F);
        drawActiveIndicator(canvas, progress, 1F);
        if (progress != 0F) {
            canvas.translate(itemLength + itemPadding, 0F);
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
    protected void drawIndicator(Canvas canvas, float visibility) {
        paint.setColor(colorBackground);
        paint.setStrokeWidth(itemSize);
        if ((edgeAnimationFlags & FLAG_ALPHA) == FLAG_ALPHA) {
            paint.setAlpha((int) (visibility * Color.alpha(colorBackground)));
        }
        if (type == TYPE_LINE) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(itemSize);
            canvas.drawLine(
                    itemPadding / 2,
                    itemSize / 2,
                    itemLength - itemPadding / 2,
                    itemSize / 2,
                    paint
            );
        } else if (type == TYPE_CIRCLE) {
            paint.setStyle(Paint.Style.FILL);
            final float radius;
            if ((edgeAnimationFlags & FLAG_SIZE) == FLAG_SIZE) {
                radius = visibility * itemSize / 2F;
            } else {
                radius = itemSize / 2F;
            }
            canvas.drawCircle(
                    (itemPadding + itemLength) / 2F,
                    itemSize / 2F,
                    radius,
                    paint
            );
        }
    }

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
    protected void drawActiveIndicator(Canvas canvas, float progress, float visibility) {
        paint.setColor(colorFocused);
        if (type == TYPE_LINE) {
            paint.setStrokeWidth(itemSize);
            if (progress >= 0) {
                canvas.drawLine(
                        itemPadding / 2 + (itemLength - itemPadding) * progress,
                        itemSize / 2,
                        itemLength - itemPadding / 2,
                        itemSize / 2,
                        paint
                );
            } else if (progress < 0) {
                canvas.drawLine(
                        itemPadding / 2,
                        itemSize / 2,
                        itemLength - itemPadding / 2 - (itemLength - itemPadding) * (progress + 1),
                        itemSize / 2,
                        paint
                );
            }
        } else if (type == TYPE_CIRCLE) {
            paint.setStyle(Paint.Style.FILL);
            final float radius;
            if (progress < 0) {
                radius = (Math.abs(progress)) * itemSize / 2F;
            } else {
                radius = (1 - progress) * itemSize / 2F;
            }
            canvas.drawCircle(
                    (itemPadding + itemLength) / 2F,
                    itemSize / 2F,
                    radius,
                    paint
            );
        }
    }

    public void setItemSize(int size) {
        this.itemSize = size;
    }

    public float getItemSize() {
        return itemSize;
    }

    public float getItemLength() {
        return itemLength;
    }

    public void setItemLength(float length) {
        this.itemLength = length;
    }

    public float getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(float padding) {
        this.itemPadding = padding;
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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public void setColorFocused(int colorFocused) {
        this.colorFocused = colorFocused;
    }
}
