package com.example.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodayItemsAdapter extends RecyclerView.Adapter<TodayItemsAdapter.ViewHolder>{
    private Context mContext;
    private String post_key=" ";
    private String item="";
    private int amount =0;
    private List<Data> myDataList;



    public TodayItemsAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout,parent,false);
        return new TodayItemsAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Data data = myDataList.get(position);
            holder.amount.setText(mContext.getString(R.string.Amount_Todays_Item_adapter)+data.getAmount());
            holder.date.setText(mContext.getString(R.string.on_Todays_Item_adapter)+data.getDate());
            holder.notes.setText(mContext.getString(R.string.note_Today_Items_adapter)+data.getNotes());
        switch (data.getItem()){
            case "Transport":

                holder.imageView.setImageResource(R.drawable.ic_baseline_directions_bus_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Transport));
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_baseline_fastfood_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Food));
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_baseline_home_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.House));
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_baseline_theater_comedy_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Entertainment));
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_baseline_school_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Education));
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_baseline_emoji_people_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Charity));
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Personal));
                break;
            case "Travel":
                holder.imageView.setImageResource(R.drawable.ic_baseline_airplanemode_active_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Travel));
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_baseline_heart_broken_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Health));
                
                
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_baseline_menu_24);
                holder.item.setText(mContext.getString(R.string.item_)+ mContext.getString(R.string.Other));
                break;


        }



    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item,amount,date,notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_retrieve);
            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);





        }
    }
}
