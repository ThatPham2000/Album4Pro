package com.example.album4pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SlideShow extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private List<String> listPhoto;
    private Context context = null;
    private Animation animEnter, animExit, animZoomIn, animZoomOut, animRotateIn;
    SharedPreferences sharedPreferences;

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

        setContentView(R.layout.activity_slide_show);

        context = getApplicationContext();
        // ListPhoto l???y ra t??? trong m??y Android
        listPhoto = ImagesGallery.listPhoto(context);

        // View Flipper s??? d???ng cho chuy???n c???nh
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFliperSlideShow);
        // Hi???u ???ng chuy???n c???nh
        animEnter = AnimationUtils.loadAnimation(context, R.anim.anim_enter);
        animExit = AnimationUtils.loadAnimation(context, R.anim.anim_exit);
        animZoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in_slide);
        animZoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out_slide);
        animRotateIn = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_in);

        // C??c c??i ?????t cho ViewFlipper
        for(int i = 0; i < listPhoto.size(); i++) {
            String photo = listPhoto.get(i);
            ImageView imgPhoto = new ImageView(context);
            Glide.with(context).load(photo).into(imgPhoto);
            //imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imgPhoto);
        }

        Handler handler = new Handler();
        final int[] i = {0};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (i[0] % 3){
                    case 0:
                        viewFlipper.setInAnimation(animEnter);
                        viewFlipper.setOutAnimation(animExit);
                        break;
                    case 1:
                        viewFlipper.setInAnimation(animRotateIn);
                        viewFlipper.setOutAnimation(animZoomOut);
                        break;
                    case 2:
                        viewFlipper.setInAnimation(animZoomIn);
                        viewFlipper.setOutAnimation(animZoomOut);
                        break;
                }
                i[0]++;
                viewFlipper.showNext();
                handler.postDelayed(this, 2500);
            }
        }, 2000);
        //viewFlipper.setFlipInterval(2500); // Sau 0,25 gi??y th?? chuy???n h??nh ???nh
    }
}