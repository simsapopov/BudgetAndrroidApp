package com.example.budget;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpendingCalendarActivity extends AppCompatActivity {
    DatabaseReference budgetRef;

    private FloatingActionButton fab;
    public TextView totalBudgetAmountTextView;
    private RecyclerView recyclerView;
    private String onlineUserId="";
    private FloatingActionButton floatingActionButton;
    private  TodayItemsAdapter todayItemsAdapter;
    private List<Data> myData;
    private int amount =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_calendar);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        totalBudgetAmountTextView=findViewById(R.id.TotalAmount);
        getSupportActionBar().hide();
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab=findViewById(R.id.fab);
        myData = new ArrayList<>();
        bottomNavigationView.setSelectedItemId(R.id.spendingCalendar);

        todayItemsAdapter = new TodayItemsAdapter(SpendingCalendarActivity.this,myData);
        recyclerView.setAdapter(todayItemsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Searchdate();
            }
        });
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

                    case R.id.spendingCalendar:
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
    private void Searchdate() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.datepicker, null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.show();
        dialog.setCancelable(true);
        final EditText date = myView.findViewById(R.id.date_input);
        final EditText month = myView.findViewById(R.id.month_input);
        final EditText year = myView.findViewById(R.id.year_input);
        final Button submit = myView.findViewById(R.id.submit_button);
        final EditText notes = myView.findViewById(R.id.noteForDelete);
        final Button deleteBtn = myView.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_ = date.getText().toString();
                if(date_.isEmpty()){
                    date.setError(getString(R.string.enter_Date));
                }
                if(date_.length()<2&&!date_.isEmpty()){
                    date_ = "0"+date_;
                }
                String noteForDelete= notes.getText().toString();
                if(TextUtils.isEmpty(noteForDelete)){
                    notes.setError(getString(R.string.enter_Note_Delete));
                }
                String month_ = month.getText().toString();
                if(month_.isEmpty()){
                    month.setError(getString(R.string.enter_Month));
                }
                if(month_.length()<2){
                    month_ = "0"+month_;
                }

                String year_ = year.getText().toString();
                if(year_.isEmpty()){
                    year.setError(getString(R.string.enter_Year));
                }

                String date = ""+date_+"-"+month_+"-"+year_;
                if(!year_.isEmpty()&&!date_.isEmpty()&&!month_.isEmpty()&&!noteForDelete.isEmpty()){
                GetRef.deleteExpenseItemByDateAndNotes(date,notes.getText().toString());
                Query query = GetRef.getQueryByDate(date);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myData.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Data data = dataSnapshot.getValue(Data.class);
                            myData.add(data);
                        }
                        todayItemsAdapter.notifyDataSetChanged();
                        int totalAmmount =0;
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Map<String,Object> map = (Map<String, Object>)ds.getValue();
                            Object total=map.get("amount");
                            int flag=Integer.parseInt(String.valueOf(total));
                            totalAmmount+=flag;

                        }
                        StringBuilder spendingMsg= new StringBuilder();
                        spendingMsg.append(getString(R.string.amount_spent_on_datte));
                        spendingMsg.append(" ");
                        spendingMsg.append(date);
                        spendingMsg.append(" ");
                        spendingMsg.append(getString(R.string.is));
                        spendingMsg.append(" ");
                        spendingMsg.append(totalAmmount);
                        TextView textView = findViewById(R.id.TotalAmount);
                        textView.setText(spendingMsg.toString());




                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}


            }

        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_ = date.getText().toString();
                if(date_.isEmpty()){
                    date.setError(getString(R.string.enter_Date));
                }
                if(date_.length()<2&&!date_.isEmpty()){
                    date_ = "0"+date_;
                }
                String month_ = month.getText().toString();
                if(month_.isEmpty()){
                    month.setError(getString(R.string.enter_Month));
                }
                if(month_.length()<2){
                    month_ = "0"+month_;
                }

                String year_ = year.getText().toString();
                if(year_.isEmpty()){
                    year.setError(getString(R.string.enter_Year));
                }

                String date = ""+date_+"-"+month_+"-"+year_;
                if(!year_.isEmpty()&&!date_.isEmpty()&&!month_.isEmpty()){
                    dialog.dismiss();
                    Query query = GetRef.getQueryByDate(date);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            myData.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Data data = dataSnapshot.getValue(Data.class);
                                myData.add(data);
                            }
                            todayItemsAdapter.notifyDataSetChanged();
                            int totalAmmount =0;
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Map<String,Object> map = (Map<String, Object>)ds.getValue();
                                Object total=map.get("amount");
                                int flag=Integer.parseInt(String.valueOf(total));
                                totalAmmount+=flag;

                            }
                            StringBuilder spendingMsg= new StringBuilder();
                            spendingMsg.append(getString(R.string.amount_spent_on_datte));
                            spendingMsg.append(" ");
                            spendingMsg.append(date);
                            spendingMsg.append(" ");
                            spendingMsg.append(getString(R.string.is));
                            spendingMsg.append(" ");
                            spendingMsg.append(totalAmmount);
                            TextView textView = findViewById(R.id.TotalAmount);
                            textView.setText(spendingMsg.toString());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }

        });



    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(budgetRef,Data.class).build();
    }
}

//TODO make text in String.xml to enable Bulgarian