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
    private Post[] localDataSet;
    private List<Post> postList;
    private OnItemListener itemListener;
    public PostAdapter(Post[] data,OnItemListener listener){
        this.itemListener = listener;
        this.localDataSet=data;
    }
    public PostAdapter(List<Post> data,OnItemListener listener){
        this.itemListener = listener;
        this.postList=data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.post,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem,itemListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //final Post posts = localDataSet[position];
        final Post posts = postList.get(position);
        holder.pos.setText(String.valueOf(position+1));
        holder.description.setText(posts.getName());
        holder.votes.setText(String.valueOf(posts.getVoturi()));



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
