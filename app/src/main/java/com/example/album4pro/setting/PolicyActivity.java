package com.example.album4pro.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

public class PolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}