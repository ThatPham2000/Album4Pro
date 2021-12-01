package com.example.album4pro.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class MyTransformation extends BitmapTransformation {
    private int rotate = 0;

    public MyTransformation(Context context, int rotate) {
        super();
        this.rotate = rotate;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int exifOrientationDegrees = getExifOrientationDegrees(rotate);
        return TransformationUtils.rotateImageExif(pool, toTransform, exifOrientationDegrees);
    }

    private int getExifOrientationDegrees(int orientation) {
        int exifInt;
        switch (orientation) {
            case 0:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
            case 90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case 180:
                exifInt = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case 270:
                exifInt = ExifInterface.ORIENTATION_ROTATE_270;
                break;
            case -270:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            case -180:
                exifInt = ExifInterface.ORIENTATION_ROTATE_180;
                break;
            case -90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_270;
                break;
            default:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
        }
        return exifInt;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
