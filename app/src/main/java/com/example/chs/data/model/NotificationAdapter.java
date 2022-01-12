package com.example.chs.data.model;

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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private HashMap<String,String> postList;
    private OnItemListener itemListener;
    public NotificationAdapter(HashMap<String,String> data, OnItemListener listener){
        this.itemListener = listener;
        this.postList=data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        System.out.println("////////////");
        View listItem = layoutInflater.inflate(R.layout.notification_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem, itemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.name.setText("h");
       holder.description.setText("hello");
       System.out.println("////////////");
    }

    @Override
    public int getItemCount(){
        //return localDataSet.length;
        return postList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;

        public TextView description;

        public OnItemListener onClickListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onClickListener) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.numenot);
            System.out.println("////////////");
            this.description = (TextView) itemView.findViewById(R.id.descnot);

            ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.notlay);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener((View.OnClickListener) this);
            System.out.println("////////////");
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
