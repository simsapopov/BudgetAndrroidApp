package com.example.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.Table);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (item.getItemId()) {
            case R.id.budget:
            startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
            overridePendingTransition(0, 0);
            return true;
            case R.id.expenses:
            startActivity(new Intent(getApplicationContext(), TodaySpendingActivity.class));
            overridePendingTransition(0, 0);
                case R.id.Table:
            return true;
            case R.id.spendingCalendar:
            startActivity(new Intent(getApplicationContext(), SpendingCalendarActivity.class));
            overridePendingTransition(0, 0);
            return true;
            }
            return false;
            }
            });

        AddTotable(GetRef.getBudgetQuery("Transport"), findViewById(R.id.transportationBudget));
        AddTotable(GetRef.getBudgetQuery("House"),findViewById(R.id.HouseBudget));
        AddTotable(GetRef.getBudgetQuery("Charity"),findViewById(R.id.CharityBudget));
        AddTotable(GetRef.getBudgetQuery("Food"),findViewById(R.id.foodBudget));
        AddTotable(GetRef.getBudgetQuery("Personal"),findViewById(R.id.PersonalBudget));
        AddTotable(GetRef.getBudgetQuery("Other"),findViewById(R.id.OtherBudget));
        AddTotable(GetRef.getBudgetQuery("Health"),findViewById(R.id.HealthBudget));
        AddTotable(GetRef.getBudgetQuery("Travel"),findViewById(R.id.TravelBudget));
        AddTotable(GetRef.getBudgetQuery("Education"),findViewById(R.id.EducationBudget));
       // AddTotable(GetRef.getBudgetQuery("Entertainment"),findViewById(R.id.EnterttainmentBudget));

        AddTotable(GetRef.getExpenseQuery("Transport"),findViewById(R.id.transportationExp));
        AddTotable(GetRef.getExpenseQuery("House"),findViewById(R.id.HouseExp));
        AddTotable(GetRef.getExpenseQuery("Charity"),findViewById(R.id.CharityExp));
        AddTotable(GetRef.getExpenseQuery("Food"),findViewById(R.id.FoodExp));
        AddTotable(GetRef.getExpenseQuery("Personal"),findViewById(R.id.PersonalExp));
        AddTotable(GetRef.getExpenseQuery("Other"),findViewById(R.id.OtherExp));
        AddTotable(GetRef.getExpenseQuery("Health"),findViewById(R.id.HealthExp));
        AddTotable(GetRef.getExpenseQuery("Travel"), findViewById(R.id.TravelExp));
        AddTotable(GetRef.getExpenseQuery("Education"),findViewById(R.id.EducationExp));
      //  AddTotable(GetRef.getExpenseQuery("Entertainment"),findViewById(R.id.Enterexpens));
    }

    void AddTotable(Query query,TextView textView) {


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Data> myData = new ArrayList<>();
                        int totalAmmount =0;
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Map<String,Object> map = (Map<String, Object>)ds.getValue();
                            Object total=map.get("amount");
                            int flag=Integer.parseInt(String.valueOf(total));
                            totalAmmount+=flag;

                        }
                        String Sum = String.valueOf(totalAmmount);
                        textView.setText(Sum);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

        }







