package com.davidmedenjak.pagerindicator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerPagerListener implements PagerIndicatorView.PagerListener {
    private ViewPager pager;
    private final PageChangeListener pageChangeListener;
    private final AdapterChangeListener adapterChangeListener;

    private PagerIndicatorView.PagerCallback callback;

    public ViewPagerPagerListener(@NonNull ViewPager pager) {
        this.pager = pager;

        pageChangeListener = new PageChangeListener();
        adapterChangeListener = new AdapterChangeListener();
    }

    @Override
    public void setPagerCallback(@Nullable PagerIndicatorView.PagerCallback callback) {
        this.callback = callback;
        if (callback != null) {
            pager.addOnPageChangeListener(pageChangeListener);
            pager.addOnAdapterChangeListener(adapterChangeListener);
            pageChangeListener.onPageScrolled(pager.getCurrentItem(), 0F, 0);
            adapterChangeListener.onAdapterChanged(pager, null, pager.getAdapter());
        } else {
            pager.removeOnPageChangeListener(pageChangeListener);
            pager.removeOnAdapterChangeListener(adapterChangeListener);
        }
    }

    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (callback == null) return;
            if (newAdapter != null) {
                callback.setItemCount(newAdapter.getCount());
            } else {
                callback.setItemCount(0);
            }
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (callback == null) return;
            //noinspection ConstantConditions
            callback.setItemCount(pager.getAdapter().getCount());
            callback.setPageScrolled(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
