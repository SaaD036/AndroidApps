package com.example.saad_blog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, new FragmentMainUser()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void initComp(){
        bottomNavigationView = findViewById(R.id.main_bottomNavigationView);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedItem = null;

            switch (item.getItemId()){
                case R.id.main_bottomNav_user:
                    selectedItem = new FragmentMainUser();
                    break;

                case R.id.main_bottomNav_admin:
                    selectedItem = new FragmentMainAdmin();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, selectedItem).commit();

            return true;
        }
    };
}
