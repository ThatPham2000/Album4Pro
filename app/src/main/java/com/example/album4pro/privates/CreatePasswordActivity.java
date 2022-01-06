package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText edtNewPass, edtRePass;
    Button btnConfirm;
    SharedPreferences sharedPreferences;

    public static String pepperHasing = "Khtn@Album4pro";

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

        setContentView(R.layout.activity_create_password);

        SharedPreferences sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);

        edtNewPass = (EditText) findViewById(R.id.editTextNumberPasswordFirst);
        edtRePass = (EditText) findViewById(R.id.editTextNumberPasswordSecond);
        btnConfirm = (Button) findViewById(R.id.buttonConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_pass = edtNewPass.getText().toString().trim();
                String second_pass = edtRePass.getText().toString().trim();

                Intent intent = getIntent();
                Boolean checkout = intent.getBooleanExtra("check_out", false);

                if(first_pass.equals("") || second_pass.equals("")){
                    // there is no password
                    Toast.makeText(CreatePasswordActivity.this, R.string.entered_password_yet, Toast.LENGTH_SHORT).show();
                } else {
                    if(  second_pass.length() < 6 || first_pass.length() < 6){
                        Toast.makeText(CreatePasswordActivity.this, R.string.six_digits, Toast.LENGTH_SHORT).show();
                    } else {
                        if(first_pass.equals(second_pass)){
                            // Save password
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password_tag", CreatePasswordActivity.md5HashandPepper(second_pass + CreatePasswordActivity.pepperHasing));
                            editor.commit();
                            if(checkout == true){
                                // Enter the app
                                finish();
                            } else {
                                // Nhảy tới Activity thiết lập câu hỏi bảo mật
                                intent = new Intent(getApplicationContext(), SecurityQuestionActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // there is no match on the passwords
                            Toast.makeText(CreatePasswordActivity.this, R.string.password_donot_match, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreatePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String md5HashandPepper(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}