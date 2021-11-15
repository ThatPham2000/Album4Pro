package com.example.album4pro.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.album4pro.R;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    ListView mySetting;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    Context context;
    Dialog myThemeDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myThemeDialog = new Dialog(this);

        // DarkMode
        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        darkModeSwitch.setChecked(sharedPreferences.getBoolean("value", false));


        // Check when don't have clicked
        if (darkModeSwitch.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Add OnClick Listener
        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    reset();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    reset();
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////////////

        // Context
        context = this;

        // ListView
        mySetting = findViewById(R.id.mySetting);

        // Data
        arrayList = new ArrayList<String>();
        arrayList.add("Select Theme");
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
                    case "Select Theme":
//                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                            showPopup(view);
//                        }
                        break;
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

    private void reset() {
        finish();

        // Override Animation Of Finish Function
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    public void showPopup(View view) {
        myThemeDialog.setContentView(R.layout.theme_selection);

        // Delete Background
        myThemeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Set Animation
        myThemeDialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);

        myThemeDialog.show();



       /////////////////////////////////////////////////////////////////////////////////////////////
        TextView smokeButton = myThemeDialog.findViewById(R.id.smoke);
        TextView blueButton = myThemeDialog.findViewById(R.id.blue);
        TextView brownButton = myThemeDialog.findViewById(R.id.brown);
        TextView purpleButton = myThemeDialog.findViewById(R.id.purple);
        TextView yellowButton = myThemeDialog.findViewById(R.id.yellow);
        TextView greenButton = myThemeDialog.findViewById(R.id.green);
        TextView orangeButton = myThemeDialog.findViewById(R.id.orange);
        TextView navyButton = myThemeDialog.findViewById(R.id.navy);
        TextView pinkButton = myThemeDialog.findViewById(R.id.pink);

//        // Show Check
//        smokeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
//        // Delete Check
//        smokeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        // Create List Theme Button
        ArrayList<TextView> listThemeButton = new ArrayList<TextView>();
        listThemeButton.add(smokeButton);
        listThemeButton.add(blueButton);
        listThemeButton.add(brownButton);
        listThemeButton.add(purpleButton);
        listThemeButton.add(yellowButton);
        listThemeButton.add(greenButton);
        listThemeButton.add(orangeButton);
        listThemeButton.add(navyButton);
        listThemeButton.add(pinkButton);


        // Set Click Listener For Theme Button
        smokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smokeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != smokeButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != blueButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        brownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brownButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != brownButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purpleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != purpleButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yellowButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != yellowButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != greenButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orangeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != orangeButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        navyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navyButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != navyButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinkButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);

                // Clear Check On Another Buttons
                for (TextView btn:listThemeButton) {
                    if (btn != pinkButton) btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
    }


}