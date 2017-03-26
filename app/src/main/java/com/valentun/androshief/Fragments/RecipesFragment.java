package com.valentun.androshief.Fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.valentun.androshief.Constants;
import com.valentun.androshief.DTOs.Category;
import com.valentun.androshief.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import mehdi.sakout.dynamicbox.DynamicBox;

import static com.valentun.androshief.Utils.decodeBitmap;
import static com.valentun.androshief.Utils.getAuthHeaders;


public class RecipesFragment extends Fragment {
    private View view;
    private DynamicBox box;
    private AppCompatActivity activity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        box = new DynamicBox(getActivity(), view);
        box.showLoadingLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getContext().getTheme().applyStyle(R.style.AppTheme_MaterialPager, true);

        view = inflater.inflate(R.layout.fragment_recipes, container, false);

        activity = (AppCompatActivity) getActivity();

        activity.setTitle("Recipes");

        new CategoriesTask().execute();

        return view;
    }

    @Override
    public void onDestroy() {
        getContext().getTheme().applyStyle(R.style.AppTheme_NoActionBar, true);
        super.onDestroy();
    }

    private void initializeMaterialPager(ArrayList<Category> categories) {
        MaterialViewPager mViewPager = (MaterialViewPager) view.findViewById(R.id.materialViewPager);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            activity.setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return new RecyclerFragment(categories.get(position));
            }

            @Override
            public int getCount() {
                return categories.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return categories.get(position).getName();
            }
        });

        mViewPager.setMaterialViewPagerListener(page -> {
            Bitmap bmp = decodeBitmap(categories.get(page).getImage(), true);
            Drawable image = new BitmapDrawable(RecipesFragment.this.getResources(), bmp);
            return HeaderDesign.fromColorResAndDrawable(R.color.colorPrimary, image);
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    private class CategoriesTask extends AsyncTask<String, Void, ArrayList<Category>> {

        @Override
        protected ArrayList<Category> doInBackground(String... strings) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ArrayList<Category> result = null;

            HttpEntity<String> entity = new HttpEntity<>("", getAuthHeaders(getActivity()));

            try {
                ResponseEntity<Category[]> response = restTemplate.exchange(Constants.URL.CATEGORIES,
                                                                            HttpMethod.GET, entity,
                                                                            Category[].class);
                result = new ArrayList<>(Arrays.asList(response.getBody()));
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<Category> response) {
            box.hideAll();
            initializeMaterialPager(response);
        }
    }

}
