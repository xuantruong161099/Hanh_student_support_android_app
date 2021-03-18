package com.meowbeos.studentsupport.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.fragment.competition.CompetitionFragment;
import com.meowbeos.studentsupport.fragment.document.DocumentFragment;
import com.meowbeos.studentsupport.fragment.enrollclass.EnrollClassFragment;
import com.meowbeos.studentsupport.fragment.followbus.BusFragment;
import com.meowbeos.studentsupport.fragment.groupsubjects.GroupsFragment;
import com.meowbeos.studentsupport.fragment.marks.MarkFragment;
import com.meowbeos.studentsupport.fragment.news.NewsFragment;
import com.meowbeos.studentsupport.fragment.schedule.ScheduleFragment;
import com.meowbeos.studentsupport.fragment.support.SupportFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    String studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        handleEvents();
        receiveData();
        //sendData();
    }

    private void initComponents() {
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void handleEvents() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.news:
                    selectedFragment = new NewsFragment();
                    break;
                case R.id.schedule:
                    selectedFragment = new ScheduleFragment();
                    break;
                case R.id.document:
                    selectedFragment = new DocumentFragment();
                    break;
                case R.id.support:
                    selectedFragment = new SupportFragment();
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mark:
                selectedFragment = new MarkFragment();
                sendData();
                break;
            case R.id.nav_groups:
                selectedFragment = new GroupsFragment();
                sendData();
                break;
            case R.id.nav_competition:
                selectedFragment = new CompetitionFragment();
                break;
            case R.id.nav_enroll:
                selectedFragment = new EnrollClassFragment();
                sendData();
                break;
            case R.id.nav_bus:
                selectedFragment = new BusFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void receiveData() {
        Intent intent = getIntent();
        studentID = intent.getStringExtra("studentID");
    }

    private void sendData() {
        Bundle bundle = new Bundle();
        bundle.putString("studentID", studentID);
        selectedFragment.setArguments(bundle);
    }
}