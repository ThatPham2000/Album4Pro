package com.example.album4pro.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.albums.AlbumAdapter;
import com.example.album4pro.albums.AlbumItem;
import com.example.album4pro.albums.AlbumPage;
import com.example.album4pro.albums.NewAlbumAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumsFragment extends Fragment {

    // Kh???i t???o c??c tham s???
    private RecyclerView rcvAlbum;
    private AlbumAdapter albumAdapter;
    private Context context = null;
    private List<String> listFolderName;
    private List<String> listImageOnAlbum;
    private String numberImageOnAlbum;
    private String fristImageOnAlbum;

    /*private int firstImageOnAlbum;*/

    private List<AlbumItem> listAlbum =  new ArrayList<>();
    private View mView;
    private MainActivity main;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //choose photo for album
    private List<String> imagePathList;
    private static final int SELECT_PICTURES = 10;
    public static final int RESULT_OK = -1;

    private String filepath;

//    private FloatingActionButton fab;

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
        setHasOptionsMenu(true);

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
                onClickGoToAlbumPage(album);
            }
        });
        albumAdapter.setData(loadAlbums());
        rcvAlbum.setAdapter(albumAdapter);

//        fab = mView.findViewById(R.id.fab_new_album);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickGoToDialog();
//            }
//        });

    }
    private void onClickGoToAlbumPage(AlbumItem album){
        Intent intent = new Intent(context, AlbumPage.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("folder name", album);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    private void onClickGoToDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_new_album);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        EditText edtAlbumName = dialog.findViewById(R.id.edt_album_name);
        Button btnExit = dialog.findViewById(R.id.btn_exit);
        Button btnCreate = dialog.findViewById(R.id.btn_create);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filepath = edtAlbumName.getText().toString().trim();
                ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
                File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
                selectImagesFromGallery();
                for (int i = 0; i < imagePathList.size(); i++){
                    File myInternalFile = new File(directory, imagePathList.get(i));
                    try {
                        myInternalFile.createNewFile();
                        copyFile(new File(imagePathList.get(i)), myInternalFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //===================================================================
    private void selectImagesFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURES);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES && resultCode == RESULT_OK  && data != null) {
            if (data.getClipData() != null) {
                imagePathList = new ArrayList<>();
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    getImageFilePath(imageUri);
                }
            }
            else if (data.getData() != null) {
                Uri imgUri = data.getData();
                getImageFilePath(imgUri);
            }
        }
    }
    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                new String[]{image_id},
                null);
        if (cursor!=null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();
        }
    }
    //=============================================================================
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
        /*listAlbum.add(new AlbumItem(R.drawable.icon_create_album,"T???o album m???i",""));*/

        for (String folderName : listFolderName) {
            listImageOnAlbum = ImagesGallery.listImageOnAlbum(context, folderName);
            numberImageOnAlbum = ImagesGallery.numberImageOnAlbum(listImageOnAlbum);
            fristImageOnAlbum = ImagesGallery.firstImageOnAlbum(listImageOnAlbum);
            listAlbum.add(new AlbumItem(fristImageOnAlbum, folderName, numberImageOnAlbum));
        }

        /*listAlbum.add(new AlbumItem(R.drawable.icon_album,newAlbum.getName(),newAlbum.getNumber()));*/
        return listAlbum;
    }

//-------------------------------------------------------------------------------------------------------
    /*private void selectImagesFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES && resultCode == RESULT_OK && data != null){
            imagePathList = new ArrayList<String>();
            if (data.getClipData() != null){
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++){
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    getImageFilePath(imageUri);
                }
            }
            else if (data.getData() != null){
                Uri imageUri = data.getData();
                getImageFilePath(imageUri);
            }
        }
    }
    public void getImageFilePath(Uri uri){
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                new String[]{image_id},
                null);
        if (cursor != null){
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();
        }
    }*/
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem_camera = menu.findItem(R.id.action_camera);
        menuItem_camera.setVisible(false);

        MenuItem menuItem_slideshow = menu.findItem(R.id.action_slideshow);
        menuItem_slideshow.setVisible(false);

        MenuItem menuItem_sort = menu.findItem(R.id.action_sort);
        menuItem_sort.setVisible(false);

        MenuItem menuItem_loadUrl = menu.findItem(R.id.action_load_url);
        menuItem_loadUrl.setVisible(false);

        MenuItem menuItem_image_search = menu.findItem(R.id.action_search_image_firebase);
        menuItem_image_search.setVisible(false);

        MenuItem menuItem_trash = menu.findItem(R.id.action_load_trash);
        menuItem_trash.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}