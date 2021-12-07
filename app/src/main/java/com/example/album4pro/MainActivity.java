package com.example.album4pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.album4pro.fragments.MyFragmentAdapter;
import com.example.album4pro.fragments.ZoomOutPageTransformer;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.privates.CreatePasswordActivity;
import com.example.album4pro.privates.EnterPasswordActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 menuViewPager2;
    private BottomNavigationView menuBottomNavigationView;
    public Context libraryContext, privateContext;

    private PrivateDatabase privateDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tường
        // Tạo database Private
        privateDatabase = new PrivateDatabase(this, "private.sqlite", null, 1);
        // tạo bảng
        privateDatabase.QueryData("CREATE TABLE IF NOT EXISTS PrivateData(Id INTEGER PRIMARY KEY AUTOINCREMENT, Path VARCHAR(200))");

        menuViewPager2 = (ViewPager2) findViewById(R.id.view_pager_2);
        menuBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Khởi tạo adapter
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(this);
        menuViewPager2.setAdapter(myFragmentAdapter);

        // Set hiệu ứng chuyển trang Zoom Out Page
        menuViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        // Set sự kiện khi LƯỚT trên trang
        menuViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_library).setChecked(true);
                        break;
                    case 1:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_albums).setChecked(true);
                        break;
                    case 2:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_private).setChecked(true);
                        break;
                    case 3:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 4:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                        break;
                }
            }
        });

        // Set sự kiện khi chọn trong thanh menu
        menuBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_library:
                        menuViewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_albums:
                        menuViewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_private:
                        menuViewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_search:
                        menuViewPager2.setCurrentItem(3);
                        break;
                    case R.id.menu_setting:
                        menuViewPager2.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

    }

    // Tuong
    public ArrayList<String> listPhotoPrivate(Context context){

        ArrayList<String> arrListPrivate = new ArrayList<>();
        // select data
        Cursor dataCursor = privateDatabase.GetData("SELECT * FROM PrivateData");
        while (dataCursor.moveToNext()){
            String path_p = dataCursor.getString(1); // i là cột
            //int id = dataCursor.getInt(0);

            arrListPrivate.add(path_p);
        }
        return arrListPrivate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String pathImage = DetailPhoto.pathPrivate;

        Boolean check = false;
        Cursor checkCursor = privateDatabase.GetData("SELECT * FROM PrivateData");
        while (checkCursor.moveToNext()){
            String path_p = checkCursor.getString(1); // i là cột
            if(pathImage.equals(path_p)){
               check = true;
               break;
            }
        }
        // Chưa tồn tại trong Private
        if(check == false && !pathImage.equals("")){
            // Thêm vào Private
            privateDatabase.QueryData("INSERT INTO PrivateData VALUES(null, '"+pathImage+"')");

            // Xóa trong Library
            // ImagesGallery.listPhoto(this).remove(pathImage);
//            list_private = ImagesGallery.listPhoto(libraryContext);
//            Toast.makeText(this, list_private.size() + "  0", Toast.LENGTH_SHORT).show();
//            list_private.remove(pathImage);
//            Toast.makeText(this, list_private.size() + "  1", Toast.LENGTH_SHORT).show();
//            Configuration.getInstance().getGalleryAdapter().setListPhoto(list_private);
//            Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "Show Camera", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_slideshow:
                // User chose to slideshow Image
                startActivity(new Intent(this, SlideShow.class));
                return true;

            case R.id.action_sort_image:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                if(Configuration.getInstance().getGalleryAdapter() != null){
                    List<String> list = ImagesGallery.listPhoto(libraryContext);
                    Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                    Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
                }
                return true;

            case R.id.action_sort_video:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                if(Configuration.getInstance().getGalleryAdapter() != null){
                    List<String> list = ImagesGallery.listVideo(libraryContext);
                    Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                    Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
                }
                return true;

            case R.id.action_load_url:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(this, "Show load ảnh từ url", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_selection:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(this, "Show selection", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
