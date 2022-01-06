package com.example.album4pro.albums;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.album4pro.R;

import java.io.IOException;
import java.util.List;

public class NewAlbumAdapter extends RecyclerView.Adapter<NewAlbumAdapter.NewAlbumViewHolder> {

    private Context mContext;
    private List<Uri> mListPhotos;

    public NewAlbumAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setNewAlbum(List<Uri> list){
        this.mListPhotos = list;
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
        Uri uri = mListPhotos.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            holder.imgPhoto.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhotos == null){
            return 0;
        } else {
            return mListPhotos.size();
        }

    }

    public class NewAlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        public NewAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.gallery_item_imgView);
        }
    }
}
