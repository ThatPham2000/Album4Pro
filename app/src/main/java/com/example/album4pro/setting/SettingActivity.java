package com.example.album4pro.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.privates.CreatePasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    ListView mySetting;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    Context context;
    Dialog myThemeDialog;
    HashMap<TextView, Boolean> listThemeButton;
    Switch darkModeSwitch;
    TextView smokeButton;
    TextView blueButton;
    TextView brownButton;
    TextView purpleButton;
    TextView yellowButton;
    TextView greenButton;
    TextView orangeButton;
    TextView navyButton;
    TextView pinkButton;
    SharedPreferences sharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------------------------------------- INITIAL VALUE -----------------------------------------------
        myThemeDialog = new Dialog(this);
        myThemeDialog.setContentView(R.layout.theme_selection);
        listThemeButton = new HashMap<TextView, Boolean>();
        context = this;


        //----------------------------------- CREATE HASHMAP THEME BUTTON ------------------------------------

        smokeButton = myThemeDialog.findViewById(R.id.smoke);
        blueButton = myThemeDialog.findViewById(R.id.blue);
        brownButton = myThemeDialog.findViewById(R.id.brown);
        purpleButton = myThemeDialog.findViewById(R.id.purple);
        yellowButton = myThemeDialog.findViewById(R.id.yellow);
        greenButton = myThemeDialog.findViewById(R.id.green);
        orangeButton = myThemeDialog.findViewById(R.id.orange);
        navyButton = myThemeDialog.findViewById(R.id.navy);
        pinkButton = myThemeDialog.findViewById(R.id.pink);


        listThemeButton.put(smokeButton, sharedPreferences.getBoolean("smoke", false));
        listThemeButton.put(blueButton, sharedPreferences.getBoolean("blue", true));
        listThemeButton.put(brownButton, sharedPreferences.getBoolean("brown", false));
        listThemeButton.put(purpleButton, sharedPreferences.getBoolean("purple", false));
        listThemeButton.put(yellowButton, sharedPreferences.getBoolean("yellow", false));
        listThemeButton.put(greenButton, sharedPreferences.getBoolean("green", false));
        listThemeButton.put(orangeButton, sharedPreferences.getBoolean("orange", false));
        listThemeButton.put(navyButton, sharedPreferences.getBoolean("navy", false));
        listThemeButton.put(pinkButton, sharedPreferences.getBoolean("pink", false));

        //--------------------------------------------- THEME BUTTON ------------------------------------------------
        // Check Button Theme have been saved before
        for (TextView btn:listThemeButton.keySet()) {
            if (listThemeButton.get(btn) == true) {
                if (btn == brownButton || btn == navyButton) btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);
                else btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
            }
        }

        // Smoke Button
        smokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smokeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(smokeButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != smokeButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Blue Button
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(blueButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != blueButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Brown Button
        brownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brownButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);
                listThemeButton.put(brownButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != brownButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Purple Button
        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purpleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(purpleButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != purpleButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Yellow Button
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yellowButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(yellowButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != yellowButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Green Button
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(greenButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != greenButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Orange Button
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orangeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(orangeButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != orangeButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Navy Button
        navyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navyButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);
                listThemeButton.put(navyButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != navyButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });

        // Pink Button
        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinkButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                listThemeButton.put(pinkButton, true);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton.keySet()) {
                    if (btn != pinkButton) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        listThemeButton.put(btn, false);
                    }
                }

                reset();
            }
        });


        //------------------------------------------- DARK MODE -------------------------------------------------

        // DarkMode Switch
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        // If DarkMode On, Set Check On Switch
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.setChecked(true);
        }

        // Add OnClick Listener
        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    reset();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    reset();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //---------------------------------------- SET LANGUAGE ------------------------------------------------
        // Set Language
        if (sharedPreferences.getBoolean("vietnamese", false)) {
            setLocale("vi");
        } else {
            setLocale("en");
        }

        //---------------------------------------------- List View -------------------------------------------------

        // ListView
        mySetting = findViewById(R.id.mySetting);

        // Data
        arrayList = new ArrayList<String>();

        if (sharedPreferences.getBoolean("vietnamese", false)) {
            arrayList.add("Chủ Đề");
            arrayList.add("Về Chúng Tôi");
            arrayList.add("Trợ Giúp");
            arrayList.add("Ngôn Ngữ");
            arrayList.add("Privacy Policy");
        } else {
            arrayList.add("Select Theme");
            arrayList.add("About Us");
            arrayList.add("Help");
            arrayList.add("Language");
            arrayList.add("Privacy Policy");
        }

        // Connect Data to ListView
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayList);
        mySetting.setAdapter(arrayAdapter);

        // Set Onclick For ListView
        mySetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (arrayList.get(i)) {
                    case "Select Theme":
                    case "Chủ Đề":
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                            showPopup(view);
                        }
                        break;
                    case "Privacy Policy":
                        startActivity(new Intent(context, PolicyActivity.class));
                        break;
                    case "About Us":
                    case "Về Chúng Tôi":
                        startActivity(new Intent(context, AboutUsActivity.class));
                        break;
                    case "Help":
                    case "Trợ Giúp":
                        startActivity(new Intent(context, HelpActivity.class));
                        break;
                    case "Language":
                    case "Ngôn Ngữ":
                        startActivity(new Intent(context, LanguageActivity.class));
                        break;
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        // Save HashMap Theme Button
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("smoke", listThemeButton.get(smokeButton));
        editor.putBoolean("blue", listThemeButton.get(blueButton));
        editor.putBoolean("brown", listThemeButton.get(brownButton));
        editor.putBoolean("purple", listThemeButton.get(purpleButton));
        editor.putBoolean("yellow", listThemeButton.get(yellowButton));
        editor.putBoolean("green", listThemeButton.get(greenButton));
        editor.putBoolean("orange", listThemeButton.get(orangeButton));
        editor.putBoolean("navy", listThemeButton.get(navyButton));
        editor.putBoolean("pink", listThemeButton.get(pinkButton));

        // Save State Of DarkMode Button
        editor.putBoolean("darkmode", darkModeSwitch.isChecked());

        editor.apply();
    }

    private void reset() {
        finish();

        // Override Animation Of Finish Function
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    public void showPopup(View view) {
        //---------------------------------------------- SHOW POPUP -------------------------------------------------

        // Delete Background
        myThemeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Set Animation
        myThemeDialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);

        // Show Popup
        myThemeDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setLocale(String language) {
        // Initalize resources
        Resources resources = getResources();
        // Initialize metrics
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // Initialize configuration
        Configuration configuration = resources.getConfiguration();
        // Initialize locale
        configuration.setLocale(new Locale(language));
        // Update configuration
        resources.updateConfiguration(configuration, metrics);
        // Notify configuration
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Set strings from resources
        darkModeSwitch.setText(R.string.darkmode_text);
    }
}