package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.album4pro.R;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText edtOldPass, edtNewPass, edtReNewPass;
    Button btnCofirmReset, btnCancleReset;
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

        setContentView(R.layout.activity_reset_password);

        SharedPreferences sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Password have been saved before
        String password_Prefs = sharedPreferences.getString("password_tag", "");

        AnhXa();

        btnCofirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempoldPass = edtOldPass.getText().toString().trim();
                String oldPass = CreatePasswordActivity.md5HashandPepper(tempoldPass + CreatePasswordActivity.pepperHasing);
                String newPass = edtNewPass.getText().toString().trim();
                String renewPass = edtReNewPass.getText().toString().trim();

                if(tempoldPass.equals("") || newPass.equals("") ||renewPass.equals("")){
                    // there is no password
                    Toast.makeText(ResetPasswordActivity.this, R.string.entered_password_yet, Toast.LENGTH_SHORT).show();
                } else {
                    // Password is not enough 6 digits
                    if(tempoldPass.length() < 6 || newPass.length() < 6 ||renewPass.length() < 6){
                        Toast.makeText(ResetPasswordActivity.this,R.string.six_digits, Toast.LENGTH_SHORT).show();
                    } else {
                        if(!oldPass.equals(password_Prefs)){
                            Toast.makeText(ResetPasswordActivity.this,R.string.rong_password, Toast.LENGTH_SHORT).show();
                        } else {
                            // old password is correct
                            if(newPass.equals(renewPass)){
                                // the new passwords match
                                editor.remove("password_tag");
                                editor.putString("password_tag", CreatePasswordActivity.md5HashandPepper(newPass + CreatePasswordActivity.pepperHasing));
                                editor.commit();

                                // Enter the EnterPassword Activity
                                Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // there is no match on the passwords
                                Toast.makeText(ResetPasswordActivity.this,R.string.password_donot_match, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });

        btnCancleReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResetPasswordActivity.this, EnterPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private  void AnhXa(){
        edtOldPass = (EditText) findViewById(R.id.editTextNumberPasswordCurrent);
        edtNewPass = (EditText) findViewById(R.id.editTextTextPasswordNew);
        edtReNewPass = (EditText) findViewById(R.id.editTextNumberPasswordCofirmNew);
        btnCofirmReset = (Button) findViewById(R.id.buttonConfirmReset);
        btnCancleReset = (Button) findViewById(R.id.buttonCancelReset);
    }
}