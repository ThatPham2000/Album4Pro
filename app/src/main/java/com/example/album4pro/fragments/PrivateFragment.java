package com.example.album4pro.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.PrivateDatabase;
import com.example.album4pro.R;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.gallery.VideoViewActivity;
import com.example.album4pro.privates.CreatePasswordActivity;
import com.example.album4pro.privates.EnterPasswordActivity;
import com.example.album4pro.privates.SecurityQuestionActivity;

import java.util.ArrayList;
import java.util.List;

public class PrivateFragment extends Fragment {

    // Khởi tạo các tham số
    private boolean PASSWORD_ENTERED = false;

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private List<String> listPhoto;

    private Context context = null;
    private MainActivity mainActivity;

    String password_Prefs, answer_Prefs;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrivateFragment newInstance(String param1, String param2) {
        PrivateFragment fragment = new PrivateFragment();
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
            mainActivity = (MainActivity) getActivity(); // Tham chiếu tới MainActivity
            mainActivity.privateContext = this.context;
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_private, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_private);

        // Load hình ảnh từ Database
        LoadImage();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Chưa nhập mật khẩu --> điều hướng màn hình
        if (PASSWORD_ENTERED == false){
            navigatingPrivateScreens();
        }

        // Load lại Private khi có sự thay đổi
        if(DetailPhoto.pressinsidePrivate == true && DetailPhoto.tempcheckToast == true){
            Toast.makeText(context, R.string.show_image_again, Toast.LENGTH_SHORT).show();
            DetailPhoto.tempcheckToast = false;
        }
        if(DetailPhoto.pressinsidePrivate == true){
            LoadImage();
            DetailPhoto.pressinsidePrivate = false;
        }
    }

    private void LoadImage() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        listPhoto = mainActivity.listPhotoPrivate(context);

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

    private void navigatingPrivateScreens(){
        sharedPreferences = getActivity().getSharedPreferences("PASSPREF", Context.MODE_PRIVATE);
        password_Prefs = sharedPreferences.getString("password_tag", "");
        answer_Prefs = sharedPreferences.getString("answer_tag", "");
        if(answer_Prefs.equals("") && !password_Prefs.equals("")){
            // Đã có mật khẩu nhưng chưa chọn câu hỏi bảo mật
            Intent intent = new Intent(getActivity(), SecurityQuestionActivity.class);
            startActivity(intent);
        } else {
            if(password_Prefs.equals("")){
                // there is no password
                Intent intent = new Intent(getActivity(), CreatePasswordActivity.class);
                startActivity(intent);
            } else {
                // there is a password
                if(EnterPasswordActivity.enterPassword == false){
                    Intent intent = new Intent(getActivity(), EnterPasswordActivity.class);
                    startActivity(intent);
                }
            }
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