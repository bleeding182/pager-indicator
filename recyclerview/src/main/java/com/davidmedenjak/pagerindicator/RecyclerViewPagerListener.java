package com.davidmedenjak.pagerindicator;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewPagerListener implements PagerIndicatorView.PagerListener {
    private RecyclerView recyclerView;
    private final PageChangeListener pageChangeListener;
    private PagerIndicatorView.PagerCallback callback;

    public RecyclerViewPagerListener(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        pageChangeListener = new PageChangeListener();
    }

    @Override
    public void setPagerCallback(@Nullable PagerIndicatorView.PagerCallback callback) {
        this.callback = callback;
        if (callback != null) {
            recyclerView.addItemDecoration(pageChangeListener);
        } else {
            recyclerView.removeItemDecoration(pageChangeListener);
        }
    }

    private class PageChangeListener extends RecyclerView.ItemDecoration {
        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            final RecyclerView.LayoutManager lm = parent.getLayoutManager();
            if (!(lm instanceof LinearLayoutManager)) {
                callback.setItemCount(0);
                return;
            }

            final LinearLayoutManager layoutManager = (LinearLayoutManager) lm;
            final int activePosition = layoutManager.findFirstVisibleItemPosition();
            if (activePosition == RecyclerView.NO_POSITION) {
                return;
            }

            // find offset of active page (if the user is scrolling)
            final View activeChild = layoutManager.findViewByPosition(activePosition);
            final int left = activeChild.getLeft();
            final int width = activeChild.getWidth();

            // interpolate offset for smooth animation
            float rawProgress = left * -1 / (float) width;

            callback.setItemCount(recyclerView.getAdapter().getItemCount());
            callback.setPageScrolled(activePosition, rawProgress);
        }
    }
}
