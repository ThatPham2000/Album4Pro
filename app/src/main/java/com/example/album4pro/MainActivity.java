package com.example.album4pro;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.database.Cursor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.album4pro.fragments.MyFragmentAdapter;
import com.example.album4pro.fragments.ZoomOutPageTransformer;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.setting.PolicyActivity;
import com.example.album4pro.setting.SettingActivity;
import com.example.album4pro.privates.CreatePasswordActivity;
import com.example.album4pro.privates.EnterPasswordActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Timestamp;
import java.util.ArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public ViewPager2 menuViewPager2;
    private BottomNavigationView menuBottomNavigationView;
    public Context libraryContext, privateContext;
    public MyFragmentAdapter myFragmentAdapter;

    private PrivateDatabase privateDatabase;
    SharedPreferences sharedPreferences;

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;

    // Name generator
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Theme Before SetContentView, Default Is Light Theme
        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

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

        // Tạo database Private (Tuong)
        privateDatabase = new PrivateDatabase(this, "private.sqlite", null, 1);
        privateDatabase.QueryData("CREATE TABLE IF NOT EXISTS PrivateData(Id INTEGER PRIMARY KEY AUTOINCREMENT, Path VARCHAR(200))");

        menuViewPager2 = (ViewPager2) findViewById(R.id.view_pager_2);
        menuBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Khởi tạo adapter
        myFragmentAdapter = new MyFragmentAdapter(this);
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
            case R.id.action_camera_photo:
                // Hiển thị camera - photo
                captureImage();
                return true;

            case R.id.action_camera_video:
                //Hiển thị camera - video
                askPermissionAndCaptureVideo();
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

                myBuilder.setIcon(R.drawable.ic_app)
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


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    private void askPermissionAndCaptureVideo() {

        // With Android Level >= 23, you have to ask the user
        // for permission to read/write data on the device.
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have read/write permission
            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_ID_READ_WRITE_PERMISSION
                );
                return;
            }
        }
        this.captureVideo();
    }

    private void captureVideo() {
        try {
            // Create an implicit intent, for video capture.
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            // The external storage directory.
            File dir = Environment.getExternalStorageDirectory();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // file:///storage/emulated/0/video.mp4
            String savePath = dir.getAbsolutePath() + "/DCIM/Album4Pro/" + randomIdentifier();
            File videoFile = new File(savePath);
            Uri videoUri = Uri.fromFile(videoFile);

            // Specify where to save video files.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // ================================================================================================
            // To Fix Error (**)
            // ================================================================================================

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            // ================================================================================================
            // You may get an Error (**) If your app targets API 24+
            // "android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through.."
            //  Explanation: https://stackoverflow.com/questions/38200282
            // ================================================================================================

            // Start camera and wait for the results.
            this.startActivityForResult(intent, REQUEST_ID_VIDEO_CAPTURE); // (**)

        } catch(Exception e)  {
            Toast.makeText(this, "Error capture video: " +e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (read/write).
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.captureVideo();

                }
                // Cancelled or denied.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");

                //Lưu ảnh
                MediaStore.Images.Media.insertImage(getContentResolver(), bp, String.valueOf(System.currentTimeMillis()),"");

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
            if (Configuration.getInstance().getGalleryAdapter() != null){
                List<String> list = ImagesGallery.listPhoto(libraryContext);
                Configuration.getInstance().setVideo(false);
                Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_ID_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                Log.i("MyLog", "Video saved to: " + videoUri);
                Toast.makeText(this, "Video saved to:\n" +
                        videoUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed",
                        Toast.LENGTH_LONG).show();
            }
            if(Configuration.getInstance().getGalleryAdapter() != null){
                List<String> list = ImagesGallery.listVideo(libraryContext);
                Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
                Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
            }
        } else if (requestCode == 200) {
            List<String> list = ImagesGallery.listPhoto(this);
            Configuration.getInstance().getGalleryAdapter().setListPhoto(list);
            Configuration.getInstance().getGalleryAdapter().notifyDataSetChanged();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        insertAndRemovePrivate();
    }

    // Trả ra listPhotoPrivate đã lưu trong Database (Tuong)
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

    // Đưa/lấy hình ảnh/video vào/ra thư mục Private (Tuong)
    private void insertAndRemovePrivate(){
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

        } else {
            // Đã tồn tại trong private --> đưa ra ngoài Library
            privateDatabase.QueryData("DELETE FROM PrivateData WHERE Path = '"+ pathImage +"'");
        }
    }

    // list photo đã trừ đi các photo trong Private (Tuong)
    public ArrayList<String> minusPrivatePhoto(List<String> plist){
        List<String> master_list = ImagesGallery.listPhoto(libraryContext);
        ArrayList<String> list_result = new ArrayList<>();

        boolean check = false;
        for(int i = 0; i < master_list.size(); i++){
            for(int j = 0; j < plist.size(); j++){
                if(master_list.get(i).equals(plist.get(j))){
                    check = true; // có phần tử giống nhau
                }
            }
            if(check == false){
                list_result.add(master_list.get(i));
            }
            check = false;
        }
        return list_result;
    }
}
