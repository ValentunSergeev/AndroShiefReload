package com.valentun.androshief.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.valentun.androshief.Adapters.RecyclerViewAdapter;
import com.valentun.androshief.Constants;
import com.valentun.androshief.DTOs.Category;
import com.valentun.androshief.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.valentun.androshief.Utils.getAuthHeaders;

/**
 * Created by Valentun on 19.03.2017.
 */

public class RecyclerFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;

    private Category category;
    private View view;
    private RecyclerViewAdapter adapter;

    public RecyclerFragment(Category category) {
        this.category = category;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new CategoryTask().execute();

        this.view = view;

        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        adapter = new RecyclerViewAdapter(category);
        mRecyclerView.setAdapter(adapter);
    }


    private class CategoryTask extends AsyncTask<String, Void, Category> {

        @Override
        protected Category doInBackground(String... strings) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Category result = null;

            HttpEntity<String> entity = new HttpEntity<>("", getAuthHeaders(getActivity()));

            String url = Constants.URL.CATEGORIES + category.getId();

            try {
                ResponseEntity<Category> response = restTemplate.exchange(url, HttpMethod.GET,
                        entity, Category.class);
                result = response.getBody();
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            return result;
        }


        @Override
        protected void onPostExecute(Category response) {
            adapter.setCategory(response);
        }
    }
}
