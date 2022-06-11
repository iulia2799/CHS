package com.example.chs.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chs.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<Post> postList;
    private final OnItemListener itemListener;

    public PostAdapter(List<Post> data,OnItemListener listener){
        this.itemListener = listener;
        this.postList=data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.post,parent,false);
        return new ViewHolder(listItem,itemListener);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //final Post posts = localDataSet[position];
        final Post posts = postList.get(position);
        holder.pos.setText(String.valueOf(position+1));
        if(posts.getStatus() !=null){
            if(posts.getStatus().contains("In curs")){
                holder.itemView.setBackgroundResource(R.drawable.post3);
                if(posts.getDatet() !=0){
                    int days = (int) ((System.currentTimeMillis()- posts.getDatet())/ (1000*60*60*24));
                    if(days>30){
                        holder.itemView.setBackgroundResource(R.drawable.post2);
                    }else if (days>15){
                        holder.itemView.setBackgroundResource(R.drawable.post1);
                    }
                }
            } else if(posts.getStatus().contains("Nerezolvat") || posts.getStatus().contains("NOT SOLVED")){
                holder.itemView.setBackgroundResource(R.drawable.post2);
            }
        }
        //change background for status and date
        //holder.itemView.setBackground(R.drawable.);
        holder.description.setText(posts.getName());
        String s = "Voturi : "+String.valueOf(posts.getVoturi());
        holder.votes.setText(s);



    }
    @Override
    public int getItemCount(){
        //return localDataSet.length;
        return postList.size();
    }
   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       public TextView pos;
       public TextView description;
       public TextView votes;
       public OnItemListener onClickListener;
       public ViewHolder(@NonNull View itemView, OnItemListener onClickListener) {
           super(itemView);
           this.pos = (TextView) itemView.findViewById(R.id.user_rank);
           this.description = (TextView) itemView.findViewById(R.id.user_name);
           this.votes = itemView.findViewById(R.id.user_points);
           ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.userlayout);
           this.onClickListener = onClickListener;
           itemView.setOnClickListener((View.OnClickListener) this);

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
