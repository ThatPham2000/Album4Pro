package com.example.album4pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SlideShow extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private List<String> listPhoto;
    private Context context = null;
    private Animation animEnter, animExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        context = getApplicationContext();
        // ListPhoto lấy ra từ trong máy Android
        listPhoto = ImagesGallery.listPhoto(context);

        // View Flipper sử dụng cho chuyển cảnh
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFliperSlideShow);
        // Hiệu ứng chuyển cảnh
        animEnter = AnimationUtils.loadAnimation(context, R.anim.anim_enter);
        animExit = AnimationUtils.loadAnimation(context, R.anim.anim_exit);


        // Các cài đặt cho ViewFlipper
        for(int i = 0; i < listPhoto.size(); i++) {
            String photo = listPhoto.get(i);
            ImageView imgPhoto = new ImageView(context);
            Glide.with(context).load(photo).into(imgPhoto);
            //imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imgPhoto);
        }
        viewFlipper.setInAnimation(animEnter);
        viewFlipper.setOutAnimation(animExit);
        viewFlipper.setFlipInterval(2500); // Sau 0,25 giây thì chuyển hình ảnh
        viewFlipper.setAutoStart(true);
    }
}