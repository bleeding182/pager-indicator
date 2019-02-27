package com.davidmedenjak.pagerindicator.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmedenjak.pagerindicator.RecyclerViewPagerListener;
import com.davidmedenjak.pagerindicator.demo.adapters.DummyAdapter;

public class RecyclerViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new DummyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        Indicators.bindIndicators(view, () -> new RecyclerViewPagerListener(recyclerView));
    }
}
