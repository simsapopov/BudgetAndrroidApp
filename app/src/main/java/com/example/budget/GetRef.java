package com.example.budget;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GetRef {
    private static String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();;

    public static void deleteExpenseItemByDateAndNotes(String date,String note){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://budgetapp-3b284-default-rtdb.europe-west1.firebasedatabase.app/").getReference("expense").child(onlineUserId);
        Query a =ref.orderByChild("date").equalTo(date).getRef().orderByChild("notes").equalTo(note);


        a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds1: snapshot.getChildren()){
                    ds1.getRef().removeValue();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

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
