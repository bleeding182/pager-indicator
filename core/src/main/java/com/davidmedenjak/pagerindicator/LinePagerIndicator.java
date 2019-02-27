package com.davidmedenjak.pagerindicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LinePagerIndicator extends PagerIndicator {

    private Paint paint = new Paint();

    public LinePagerIndicator() {
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    protected void drawIndicator(Canvas canvas, float visibility) {
        paint.setColor(colorBackground);
        paint.setStrokeWidth(getItemSize());
        if ((getEdgeAnimationFlags() & FLAG_ALPHA) == FLAG_ALPHA) {
            paint.setAlpha((int) (visibility * Color.alpha(colorBackground)));
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getItemSize());
        canvas.drawLine(
                getItemPadding() / 2,
                getItemSize() / 2,
                getItemLength() - getItemPadding() / 2,
                getItemSize() / 2,
                paint
        );
    }

    @Override
    protected void drawActiveIndicator(Canvas canvas, float progress, float visibility) {
        paint.setColor(colorFocused);
        paint.setStrokeWidth(getItemSize());
        if (progress >= 0) {
            canvas.drawLine(
                    getItemPadding() / 2 + (getItemLength() - getItemPadding()) * progress,
                    getItemSize() / 2,
                    getItemLength() - getItemPadding() / 2,
                    getItemSize() / 2,
                    paint
            );
        } else if (progress < 0) {
            canvas.drawLine(
                    getItemPadding() / 2,
                    getItemSize() / 2,
                    getItemLength() - getItemPadding() / 2 - (getItemLength() - getItemPadding()) * (progress + 1),
                    getItemSize() / 2,
                    paint
            );
        }
    }
}
