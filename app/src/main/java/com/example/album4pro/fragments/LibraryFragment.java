package com.example.album4pro.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.gallery.VideoViewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class LibraryFragment extends Fragment implements View.OnClickListener {

    // Khởi tạo các tham số
    private RecyclerView recyclerView;
    private FloatingActionButton btnScrollUp;
    private GalleryAdapter galleryAdapter;
    private List<String> listPhoto;
    private Context context = null;
    private MainActivity mainActivity;
    GridLayoutManager gridLayoutManager;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
        try {
            context = getActivity(); // use this reference to invoke main callbacks
            mainActivity = (MainActivity) getActivity();
            mainActivity.libraryContext = this.context;
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_gallery);
        btnScrollUp = view.findViewById(R.id.btnScrollUp);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    btnScrollUp.show();
                } else {
                    btnScrollUp.hide();
                }
            }
        });

        btnScrollUp.setOnClickListener(this);

        requestPermission();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Ẩn Photo khi đưa vào Private
        if(DetailPhoto.pressPrivate == true && DetailPhoto.tempcheckToast == true){
            Toast.makeText(context, R.string.hide_image, Toast.LENGTH_SHORT).show();
            DetailPhoto.tempcheckToast = false;
        }
        if(DetailPhoto.pressPrivate == true){
            loadImages();
            DetailPhoto.pressPrivate = false;
        }
    }

    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // Khi người dùng cho phép quyền
                // load gallery
                loadImages();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Nếu bạn từ chối quyền, bạn không thể sử dụng dịch vụ này\n\nVui lòng bật quyền tại [Setting]> [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }


    private void loadImages(){
        recyclerView.setHasFixedSize(true);

        // Get Column Selection Data
        sharedPreferences = this.getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        gridLayoutManager = new GridLayoutManager(context, sharedPreferences.getInt("column", 3));


        recyclerView.setLayoutManager(gridLayoutManager);
        //listPhoto = ImagesGallery.listPhoto(context); // code cũ
        listPhoto = mainActivity.minusPrivatePhoto(mainActivity.listPhotoPrivate(context)); // Chỉnh sửa để ẩn Private trong Library (Tuong)

        galleryAdapter = new GalleryAdapter(context, listPhoto, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // TODO ST
                String[] imageExtensions = {"jpg", "png", "gif", "jpeg", "tiff", "webp"};
                boolean isImage = false;
                String extension = path.substring(path.lastIndexOf(".") + 1);

                for (int i = 0; i < imageExtensions.length; i++){
                    if(extension.equalsIgnoreCase(imageExtensions[i])){
                        isImage = true;
                        break;
                    }
                }

                if (!isImage){
                    Intent intent = new Intent(context, VideoViewActivity.class);
                    intent.putExtra("path", path);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, DetailPhoto.class);
                    intent.putExtra("path", path);
                    context.startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(galleryAdapter);
        Configuration.getInstance().setGalleryAdapter(this.galleryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScrollUp:
                scrollToItem(0);
                break;
        }
    }

    private void scrollToItem(int index) {
        if (gridLayoutManager == null) return;

        gridLayoutManager.scrollToPositionWithOffset(index, 0);
    }
}