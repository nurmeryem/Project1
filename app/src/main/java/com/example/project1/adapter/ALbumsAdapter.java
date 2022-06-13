package com.example.project1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.model.Albums;

import java.util.List;

public class ALbumsAdapter extends RecyclerView.Adapter<ALbumsAdapter.MyViewHolder>  {
    private Context context;
    private List<Albums> albumsList;

    private OnAlbumListener mOnAlbumListener;

    public ALbumsAdapter(Context context, List<Albums> albumsList, OnAlbumListener onAlbumListener){
        this.context = context;
        this.albumsList = albumsList;
        this.mOnAlbumListener = onAlbumListener;
    }

    public void setAlbumsList(List<Albums> albumsList) {
        this.albumsList = albumsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ALbumsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyle_row,parent,false);
        return new MyViewHolder(view,mOnAlbumListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ALbumsAdapter.MyViewHolder holder, int position) {
        Log.e("TAG", "onBindViewHolder: " + this.albumsList.get(position).getTitle().toString());
        holder.tvTitle.setText(this.albumsList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        if(this.albumsList != null){
            return this.albumsList.size();
        }
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvTitle;
        OnAlbumListener onAlbumListener;

        public MyViewHolder(View itemview,OnAlbumListener onAlbumListener){
            super(itemview);
            tvTitle = (TextView) itemview.findViewById(R.id.titleView);

            this.onAlbumListener = onAlbumListener;
            itemview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.e("TAG", "onClick: " );
            onAlbumListener.onAlbumClick(getAdapterPosition());

        }
    }
    public interface OnAlbumListener{
        void onAlbumClick(int position);
    }


}
