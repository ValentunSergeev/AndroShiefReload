package com.valentun.androshief.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.valentun.androshief.R;


public class RecipesFragment extends Fragment {
    private View view;
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recipes, container, false);

        activity = (AppCompatActivity) getActivity();

        activity.setTitle("Recipes");

        initializeMaterialPager();

        return  view;
    }

    private void initializeMaterialPager() {
        MaterialViewPager mViewPager = (MaterialViewPager) view.findViewById(R.id.materialViewPager);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            activity.setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        return new RecyclerFragment(ResourcesCompat.getColor(getResources(),
                                                                             R.color.colorAccent,
                                                                             null));
                    case 1:
                        return new RecyclerFragment(ResourcesCompat.getColor(getResources(),
                                                                             R.color.colorPrimary,
                                                                             null));
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "Deserts";

                    case 1:
                        return "Vegetables";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(page -> {
            switch (page) {
                case 0:
                    return HeaderDesign.fromColorResAndDrawable(
                            R.color.accent_color,
                            ResourcesCompat.getDrawable(getResources(), R.drawable.desert, null)
                            );
                case 1:
                    return HeaderDesign.fromColorResAndDrawable(
                            R.color.green,
                            ResourcesCompat.getDrawable(getResources(), R.drawable.vegetables, null)
                            );
            }

            return null;
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

}
