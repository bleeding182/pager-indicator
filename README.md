## PagerIndicator

WIP of a small library for page indicators as a line or circle.

Works with:
* RecyclerView
* ViewPager
* ViewPager2

### Features

Different indicators:

* line indicator
* circle indicator



### Setup

// todo publish artifacts!

### Usage

You can check out the demo application to see what's possible.

To use the indicator in your project add the `PagerIndicatorView` to your layout.

    <?xml version="1.0" encoding="utf-8"?>
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!-- >> Your Pager / RecyclerView << -->

        <com.davidmedenjak.pagerindicator.PagerIndicatorView
            android:id="@+id/indicator"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|center_horizontal"
            android:paddingTop="4dp"
            android:background="#33000000"
            android:paddingBottom="4dp" />
    </FrameLayout>

Then register the listener and done!

    PagerIndicatorView indicator = view.findViewById(R.id.indicator);
    
    indicator.setPager(new RecyclerViewPagerListener(recyclerView));
      // ... or ...
    indicator.setPager(new ViewPagerPagerListener(pager));
      // ... or ...
    indicator.setPager(new ViewPager2PagerListener(pager));

