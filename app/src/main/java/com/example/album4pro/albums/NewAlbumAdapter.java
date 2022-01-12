package com.example.album4pro.albums;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.album4pro.R;
import com.example.album4pro.gallery.DetailPhoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewAlbumAdapter extends RecyclerView.Adapter<NewAlbumAdapter.NewAlbumViewHolder> {

    private Context mContext;
    ArrayList<String> stringArrayList;

    public NewAlbumAdapter(Context mContext, ArrayList<String> selectedImageList) {
        this.mContext = mContext;
    }

    public void setNewAlbum(ArrayList<String> stringArrayList){
        this.stringArrayList = stringArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent,false);
        return new NewAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewAlbumViewHolder holder, int position) {
        final int mPos = position;
        Glide.with(mContext).
                load(stringArrayList.get(mPos)).
                placeholder(R.color.smoke_theme).
                centerCrop().
                into(holder.imgPhoto);
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(
                        new Intent(mContext, DetailPhoto.class).
                                putExtra("image", stringArrayList.get(mPos)));
            }
        });
    }


    @Override
    public int getItemCount() {
        if (stringArrayList == null){
            return 0;
        } else {
            return stringArrayList.size();
        }

    }

    public static class NewAlbumViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPhoto;
        public NewAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.gallery_item_imgView);
        }
    }
}
