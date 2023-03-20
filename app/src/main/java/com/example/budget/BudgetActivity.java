package com.example.budget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

public class BudgetActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private DatabaseReference budgetRef;
    private ProgressDialog loader;
    private TextView totalBudgetAmountTextView;
    private RecyclerView recyclerView;
    private String post_key=" ";
    private String item="";
    public int totalAmount=0;

    private int amount =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalAmount=0;
        setContentView(R.layout.activity_budget);
        NavigationBarView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        totalBudgetAmountTextView=findViewById(R.id.TotalBudget);
        recyclerView=findViewById(R.id.recyclerView);
        budgetRef= GetRef.getBudgetRef();
        loader=new ProgressDialog(this);
        bottomNavigationView.setSelectedItemId(R.id.budget);
        fab=findViewById(R.id.fab);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
              addItem();
          }
      });
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
    void totalAmountChange(Query query, TextView textView) {
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
                textView.setText(getString(R.string.total_budget)+" = "+totalAmmount);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void addItem() {
        totalAmountChange(budgetRef,totalBudgetAmountTextView);
        AlertDialog.Builder myDialog= new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout,null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        final Spinner itemSpinner = myView.findViewById(R.id.itemSpinner);
        final EditText amount = myView.findViewById(R.id.amountBudget);
        final Button cansel = myView.findViewById(R.id.cancel_button);
        final Button save = myView.findViewById(R.id.saveBtn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetItem;
                String budgetAmount=amount.getText().toString();

                 budgetItem=itemSpinner.getSelectedItem().toString();
                if(itemSpinner.getSelectedItem().toString().equals("Транспорт")){
                 budgetItem="Transport";
                }
                if(itemSpinner.getSelectedItem().toString().equals("Храна")){
                 budgetItem="Food";
                }if(itemSpinner.getSelectedItem().toString().equals("За дома")){
                 budgetItem="House";
                }if(itemSpinner.getSelectedItem().toString().equals("Забавление")){
                 budgetItem="Entertainment";
                }if(itemSpinner.getSelectedItem().toString().equals("Образование")){
                 budgetItem="Education";
                }if(itemSpinner.getSelectedItem().toString().equals("Благотворителност")){
                 budgetItem="Charity";
                }if(itemSpinner.getSelectedItem().toString().equals("Лични разходи")){
                 budgetItem="Personal";
                }if(itemSpinner.getSelectedItem().toString().equals("Пътувания")){
                 budgetItem="Travel";
                }if(itemSpinner.getSelectedItem().toString().equals("Здравни разходи")){
                 budgetItem="Health";
                }if(itemSpinner.getSelectedItem().toString().equals("Други")){
                 budgetItem="Other";
                }
                if (TextUtils.isEmpty(amount.getText().toString())) {
                    amount.setError("Amount is required");
                    return;
                }else  if(budgetItem.equals("Select item")||budgetItem.equals("Категория")){
                    Toast.makeText(BudgetActivity.this, R.string.select_valid_item ,Toast.LENGTH_LONG).show();
                    return;
                } else{
                    totalAmount=totalAmount+ Integer.parseInt(amount.getText().toString());
                   loader.setMessage("adding a budget item");
                    loader.setCanceledOnTouchOutside(false);
                  loader.show();
                    String id = budgetRef.push().getKey();
                    DateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal=Calendar.getInstance();
                    String date = dateformat.format(cal.getTime());
                    MutableDateTime epoch=new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch,now);

                    Data data = new Data(budgetItem,id,date,null,Integer.parseInt(budgetAmount),months.getMonths());
                    budgetRef.child(id).setValue(data).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(BudgetActivity.this, R.string.budget_added_suc,Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(BudgetActivity.this,"Error",Toast.LENGTH_LONG);
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(budgetRef,Data.class).build();
        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position,@NonNull Data model){
                totalAmountChange(budgetRef,totalBudgetAmountTextView);
                holder.setItemAmount(getString(R.string.amount_allocateed_BudgetActivity)+ model.getAmount());
                holder.setDate(getString(R.string.on_Date)+ model.getDate());


                holder.notes.setVisibility(View.GONE);
                switch (model.getItem()){
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_directions_bus_24);
                        holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Transport));;
                        break;
                    case "Food":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_fastfood_24);
                        holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Food));;
                        break;
                        case "House":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_home_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.House));;
                        break;
                        case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_theater_comedy_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Entertainment));;
                        break;
                        case "Education":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_school_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Education));;
                        break;
                        case "Charity":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_emoji_people_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Charity));;
                        break;
                        case "Personal":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Personal));;
                        break;
                        case "Travel":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_airplanemode_active_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Travel));;
                        break;
                        case "Health":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_heart_broken_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Health));;
                        break;
                        case "Other":
                        holder.imageView.setImageResource(R.drawable.ic_baseline_menu_24);
                            holder.setItemName(getString(R.string.Budget_Item)+ getString(R.string.Other));;
                        break;


                }
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();
                        item= model.getItem();
                        amount= model.getAmount();
                        updateData();
                    }
                });


            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout,parent,false);
                return new MyViewHolder(view);

            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();


    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView notes,date;


         public MyViewHolder(@NonNull View itemView){
             super(itemView);
             mView = itemView;
             imageView=itemView.findViewById(R.id.image_view_retrieve);
             notes=itemView.findViewById(R.id.note);
             date = itemView.findViewById(R.id.date);



         }
         public void setItemName(String itemName){
             TextView item = mView.findViewById(R.id.item);
             item.setText(itemName);
         }
        public void setItemAmount(String itemAmount){
            TextView item = mView.findViewById(R.id.amount);
            item.setText(itemAmount);
        }
        public void setDate(String itemDate){
            TextView item = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }

    }

    private void updateData(){
        totalAmountChange(budgetRef,totalBudgetAmountTextView);
        AlertDialog.Builder mDialog=new AlertDialog.Builder(this);
        LayoutInflater inflater= LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout,null);
        mDialog.setView(mView);
        final AlertDialog dialog = mDialog.create();
        final TextView mItem=mView.findViewById(R.id.itemName);
        final TextView mAmount=mView.findViewById(R.id.amount);
        final EditText mNotes=mView.findViewById(R.id.note);
        mNotes.setVisibility(View.GONE);
        mItem.setText(item);
        mAmount.setText(String.valueOf(amount));
        Button deleteBtn = mView.findViewById(R.id.deleteBtn);
        Button updateBtn = mView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(mAmount.getText().toString());
                DateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal=Calendar.getInstance();
                String date = dateformat.format(cal.getTime());
                MutableDateTime epoch=new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch,now);
                Data data = new Data(item,post_key,date,null,amount,months.getMonths());
                budgetRef.child(post_key).setValue(data).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(BudgetActivity.this, R.string.successfully_update,Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(BudgetActivity.this,task.getException().toString(),Toast.LENGTH_LONG);
                    }
                });
                dialog.dismiss();
            }
        });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        budgetRef.child(post_key).removeValue().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(BudgetActivity.this, R.string.sucessfully_del,Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(BudgetActivity.this,task.getException().toString(),Toast.LENGTH_LONG);
                            }
                        });
                        dialog.dismiss();
                    }
                });

        dialog.show();

    }
}