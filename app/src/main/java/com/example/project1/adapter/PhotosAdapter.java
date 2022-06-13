package com.example.project1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.project1.R;
import com.example.project1.model.Albums;
import com.example.project1.model.Photos;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {
    private Context context;
    private List<Photos> photoList;

    private OnPhotoListener mOnPhotoListener;

    public PhotosAdapter(Context context, List<Photos> photoList, OnPhotoListener onPhotoListener){
        this.context = context;
        this.photoList = photoList;
        this.mOnPhotoListener = onPhotoListener;
    }

    public void setAlbumsList(List<Photos> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_row_photo,parent,false);
        return new PhotosAdapter.MyViewHolder(view,mOnPhotoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(this.photoList.get(position).getTitle());


        GlideUrl url = new GlideUrl(this.photoList.get(position).getThumbnailUrl(), new LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build());


        Glide.with(context).
                load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(this.photoList != null){
            return this.photoList.size();
        }
        return 0;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvTitle;
        ImageView imageView;
        OnPhotoListener onPhotoListener;

        public MyViewHolder(View itemview, PhotosAdapter.OnPhotoListener onPhotoListener){
            super(itemview);
            tvTitle = (TextView) itemview.findViewById(R.id.titleView);
            imageView = (ImageView) itemview.findViewById(R.id.imageView);

            this.onPhotoListener = onPhotoListener;
            itemview.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onPhotoListener.onPhotosClick(getAdapterPosition());

        }
    }
    public interface OnPhotoListener{
        void onPhotosClick(int position);
    }

}
