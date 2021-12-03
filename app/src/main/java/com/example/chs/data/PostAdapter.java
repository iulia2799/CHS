package com.example.chs.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chs.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Post[] localDataSet;
    public PostAdapter(Post[] data){
        this.localDataSet=data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.post,parent,false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final Post posts = localDataSet[position];
        holder.name.setText(posts.getName());
        holder.postloc.setText(posts.getLocation());


    }
    @Override
    public int getItemCount(){
        return localDataSet.length;
    }
   public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView name;
       public TextView postloc;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           this.name = (TextView) itemView.findViewById(R.id.post_name);
           this.postloc = (TextView) itemView.findViewById(R.id.postloc);
           LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.postlay);

       }
   }
}
