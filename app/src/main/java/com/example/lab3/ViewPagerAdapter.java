package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        // Initialize fragments array with specific fragment instances
        fragments = new Fragment[]{
                new StartFragment(),
                new DetailsFragment(),
                new ListReservationsFragment()
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    public Fragment getFragmentAt(int position) {
        return fragments[position];
    }
}
