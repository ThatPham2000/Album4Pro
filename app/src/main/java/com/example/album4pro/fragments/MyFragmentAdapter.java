package com.example.album4pro.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter extends FragmentStateAdapter {
    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LibraryFragment();
            case 1:
                return new AlbumsFragment();
            case 2:
                return new PrivateFragment();
            case 3:
                return new SearchFragment();
            case 4:
                return new SettingFragment();
            default:
                return new LibraryFragment();
        }
    }

    // Trả về số page
    @Override
    public int getItemCount() {
        return 5;
    }
}
