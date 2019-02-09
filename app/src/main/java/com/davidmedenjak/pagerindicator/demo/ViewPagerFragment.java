package com.davidmedenjak.pagerindicator.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.davidmedenjak.pagerindicator.PagerIndicatorView;
import com.davidmedenjak.pagerindicator.ViewPagerPagerListener;
import com.davidmedenjak.pagerindicator.demo.adapters.DummyPagerAdapter;

public class ViewPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager pager = view.findViewById(R.id.pager);
        pager.setAdapter(new DummyPagerAdapter());

        PagerIndicatorView indicator = view.findViewById(R.id.indicator);
        indicator.setPager(new ViewPagerPagerListener(pager));
    }
}
