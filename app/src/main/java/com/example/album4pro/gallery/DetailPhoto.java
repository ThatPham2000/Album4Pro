package com.example.album4pro.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.album4pro.BuildConfig;
import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.PrivateDatabase;
import com.example.album4pro.R;
import com.example.album4pro.TrashActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailPhoto extends AppCompatActivity {

    private PhotoView img;
    private ImageButton btnShare;
    private ImageButton btnEdit;
    private ImageButton btnHide;
    private ImageButton btnDelete;
    private ImageButton btnMore;
    private Context mContext;

    private String pathImage = "";

    public static String pathPrivate = "";
    public static Boolean pressPrivate = false;
    public static Boolean pressinsidePrivate = false;
    public static Boolean tempcheckToast = false;

    public static String pathDelete = "";
    public static Boolean pressDelete = false;
    public static Boolean pressinsideDelete = false;
    public static Boolean tempcheckToastDelete = false;

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

        setContentView(R.layout.activity_detail_photo);

        mContext = getApplicationContext();

        img = (PhotoView) findViewById(R.id.img_detail);
        pathImage = getIntent().getStringExtra("path");
        Glide.with(this).load(pathImage).into(img);

        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnEdit = (ImageButton) findViewById(R.id.btn_edit);
        btnHide = (ImageButton) findViewById(R.id.btn_hide);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);
        btnMore = (ImageButton) findViewById(R.id.btn_more);


        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathPrivate = pathImage;
                pressPrivate = true;
                pressinsidePrivate = true;
                tempcheckToast = true;
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, "title");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, "Share"));
                } catch (Exception e) {
                    Log.e("ERROR SHARE", e.toString());
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri filePath = Uri.fromFile(new File(pathImage));
                Intent dsPhotoEditorIntent = new Intent(DetailPhoto.this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(filePath);

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PhotoEditorNha");

                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, 200);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathDelete = pathImage;
                pressDelete = true;
                pressinsideDelete = true;
                tempcheckToastDelete = true;
                onBackPressed();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DetailPhoto.this, btnMore);
                popupMenu.inflate(R.menu.menu_image_more);

                Menu menu = popupMenu.getMenu();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_image_detail:
                                // Handle
                                try {
                                    // Get Image time
                                    ExifInterface exifInterface = new ExifInterface(pathImage);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                                    String date = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
                                    if (date == null) {
                                        Date lastModDate = new Date(new File(pathImage).lastModified());
                                        date = "" + simpleDateFormat.format(lastModDate);
                                    }

                                    // Get width and height
                                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 1000);
                                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 1000);

                                    // Show
                                    String detailImage = "Size: " + Math.round(1.0 * new File(pathImage).length() / 1000) + "KB\nWidth: " + width + "px\nHeight: " + height + "px\nDate: " + date + "\nPath: " + pathImage;
//                                    Toast.makeText(DetailPhoto.this, detailImage, Toast.LENGTH_SHORT).show();
                                    showDialog(detailImage);
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            case R.id.menu_image_rotate_left:
                                Glide.with(DetailPhoto.this)
                                        .load(pathImage)
                                        .centerCrop()
                                        .transform(new MyTransformation(mContext, 90))
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .into(img);
                                break;
                            case R.id.menu_image_set_background:
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                                try {
                                    Bitmap bitmap = BitmapFactory.decodeFile(pathImage);
                                    wallpaperManager.setBitmap(bitmap);
                                    Toast.makeText(DetailPhoto.this, "Đã thay đổi hình nền", Toast.LENGTH_SHORT).show();
                                    break;
                                } catch (Exception e) {
                                    Log.e("SET_BACKGROUND", e.toString());
                                    break;
                                }
                            case R.id.menu_image_rename:
//                                File file = new File(pathImage);
//                                Uri uri = Uri.fromFile(file);
//                                String extension = pathImage.substring(pathImage.lastIndexOf(".") + 1);
//                                String newPath = file.getParent() + "/YeuEmNhieu." + extension;
//
//                                File newFile = new File(newPath);
//                                Log.e("NEW PATH", uri.toString());
//                                if (file.renameTo(newFile)) {
//                                    Toast.makeText(DetailPhoto.this, "Đổi tên thành công!", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(DetailPhoto.this, "Đổi tên thất bại!", Toast.LENGTH_SHORT).show();
//                                }
                                break;
                            case R.id.menu_image_add_album:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
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

    protected void showDialog(String message) {
        // Khởi tạo AlertDialog từ đối tượng Builder. Tham số là context.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set Message để thiết lập câu thông báo
        builder.setMessage(message)
                // positiveButton
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Tạo dialog và hiển thị
        builder.create().show();
    }
}