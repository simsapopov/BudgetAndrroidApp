package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        TextView textUsername = findViewById(R.id.TextMainActivity1);
        TextView textForItemsInsert = findViewById(R.id.TextMainActivity2);
        StringBuilder username=new StringBuilder() ;

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        int index = email.indexOf("@");
        email = email.substring(0,index);
        username.append(email);
        boolean[] result = new boolean[1];
        ;
        if(GetRef.checkRecord()){

            textForItemsInsert.setText(R.string.continue_inserting_main);
        }else {

        }
        textUsername.setText(getString(R.string.hello_again)+" "+email);
        getSupportActionBar().hide();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(item.getItemId())
                {
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.expenses:
                        startActivity(new Intent(getApplicationContext(), TodaySpendingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Table:
                            startActivity(new Intent(getApplicationContext(), TableActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    case R.id.spendingCalendar:
                        startActivity(new Intent(getApplicationContext(), SpendingCalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
}