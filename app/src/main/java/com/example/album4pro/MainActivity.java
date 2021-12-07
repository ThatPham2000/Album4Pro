package com.example.album4pro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.album4pro.fragments.MyFragmentAdapter;
import com.example.album4pro.fragments.ZoomOutPageTransformer;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.setting.PolicyActivity;
import com.example.album4pro.setting.SettingActivity;
import com.example.album4pro.privates.CreatePasswordActivity;
import com.example.album4pro.privates.EnterPasswordActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 menuViewPager2;
    private BottomNavigationView menuBottomNavigationView;
    public Context libraryContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Theme Before SetContentView, Default Is Light Theme
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            if (sharedPreferences.getBoolean("smoke", false)) setTheme(R.style.SmokeTheme);
            if (sharedPreferences.getBoolean("blue", true)) setTheme(R.style.Theme_Album4Pro);
            if (sharedPreferences.getBoolean("brown", false)) setTheme(R.style.BrownTheme);
            if (sharedPreferences.getBoolean("purple", false)) setTheme(R.style.PurpleTheme);
            if (sharedPreferences.getBoolean("yellow", false)) setTheme(R.style.YellowTheme);
            if (sharedPreferences.getBoolean("green", false)) setTheme(R.style.GreenTheme);
            if (sharedPreferences.getBoolean("orange", false)) setTheme(R.style.OrangeTheme);
            if (sharedPreferences.getBoolean("navy", false)) setTheme(R.style.NavyTheme);
            if (sharedPreferences.getBoolean("pink", false)) setTheme(R.style.PinkTheme);
        }

        setContentView(R.layout.activity_main);

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
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(this, "Show slide show", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_sort_image:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                if(Configuration.getInstance().getGalleryAdapter() != null){
                    List<String> list = ImagesGallery.listPhoto(libraryContext);
                    Configuration.getInstance().setVideo(false);
                    Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                    Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
                }
                return true;

            case R.id.action_sort_video:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                if(Configuration.getInstance().getGalleryAdapter() != null){
                    List<String> list = ImagesGallery.listVideo(libraryContext);
                    Configuration.getInstance().setVideo(true);
                    Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                    Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
                }
                return true;

            case R.id.action_load_url:
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(layoutParams);

                myBuilder.setIcon(R.drawable.ic_launcher_foreground)
                        .setTitle("URL Hình ảnh")
                        .setMessage("Nhập URL của hình ảnh\n")
                        .setView(input)
                        .setPositiveButton("Close", null)
                        .setNegativeButton("More", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri filePath = Uri.parse(input.getText().toString());
                                Intent dsPhotoEditorIntent = new Intent(MainActivity.this, DsPhotoEditorActivity.class);
                                dsPhotoEditorIntent.setData(filePath);

                                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PhotoEditorNha");

                                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                                startActivityForResult(dsPhotoEditorIntent, 200);
                            }
                        }).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            List<String> list = ImagesGallery.listPhoto(this);
            Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
            Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
        }
    }
}
