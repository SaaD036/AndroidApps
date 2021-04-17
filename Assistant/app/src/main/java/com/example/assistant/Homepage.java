package com.example.assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initComp();

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout, new FragmentHomeHome()).commit();
    }

    private void initComp(){
        bottomNavigationView = findViewById(R.id.home_bottomNav);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.home_bottom_menu_home:
                    selectedFragment = new FragmentHomeHome();
                    break;
                case R.id.home_bottom_menu_apply:
                    selectedFragment = new FragmentHomeApply();
                    break;
                case R.id.home_bottom_menu_appliedJob:
                    selectedFragment = new FragmentHomeApplied();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout, selectedFragment).commit();
            return true;
        }
    };
}
