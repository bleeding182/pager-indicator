package com.davidmedenjak.pagerindicator.demo;

import android.view.View;
import android.view.ViewGroup;

import com.davidmedenjak.pagerindicator.PagerIndicatorView;

final class Indicators {

    static void bindIndicators(View view, java.util.function.Supplier<PagerIndicatorView.PagerListener> listenerSupplier) {
        ViewGroup container = (ViewGroup) view;
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof PagerIndicatorView) {
                PagerIndicatorView indicator = (PagerIndicatorView) child;
                indicator.setPager(listenerSupplier.get());
            }
        }
    }

}
