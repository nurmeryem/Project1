package com.example.project1.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.activities.PostsActivity;
import com.example.project1.model.Comments;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    public List<Comments> commentsOfPostsList;

    Comments comments;

    public CommentsAdapter(List<Comments> commentsOfPostsList) {
        this.commentsOfPostsList = commentsOfPostsList;
    }


    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comments, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvTitle.setText(commentsOfPostsList.get(position).getName());
        holder.tvBody.setText(commentsOfPostsList.get(position).getBody());
        comments = new Comments();
    }

    @Override
    public int getItemCount() {
        return commentsOfPostsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleComments);
            tvBody = itemView.findViewById(R.id.tvBodyComments);
        }
    }

}
