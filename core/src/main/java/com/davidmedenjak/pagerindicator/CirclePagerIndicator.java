package com.davidmedenjak.pagerindicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CirclePagerIndicator extends PagerIndicator {

    private Paint paint = new Paint();

    public CirclePagerIndicator() {
        paint.setAntiAlias(true);
    }

    @Override
    protected void drawIndicator(Canvas canvas, float visibility) {
        paint.setColor(colorBackground);
        paint.setStrokeWidth(getIndicatorHeight());
        if ((getEdgeAnimationFlags() & FLAG_ALPHA) == FLAG_ALPHA) {
            paint.setAlpha((int) (visibility * Color.alpha(colorBackground)));
        }
        paint.setStyle(Paint.Style.FILL);
        final float radius;
        if ((getEdgeAnimationFlags() & FLAG_SIZE) == FLAG_SIZE) {
            radius = visibility * getIndicatorHeight() / 2F;
        } else {
            radius = getIndicatorHeight() / 2F;
        }
        canvas.drawCircle(
                (getIndicatorPadding() + getIndicatorLength()) / 2F,
                getIndicatorHeight() / 2F,
                radius,
                paint
        );
    }

    @Override
    protected void drawActiveIndicator(Canvas canvas, float progress, float visibility) {
        paint.setColor(colorFocused);
        paint.setStyle(Paint.Style.FILL);
        final float radius;
        if (progress < 0) {
            radius = (Math.abs(progress)) * getIndicatorHeight() / 2F;
        } else {
            radius = (1 - progress) * getIndicatorHeight() / 2F;
        }
        canvas.drawCircle(
                (getIndicatorPadding() + getIndicatorLength()) / 2F,
                getIndicatorHeight() / 2F,
                radius,
                paint
        );
    }
}
