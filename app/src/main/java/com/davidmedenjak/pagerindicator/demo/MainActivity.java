package com.davidmedenjak.pagerindicator.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("RecyclerView"));
        tabLayout.addTab(tabLayout.newTab().setText("ViewPager"));
        tabLayout.addTab(tabLayout.newTab().setText("ViewPager2"));

        TabLayout.OnTabSelectedListener selectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                final Fragment fragment;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new RecyclerViewFragment();
                        break;
                    case 1:
                        fragment = new ViewPagerFragment();
                        break;
                    case 2:
                        fragment = new ViewPager2Fragment();
                        break;
                    default:
                        return;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, fragment)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
        tabLayout.addOnTabSelectedListener(selectedListener);
        selectedListener.onTabSelected(tabLayout.getTabAt(0));
    }

}
