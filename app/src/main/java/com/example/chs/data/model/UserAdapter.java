package com.example.chs.data.model;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chs.R;
import com.example.chs.data.login.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> userList;
    private UserAdapter.OnItemListener listener;

    public UserAdapter(List<User> users,OnItemListener listener){
        this.listener = listener;
        this.userList= users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem,listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        final User xUser = userList.get(position);
        holder.rank.setText(String.valueOf(position+1));
        holder.username.setText(xUser.getUsername());
        holder.points.setText(String.valueOf(xUser.getPoints()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
