package com.example.budget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TodaySpendingActivity extends AppCompatActivity {
        private TextView totalAmount;
        private FirebaseAuth mAuth;
        private String onlineUserId="";
        private DatabaseReference expenseRef;
        private ProgressBar prograssBar;
        private RecyclerView recyclerView;
        private FloatingActionButton floatingActionButton;
        private ProgressDialog loader;
        private  TodayItemsAdapter todayItemsAdapter;
        private List<Data> myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_today_spending);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        mAuth= FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        onlineUserId=mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance(getString(R.string.databaseURL)).getReference("expense").child(onlineUserId);
        loader = new ProgressDialog(this);
        floatingActionButton = findViewById(R.id.fab);
        totalAmount = findViewById(R.id.TotalAmount);
        prograssBar=findViewById(R.id.progress_bar);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);




        myData = new ArrayList<>();
         readItems();
        todayItemsAdapter = new TodayItemsAdapter(TodaySpendingActivity.this,myData);
        recyclerView.setAdapter(todayItemsAdapter);


      bottomNavigationView.setSelectedItemId(R.id.expenses);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemSpent();
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
                        return true;
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

    private void readItems() {
        DateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal=Calendar.getInstance();
        String date = dateformat.format(cal.getTime());
        DatabaseReference ref= FirebaseDatabase.getInstance(getString(R.string.databaseURL)).getReference("expense").child(onlineUserId);
        Query query = ref.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myData.add(data);
                }
                todayItemsAdapter.notifyDataSetChanged();


                prograssBar.setVisibility(View.GONE);
                int totalAmmount =0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total=map.get("amount");
                    int flag=Integer.parseInt(String.valueOf(total));
                    totalAmmount+=flag;

                }
                totalAmount.setText("Total spending for today= "+totalAmmount);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addItemSpent(){
        AlertDialog.Builder myDialog= new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout,null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        final Spinner itemSpinner = myView.findViewById(R.id.itemSpinner);
        final EditText amount = myView.findViewById(R.id.amountBudget);
        final EditText note = myView.findViewById(R.id.noteBudget);
        final Button cansel = myView.findViewById(R.id.cancel_button);
        final Button save = myView.findViewById(R.id.saveBtn);
        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Amount=amount.getText().toString();
                String Item=itemSpinner.getSelectedItem().toString();
                String notes = note.getText().toString();

                if(TextUtils.isEmpty(Amount)){
                    amount.setError(getString(R.string.AmoutReq));
                    return;
                }else if(Item.equals("Select item")){
                    Toast.makeText(TodaySpendingActivity.this, R.string.SelecetvalidItem,Toast.LENGTH_LONG).show();
                }  else if(TextUtils.isEmpty(notes)){
                    note.setError(getString(R.string.NoteReq));
                }
                else{
                    loader.setMessage("adding a budget item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    String id = expenseRef.push().getKey();
                    DateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal=Calendar.getInstance();
                    String date = dateformat.format(cal.getTime());
                    MutableDateTime epoch=new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch,now);
                    Data data = new Data(Item,id,date,notes,Integer.parseInt(Amount),months.getMonths());
                    expenseRef.child(id).setValue(data).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(TodaySpendingActivity.this, R.string.Expenseadded,Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(TodaySpendingActivity.this, R.string.Err,Toast.LENGTH_LONG);
                        }
                        loader.dismiss();
                    });
                }
                dialog.dismiss();

            }
        });
        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}