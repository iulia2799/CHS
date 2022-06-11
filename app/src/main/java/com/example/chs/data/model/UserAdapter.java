package com.example.chs.data.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chs.R;
import com.example.chs.data.login.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private final List<User> userList;
    private final UserAdapter.OnItemListener listener;

    public UserAdapter(List<User> users,OnItemListener listener){
        this.listener = listener;
        this.userList= users;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user,parent,false);
        return new ViewHolder(listItem, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User xUser = userList.get(position);
        holder.rank.setText(String.valueOf(position+1));
        holder.username.setText(xUser.getUsername());
        holder.points.setText(String.valueOf(xUser.getPoints()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView rank;
        public TextView username;
        public TextView points;
        public OnItemListener onClickListener;
        public ViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            this.rank = itemView.findViewById(R.id.user_rank);
            this.username = itemView.findViewById(R.id.user_name);
            this.points = itemView.findViewById(R.id.user_points);
            ConstraintLayout constraintLayout = itemView.findViewById(R.id.userlayout);
            this.onClickListener = onItemListener;
            itemView.setOnClickListener((View.OnClickListener)this);
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
