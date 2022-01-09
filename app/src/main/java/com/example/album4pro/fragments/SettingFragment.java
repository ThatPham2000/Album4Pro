package com.example.album4pro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

import com.example.album4pro.setting.LanguageActivity;
import com.example.album4pro.setting.SettingActivity;
import com.example.album4pro.setting.PolicyActivity;
import com.example.album4pro.setting.AboutUsActivity;
import com.example.album4pro.setting.HelpActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private MainActivity mainActivity;
    Context context;

    ListView mySetting;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
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
    int columnNumber;
    int columnIndex;
    int viewNumber;
    int viewIndex;
    String[] columnNumberTitleEn = {"2 Columns", "3 Columns", "4 Columns"};
    String[] columnNumberTitleVi = {"2 Cột", "3 Cột", "4 Cột"};
    String[] viewAsTitleEn = {"Top", "Bottom"};
    String[] viewAsTitleVi = {"Từ trên xuống", "Đảo ngược"};

    LayoutInflater myInflater;
    ViewGroup myContainer;
    Bundle mySavedInstanceState;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            context = getActivity(); // use this reference to invoke main callbacks
            mainActivity = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myInflater = inflater;
        myContainer = container;
        mySavedInstanceState = savedInstanceState;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // ListView
        mySetting = view.findViewById(R.id.mySetting);

        // Set Theme Before SetContentView, Default Is Light Theme
        sharedPreferences = context.getSharedPreferences("save", context.MODE_PRIVATE);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            if (sharedPreferences.getBoolean("smoke", false)) context.setTheme(R.style.SmokeTheme);
            if (sharedPreferences.getBoolean("blue", true)) context.setTheme(R.style.Theme_Album4Pro);
            if (sharedPreferences.getBoolean("brown", false)) context.setTheme(R.style.BrownTheme);
            if (sharedPreferences.getBoolean("purple", false)) context.setTheme(R.style.PurpleTheme);
            if (sharedPreferences.getBoolean("yellow", false)) context.setTheme(R.style.YellowTheme);
            if (sharedPreferences.getBoolean("green", false)) context.setTheme(R.style.GreenTheme);
            if (sharedPreferences.getBoolean("orange", false)) context.setTheme(R.style.OrangeTheme);
            if (sharedPreferences.getBoolean("navy", false)) context.setTheme(R.style.NavyTheme);
            if (sharedPreferences.getBoolean("pink", false)) context.setTheme(R.style.PinkTheme);
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------------------------------------- INITIAL VALUE -----------------------------------------------
        myThemeDialog = new Dialog(context);
        myThemeDialog.setContentView(R.layout.theme_selection);

        listThemeButton = new HashMap<TextView, Boolean>();
//        context = this;

        //----------------------------------- CREATE HASHMAP THEME BUTTON --------------------------------------

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
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

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
//                    reset();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    reset();
                }
            }
        });

        // todo -- ONCREATE TRONG ACTIVITY

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

        // ListView Screen
//        startActivity(new Intent(context, SettingActivity.class));

        //---------------------------------------- SET LANGUAGE ------------------------------------------------
        // Set Language
        if (sharedPreferences.getBoolean("vietnamese", false)) {
            setLocale("vi");
        } else {
            setLocale("en");
        }

        //--------------------------------------- Prevent Reset Value ------------------------------------------------
        columnIndex = sharedPreferences.getInt("columnindex", 1);
        columnNumber = sharedPreferences.getInt("column", 3);

        viewIndex = sharedPreferences.getInt("viewindex", 0);
        viewNumber = sharedPreferences.getInt("view", 0);

        //---------------------------------------------- List View -------------------------------------------------

//        // ListView
//        mySetting = view.findViewById(R.id.mySetting);

        // Data
        arrayList = new ArrayList<String>();

        if (sharedPreferences.getBoolean("vietnamese", false)) {
            arrayList.add("Chủ Đề");
            arrayList.add("Xem");
            arrayList.add("Hiển Thị Hình Ảnh Theo Số Cột");
            arrayList.add("Về Chúng Tôi");
            arrayList.add("Trợ Giúp");
            arrayList.add("Ngôn Ngữ");
            arrayList.add("Privacy Policy");
        } else {
            arrayList.add("Select Theme");
            arrayList.add("View As");
            arrayList.add("Pictures Displayed Columns");
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
                            showPopup("Theme");
                        }
                        break;
                    case "Pictures Displayed Columns":
                        showOptionsDialog(columnNumberTitleEn);
                        break;
                    case "Hiển Thị Hình Ảnh Theo Số Cột":
                        showOptionsDialog(columnNumberTitleVi);
                        break;
                    case "View As":
                        showOptionsDialog2(viewAsTitleEn);
                        break;
                    case "Xem":
                        showOptionsDialog2(viewAsTitleVi);
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

    private void showOptionsDialog(String[] columnNumberTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_column_title);

        columnIndex = sharedPreferences.getInt("columnindex", 1);
        int columnChecked = columnIndex;

        builder.setSingleChoiceItems(columnNumberTitle, columnChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (columnNumberTitle[which] == "2 Columns" || columnNumberTitle[which] == "2 Cột") {
                    columnIndex = which;
                    columnNumber = 2;
                }
                if (columnNumberTitle[which] == "3 Columns" || columnNumberTitle[which] == "3 Cột") {
                    columnIndex = which;
                    columnNumber = 3;
                }
                if (columnNumberTitle[which] == "4 Columns" || columnNumberTitle[which] == "4 Cột") {
                    columnIndex = which;
                    columnNumber = 4;
                }
            }
        });
        builder.show();
    }

    private void showOptionsDialog2(String[] selectViewTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_view_title);

        viewIndex = sharedPreferences.getInt("viewindex", 0);
        int viewChecked = viewIndex;

        builder.setSingleChoiceItems(selectViewTitle, viewChecked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectViewTitle[which] == "Top" || selectViewTitle[which] == "Từ trên xuống") {
                    viewIndex = which;
                    viewNumber = 0;
                }
                if (selectViewTitle[which] == "Bottom" || selectViewTitle[which] == "Đảo ngược") {
                    viewIndex = which;
                    viewNumber = 1;
                }
            }
        });
        builder.show();
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

        // Save Column Number Selection
        editor.putInt("columnindex", columnIndex);
        editor.putInt("column", columnNumber);

        // Save View Selection
        editor.putInt("viewindex", viewIndex);
        editor.putInt("view", viewNumber);

        editor.apply();
    }

    private void reset() {
        startActivity(mainActivity.getIntent());
        mainActivity.overridePendingTransition(R.anim.default_status, R.anim.default_status);
        mainActivity.finish();
    }

    public void showPopup(String selector) {
        //---------------------------------------------- SHOW POPUP -------------------------------------------------
        // Delete Background
        myThemeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Set Animation
        myThemeDialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);

        // Show Popup
        myThemeDialog.show();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem_camera = menu.findItem(R.id.action_camera);
        menuItem_camera.setVisible(false);

        MenuItem menuItem_slideshow = menu.findItem(R.id.action_slideshow);
        menuItem_slideshow.setVisible(false);

        MenuItem menuItem_sort = menu.findItem(R.id.action_sort);
        menuItem_sort.setVisible(false);

        MenuItem menuItem_loadUrl = menu.findItem(R.id.action_load_url);
        menuItem_loadUrl.setVisible(false);

        MenuItem menuItem_image_search = menu.findItem(R.id.action_search_image_firebase);
        menuItem_image_search.setVisible(false);

        MenuItem menuItem_trash = menu.findItem(R.id.action_load_trash);
        menuItem_trash.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }
}