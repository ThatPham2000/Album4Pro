package com.example.album4pro.albums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.album4pro.R;
import com.example.album4pro.fragments.AlbumsFragment;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder>{

    private Context context;
    private List<AlbumItem> aListAlbum;
    protected AlbumListener albumListener;

    public AlbumAdapter(Context context, AlbumListener albumListener) {
        this.context = context;
        this.albumListener = albumListener;
    }

    public void setData(List<AlbumItem> list){
        this.aListAlbum = list;
        notifyDataSetChanged(); //load data to AlbumAdapter
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AlbumItem album = aListAlbum.get(position);
        if (album == null) {
            return;
        }
        holder.imgAlbum.setImageResource(album.getResourceId());
        holder.tvNameFolder.setText(album.getName());
        holder.tvNumberImg.setText(album.getNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumListener.onAlbumClick(album);
            }
        });
    }

    // Interface xử lý sự kiện click
    public interface AlbumListener {
        void onAlbumClick(AlbumItem album);
    }

    @Override
    public int getItemCount() {
        if (aListAlbum != null){
            return aListAlbum.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView tvNameFolder;
        private TextView tvNumberImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAlbum = itemView.findViewById(R.id.img_album);
            tvNameFolder = itemView.findViewById(R.id.name_folder);
            tvNumberImg = itemView.findViewById(R.id.number_img);


        }
    }


}
