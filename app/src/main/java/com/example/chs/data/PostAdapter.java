package com.example.chs.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.name.setText(posts.getName());
        holder.postloc.setText(posts.getLocation());
        holder.description.setText(posts.getDescription());
        if(posts.getOp() !=null)
         holder.username.setText(posts.getOp().getEmail());



    }
    @Override
    public int getItemCount(){
        //return localDataSet.length;
        return postList.size();
    }
   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       public TextView name;
       public TextView postloc;
       public TextView description;
       public TextView username;
       public OnItemListener onClickListener;
       public ViewHolder(@NonNull View itemView, OnItemListener onClickListener) {
           super(itemView);
           this.name = (TextView) itemView.findViewById(R.id.post_name);
           this.postloc = (TextView) itemView.findViewById(R.id.postloc);
           this.description = (TextView) itemView.findViewById(R.id.postdescription);
           this.username = (TextView) itemView.findViewById(R.id.opname);
           ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.postlay);
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
