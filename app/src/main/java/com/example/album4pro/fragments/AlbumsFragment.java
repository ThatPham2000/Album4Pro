package com.example.album4pro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.albums.AlbumAdapter;
import com.example.album4pro.albums.AlbumItem;
import com.example.album4pro.albums.AlbumPage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumsFragment extends Fragment {

    // Khởi tạo các tham số
    private RecyclerView rcvAlbum;
    private AlbumAdapter albumAdapter;
    private Context context = null;
    private List<String> listFolderName;
    private List<String> listImageOnAlbum;
    private String numberImageOnAlbum;
    /*private int firstImageOnAlbum;*/

    private List<AlbumItem> listAlbum =  new ArrayList<>();
    private View mView;
    private MainActivity main;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AlbumsFragment() {
        // Required empty public constructor
    }
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if (intent != null){
                            AlbumItem newAlbum = (AlbumItem) intent.getSerializableExtra("new album");
                            listAlbum.set(listAlbum.size(),newAlbum);
                        }
                    }
                }
            }
    );
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumsFragment newInstance(String param1, String param2) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try{
            context = getActivity();
            main = (MainActivity) getActivity();
        }
        catch(IllegalStateException e){
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_albums, container, false);

        initUI();

        return mView;
    }
    private void initUI(){
        rcvAlbum = mView.findViewById(R.id.rcv_album);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rcvAlbum.setLayoutManager(linearLayoutManager);

        listFolderName = ImagesGallery.listFolderName(context);


        /*listAlbum = AlbumPage.listPhotoAndVideo(context);*/
        albumAdapter = new AlbumAdapter(context, new AlbumAdapter.AlbumListener() {
            @Override
            public void onAlbumClick(AlbumItem album) {
                if (album.getName().equals("Tạo album mới")){
                    onClickGoToNewAlbum(album);
                    listAlbum.add(new AlbumItem(R.drawable.icon_album,album.getName(),album.getNumber()));
                } else {
                    onClickGoToAlbumPage(album);
                }
            }
        });
        albumAdapter.setData(loadAlbums());
        rcvAlbum.setAdapter(albumAdapter);

    }
    private void onClickGoToAlbumPage(AlbumItem album){
        Intent intent = new Intent(context, AlbumPage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("folder name", album);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    private void onClickGoToNewAlbum(AlbumItem album){
        /*Intent intent = new Intent(context, AlbumPage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("new album", album);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);*/
        Intent intent = new Intent(context, AlbumPage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("folder name", album);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private List<AlbumItem> loadAlbums(){
        listAlbum.add(new AlbumItem(R.drawable.icon_create_album,"Tạo album mới",""));
        /*AlbumItem newAlbum = (AlbumItem) getArguments().get("new album");*/
        for (String folderName : listFolderName) {
            listImageOnAlbum = ImagesGallery.listImageOnAlbum(context, folderName);
            numberImageOnAlbum = ImagesGallery.numberImageOnAlbum(listImageOnAlbum);
            listAlbum.add(new AlbumItem(R.drawable.icon_album, folderName, numberImageOnAlbum));
        }

        /*listAlbum.add(new AlbumItem(R.drawable.icon_album,newAlbum.getName(),newAlbum.getNumber()));*/
        return listAlbum;
    }
}