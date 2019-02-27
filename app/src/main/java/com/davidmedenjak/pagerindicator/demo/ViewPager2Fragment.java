package com.davidmedenjak.pagerindicator.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.davidmedenjak.pagerindicator.ViewPager2PagerListener;
import com.davidmedenjak.pagerindicator.demo.adapters.DummyAdapter;

public class ViewPager2Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 pager = view.findViewById(R.id.pager2);
        pager.setAdapter(new DummyAdapter());

        Indicators.bindIndicators(view, () -> new ViewPager2PagerListener(pager));
    }
}
