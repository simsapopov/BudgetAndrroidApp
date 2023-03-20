package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

public class UserGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(item.getItemId())

                {
                    case R.id.expenses:

                        startActivity(new Intent(getApplicationContext(), TodaySpendingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.spendingCalendar:
                        startActivity(new Intent(getApplicationContext(), SpendingCalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Table:

                        startActivity(new Intent(getApplicationContext(), TableActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    }
