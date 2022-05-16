package com.example.chs.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chs.R;
import com.example.chs.data.model.Act;
import com.example.chs.data.model.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActAdapter extends RecyclerView.Adapter<ActAdapter.ViewHolder>{
    private List<Act> links = new ArrayList<>();
    private OnItemListener onItemListener;
    public ActAdapter(List<Act> links, OnItemListener listener){
        for(Act l : links){
            this.links.add(l);
        }
        this.onItemListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.act,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem,onItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.link = links.get(position).getLink();
        holder.name.setText(links.get(position).getNume());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public String link;
        public TextView name;
        public OnItemListener onClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewHolder(@NonNull View listItem, OnItemListener onItemListener) {
            super(listItem);
            this.name = listItem.findViewById(R.id.link);
            ConstraintLayout layout = listItem.findViewById(R.id.actlayout);
            this.onClickListener = onItemListener;
            listItem.setOnClickListener((View.OnClickListener) this);
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
