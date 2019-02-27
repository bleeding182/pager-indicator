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
        paint.setStrokeWidth(getIndicatorHeight());
        if ((getEdgeAnimationFlags() & FLAG_ALPHA) == FLAG_ALPHA) {
            paint.setAlpha((int) (visibility * Color.alpha(colorBackground)));
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getIndicatorHeight());
        canvas.drawLine(
                getIndicatorPadding() / 2,
                getIndicatorHeight() / 2,
                getIndicatorLength() - getIndicatorPadding() / 2,
                getIndicatorHeight() / 2,
                paint
        );
    }

    @Override
    protected void drawActiveIndicator(Canvas canvas, float progress, float visibility) {
        paint.setColor(colorFocused);
        paint.setStrokeWidth(getIndicatorHeight());
        if (progress >= 0) {
            canvas.drawLine(
                    getIndicatorPadding() / 2 + (getIndicatorLength() - getIndicatorPadding()) * progress,
                    getIndicatorHeight() / 2,
                    getIndicatorLength() - getIndicatorPadding() / 2,
                    getIndicatorHeight() / 2,
                    paint
            );
        } else if (progress < 0) {
            canvas.drawLine(
                    getIndicatorPadding() / 2,
                    getIndicatorHeight() / 2,
                    getIndicatorLength() - getIndicatorPadding() / 2 - (getIndicatorLength() - getIndicatorPadding()) * (progress + 1),
                    getIndicatorHeight() / 2,
                    paint
            );
        }
    }
}
