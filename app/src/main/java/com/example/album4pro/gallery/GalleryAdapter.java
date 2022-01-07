package com.example.album4pro.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.album4pro.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder>{

    private Context context;
    private List<String> listPhoto;
    protected PhotoListener photoListener;

    public GalleryAdapter(Context context, List<String> listPhoto, PhotoListener photoListener) {
        this.context = context;
        this.listPhoto = listPhoto;
        this.photoListener = photoListener;
    }

    public void setListPhoto(List<String> listPhoto) {
        this.listPhoto = listPhoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.gallery_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Handle
//        Uri uri = listPhoto.get(position);
//        if (uri == null){
//            return;
//        }
//
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//            if(bitmap != null){
//                holder.imgPhoto.setImageBitmap(bitmap);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String photo = listPhoto.get(position);
        Glide.with(context).load(photo).into(holder.imgPhoto);

        String[] imageExtensions = {"jpg", "png", "gif", "jpeg", "tiff", "webp"};
        boolean isImage = false;
        String extension = photo.substring(photo.lastIndexOf(".") + 1);

        for (int i = 0; i < imageExtensions.length; i++){
            if(extension.equalsIgnoreCase(imageExtensions[i])){
                isImage = true;
                break;
            }
        }

        if (!isImage){
            holder.playVideoIV.setVisibility(View.VISIBLE);
        } else {
            holder.playVideoIV.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoListener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listPhoto != null) {
            return listPhoto.size();
        }
        return 0;
    }


    // Class ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPhoto;
        private ImageView playVideoIV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.gallery_item_imgView);
            playVideoIV = (ImageView) itemView.findViewById(R.id.ic_play_video);
        }
    }

    // Interface xử lý sự kiện click
    public interface PhotoListener {
        void onPhotoClick(String path);
    }
}
