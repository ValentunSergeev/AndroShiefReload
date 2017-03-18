package com.valentun.androshief;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.valentun.androshief.Constants.APP_PREFERENCES;
import static com.valentun.androshief.Constants.TOKEN_LIFESPAN;
import static com.valentun.androshief.Support.decodeBitMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validateToken();

        initializeNavBar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_explore:
                break;
            case R.id.nav_home:
                break;
            case R.id.nav_cookbook:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeNavBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        setNavData(navigationView);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void validateToken() {
        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        long authTime = sPref.getLong("AUTH_TIME", 0);

        if (authTime == 0) {
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if (System.currentTimeMillis() - authTime > TOKEN_LIFESPAN) {
            new SignInTask(this, findViewById(R.id.main_container), Constants.AUTH_MODE.REAUTHORIZE)
                          .execute(sPref.getString("EMAIL", ""), sPref.getString("PASSWORD", ""));
        }
    }

    private void setNavData(NavigationView navigationView) {
        View headerLayout = navigationView.getHeaderView(0);
        TextView email = (TextView) headerLayout.findViewById(R.id.nav_head_email);
        TextView name = (TextView) headerLayout.findViewById(R.id.nav_head_name);
        CircleImageView avatar = (CircleImageView) headerLayout.findViewById(R.id.nav_head_image);

        SharedPreferences sPref = getSharedPreferences(Constants.APP_PREFERENCES, MODE_PRIVATE);
        String savedEmail = sPref.getString("EMAIL", "");
        String savedName = sPref.getString("NAME", "");
        String savedImage = sPref.getString("IMAGE", "");

        email.setText(savedEmail);
        name.setText(savedName);

        avatar.setImageBitmap(decodeBitMap(savedImage));
    }
}
