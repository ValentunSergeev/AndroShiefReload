package com.valentun.androshief.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.valentun.androshief.Fragments.RegisterFragment;
import com.valentun.androshief.Fragments.SignInFragment;


public class PageAdapter extends FragmentStatePagerAdapter {
    private int tabsCount;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.tabsCount = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RegisterFragment();
            case 1:
                return new SignInFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsCount;
    }

}
