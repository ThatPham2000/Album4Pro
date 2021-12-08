package com.example.album4pro.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // Khởi tạo các tham số
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private List<String> listPhoto;
    private Context context = null;
    private MainActivity mainActivity;
    private Button btnFilter;
    private TextView tvDate;
    private Button btnSelectDate;

    private int lastSelectedYear = 2021;
    private int lastSelectedMonth = 11;
    private int lastSelectedDayOfMonth = 8;
    private Date date;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        //return inflater.inflate(R.layout.fragment_search, container, false);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_search);

        tvDate = view.findViewById(R.id.tv_date);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectDate();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        return view;
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Get search date
        String searchDay;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        if (date != null) {
            searchDay = "" + simpleDateFormat.format(date);
        } else {
            searchDay = "";
        }

        listPhoto = ImagesGallery.listPhotoFilter(context, searchDay);

        galleryAdapter = new GalleryAdapter(context, listPhoto, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // TODO ST
                Intent intent = new Intent(context, DetailPhoto.class);
                intent.putExtra("path", path);
                context.startActivity(intent);
            }
        });

        recyclerView.setAdapter(galleryAdapter);
        Configuration.getInstance().setGalleryAdapter(this.galleryAdapter);
    }

    private void buttonSelectDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                tvDate.setText( year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;

                Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                date = myCalendar.getTime();
            }
        };

        DatePickerDialog datePickerDialog = null;


        // Create DatePickerDialog:
        datePickerDialog = new DatePickerDialog(this.context,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

        datePickerDialog.show();
    }
}