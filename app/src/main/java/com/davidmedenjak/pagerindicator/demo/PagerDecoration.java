package com.davidmedenjak.pagerindicator.demo;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmedenjak.pagerindicator.PagerIndicator;

import java.util.Locale;

@Deprecated
class PagerDecoration extends RecyclerView.ItemDecoration {

    private final Rect padding = new Rect();

    private PagerIndicator indicator = new PagerIndicator();

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public PagerDecoration() {
        padding.bottom = 20;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);


        int save = c.save();
        boolean isRtl = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
        if (isRtl) {
            c.translate(parent.getWidth(), 0F);
            c.scale(-1F, 1F);
        }
        indicator.setBounds(parent.getWidth() - padding.width(), parent.getHeight() - padding.height());
        c.translate(padding.left, padding.top);

        indicator.draw(c, parent.getAdapter().getItemCount(), activePosition, progress);
        c.restoreToCount(save);
    }
}
