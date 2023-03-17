package com.example.budget;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GetRef {
    private static String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();;

    public static Query getExpenseQuery(String item){

        DatabaseReference ref = FirebaseDatabase.getInstance("https://budgetapp-3b284-default-rtdb.europe-west1.firebasedatabase.app/").getReference("expense").child(onlineUserId);
        return  ref.orderByChild("item").equalTo(item);

    }
    public static Query getBudgetQuery(String item){

        DatabaseReference ref = FirebaseDatabase.getInstance("https://budgetapp-3b284-default-rtdb.europe-west1.firebasedatabase.app/").getReference("budget").child(onlineUserId);
        return  ref.orderByChild("item").equalTo(item);

    }
    public static Query getQueryByDate(String date){

        DatabaseReference ref= FirebaseDatabase.getInstance("https://budgetapp-3b284-default-rtdb.europe-west1.firebasedatabase.app/").getReference("expense").child(onlineUserId);
        return  ref.orderByChild("date").equalTo(date);
    }  public static DatabaseReference getBudgetRef(){
         FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

        return  FirebaseDatabase.getInstance("https://budgetapp-3b284-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("budget").child(firebaseAuth.getCurrentUser().getUid());
    }



}
