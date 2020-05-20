package com.swap.newsdesk.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.swap.newsdesk.R;
import com.swap.newsdesk.fragments.AllNews;
import com.swap.newsdesk.fragments.Categories;
import com.swap.newsdesk.fragments.CrimeReport;
import com.swap.newsdesk.fragments.Culture;
import com.swap.newsdesk.fragments.Entertainment;
import com.swap.newsdesk.fragments.Sports;
import com.swap.newsdesk.fragments.TopHeadelines;

public class Master extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentManager = getSupportFragmentManager();
        TopHeadelines headelines = new TopHeadelines();
        fragmentManager.beginTransaction().add(R.id.fragment_container, headelines).commit();

        navigationView = (NavigationView) this.findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.nav_top_headlines);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_top_headlines:
                        fragmentManager = getSupportFragmentManager();
                        TopHeadelines headelines = new TopHeadelines();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, headelines).commit();
                        break;
                    case R.id.nav_everything:
                        fragmentManager = getSupportFragmentManager();
                        AllNews allNews = new AllNews();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, allNews).commit();
                        break;
                    case R.id.nav_crime:
                        fragmentManager = getSupportFragmentManager();
                        CrimeReport crimeReport = new CrimeReport();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, crimeReport).commit();
                        break;
                    case R.id.nav_entertainment:
                        fragmentManager = getSupportFragmentManager();
                        Entertainment entertainment = new Entertainment();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, entertainment).commit();
                        break;
                    case R.id.nav_sports:
                        fragmentManager = getSupportFragmentManager();
                        Sports sports = new Sports();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, sports).commit();
                        break;
                    case R.id.nav_culture:
                        fragmentManager = getSupportFragmentManager();
                        Culture culture = new Culture();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, culture).commit();
                        break;
                    case R.id.nav_other:
                        fragmentManager = getSupportFragmentManager();
                        Categories categories = new Categories();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, categories).commit();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
