package com.example.album4pro.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

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
//        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);
//
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.Theme_Dark);
//        } else {
//            setTheme(R.style.Theme_Light);
//        }
//
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            darkModeSwitch.setChecked(true);
//        }
//
//        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    reset();
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    reset();
//                }
//            }
//        });

        //////////////////////////////////////////////////////////////////////////////////
        // Test
        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        darkModeSwitch.setChecked(sharedPreferences.getBoolean("value", false));


        // Check when don't have clicked
        if (darkModeSwitch.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Add OnCheck Listener
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    finish();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    finish();
                }

                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////

        // Context
        context = this;

        // ListView
        mySetting = findViewById(R.id.mySetting);

        // Data
        arrayList = new ArrayList<String>();
        arrayList.add("Policy");
        arrayList.add("About Us");
        arrayList.add("Help");
        arrayList.add("Language");

        // Connect Data to ListView
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayList);
        mySetting.setAdapter(arrayAdapter);

        // Set Onclick For ListView
        mySetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (arrayList.get(i)) {
                    case "Policy":
                        startActivity(new Intent(context, PolicyActivity.class));
                        break;
                    case "About Us":
                        startActivity(new Intent(context, AboutUsActivity.class));
                        break;
                    case "Help":
                        startActivity(new Intent(context, HelpActivity.class));
                        break;
                    case "Language":
                        startActivity(new Intent(context, LanguageActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }





}