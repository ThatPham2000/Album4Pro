package com.example.album4pro.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
    private TextView tvStartDate;
    private TextView tvEndDate;
    //private Button btnSelectDate;

    Calendar calendar = Calendar.getInstance();

    private int lastSelectedYear_Start =  calendar.get(Calendar.YEAR);;
    private int lastSelectedMonth_Start = calendar.get(Calendar.MONTH);
    private int lastSelectedDayOfMonth_Start = calendar.get(Calendar.DAY_OF_MONTH);

    private int lastSelectedYear_End =  calendar.get(Calendar.YEAR);;
    private int lastSelectedMonth_End = calendar.get(Calendar.MONTH);
    private int lastSelectedDayOfMonth_End = calendar.get(Calendar.DAY_OF_MONTH);

    private Date startDate, endDate;

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
        setHasOptionsMenu(true);

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

        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvEndDate = view.findViewById(R.id.tv_end_date);
//        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnFilter = view.findViewById(R.id.btn_filter);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartDate();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEndDate();
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
        String startDay, endDay;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        if (startDate != null && endDate!= null) {
            startDay = "" + simpleDateFormat.format(startDate);
            endDay = "" + simpleDateFormat.format(endDate);

            listPhoto = ImagesGallery.listPhotoFilter(context, startDay, endDay);
        } else {
            startDay = "";
            endDay = "";
        }

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

    private void selectStartDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                tvStartDate.setText( year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                lastSelectedYear_Start = year;
                lastSelectedMonth_Start = monthOfYear;
                lastSelectedDayOfMonth_Start = dayOfMonth;

                Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                startDate = myCalendar.getTime();
            }
        };

        DatePickerDialog datePickerDialog = null;


        // Create DatePickerDialog:
        datePickerDialog = new DatePickerDialog(this.context,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, lastSelectedYear_Start, lastSelectedMonth_Start, lastSelectedDayOfMonth_Start);

        datePickerDialog.show();
    }

    private void selectEndDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                tvEndDate.setText( year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                lastSelectedYear_End = year;
                lastSelectedMonth_End = monthOfYear;
                lastSelectedDayOfMonth_End = dayOfMonth;

                Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                endDate = myCalendar.getTime();
            }
        };

        DatePickerDialog datePickerDialog = null;


        // Create DatePickerDialog:
        datePickerDialog = new DatePickerDialog(this.context,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, lastSelectedYear_End, lastSelectedMonth_End, lastSelectedDayOfMonth_End);

        datePickerDialog.show();
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

        MenuItem menuItem_trash = menu.findItem(R.id.action_load_trash);
        menuItem_trash.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}