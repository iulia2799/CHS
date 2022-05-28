package com.example.chs.data.model;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chs.Notification;
import com.example.chs.R;
import com.example.chs.data.Post;
import com.example.chs.data.PostAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private HashMap<String,String> postList;
    private OnItemListener itemListener;
    private List<Alert> alertList = new ArrayList<>();
    public NotificationAdapter(List<Alert> alerts, OnItemListener listener){
        this.itemListener = listener;
        alertList.addAll(alerts);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        System.out.println("1////////////");
        View listItem = layoutInflater.inflate(R.layout.notification_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem, itemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time = Long.parseLong(alertList.get(position).getDate());
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        holder.name.setText(date);
        holder.description.setText(alertList.get(position).getDescription());
        System.out.println("2////////////");
    }

    @Override
    public int getItemCount(){
        //return localDataSet.length;
        System.out.println("3////////////");
        return alertList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;

        public TextView description;

        public OnItemListener onClickListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onClickListener) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.numenot);
            System.out.println("4////////////");
            this.description = (TextView) itemView.findViewById(R.id.descnot);

            ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.notlay);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener((View.OnClickListener) this);
            System.out.println("5////////////");
        }

        @Override
        public void onClick(View view) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemListener{
        void onItemClick(int position);
    }
}
