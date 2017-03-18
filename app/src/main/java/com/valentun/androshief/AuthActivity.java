package com.valentun.androshief;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.valentun.androshief.Adapters.PageAdapter;

import static com.valentun.androshief.Constants.APP_PREFERENCES;

/**
 * Created by Valentun on 14.03.2017.
 */

public class AuthActivity extends AppCompatActivity {

    private CoordinatorLayout fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initializeTabs();

        checkSavedAuthData();
    }

    private void checkSavedAuthData() {
        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        String email = sPref.getString("EMAIL", null);

        if (email != null) {
            String password = sPref.getString("PASSWORD", null);
            new SignInTask(this, fragmentContainer, Constants.AUTH_MODE.SIGN_IN).execute(email, password);
        }
    }


    private void initializeTabs(){
        fragmentContainer = (CoordinatorLayout) findViewById(R.id.auth_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.register));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sign_in));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
