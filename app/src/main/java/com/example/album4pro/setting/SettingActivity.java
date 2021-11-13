package com.example.album4pro.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    ListView mySetting;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // DarkMode
        Switch darkMode = findViewById(R.id.darkMode);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkMode.setChecked(true);
        }

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    reset();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    reset();
                }
            }
        });


        //////////////////////////////////////////////////////////////////////////////////

        // ListView
        mySetting = findViewById(R.id.mySetting);

        // Data
        arrayList = new ArrayList<String>();
        arrayList.add("Policy");
        arrayList.add("About Us");
        arrayList.add("Help");
        arrayList.add("Language");

        // Connect Data to ListView
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        mySetting.setAdapter(arrayAdapter);

        // Context
        context = this;

        // Set Onclick For ListView
        mySetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).equals("Policy")) {
                    startActivity(new Intent(context, PolicyActivity.class));
                } else if (arrayList.get(i).equals("About Us")) {
                    startActivity(new Intent(context, AboutUsActivity.class));
                } else if (arrayList.get(i).equals("Help")) {
                    startActivity(new Intent(context, HelpActivity.class));
                } else if (arrayList.get(i).equals("Language")) {
                    startActivity(new Intent(context, LanguageActivity.class));
                }
            }
        });
    }

 private void reset() {
     startActivity(new Intent(context, SettingActivity.class));
     finish();
 }




}