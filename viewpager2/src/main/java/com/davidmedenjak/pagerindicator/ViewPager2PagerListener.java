package com.davidmedenjak.pagerindicator;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPager2PagerListener implements PagerIndicatorView.PagerListener {
    private ViewPager2 pager;
    private final PageChangeListener pageChangeListener;
    private PagerIndicatorView.PagerCallback callback;

    public ViewPager2PagerListener(@NonNull ViewPager2 pager) {
        this.pager = pager;
        pageChangeListener = new PageChangeListener();
    }

    @Override
    public void setPagerCallback(PagerIndicatorView.PagerCallback callback) {
        this.callback = callback;
        if (callback != null) {
            pager.registerOnPageChangeCallback(pageChangeListener);
            pageChangeListener.onPageScrolled(pager.getCurrentItem(), 0F, 0);
        } else {
            pager.unregisterOnPageChangeCallback(pageChangeListener);
        }
    }

    private class PageChangeListener extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //noinspection ConstantConditions
            callback.setItemCount(pager.getAdapter().getItemCount());
            callback.setPageScrolled(position, positionOffset);
        }
    }
}
