package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        SharedPreferences sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Password have been saved before
        String password_Prefs = sharedPreferences.getString("password_tag", "");

        AnhXa();

        btnCofirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPass = edtOldPass.getText().toString().trim();
                String newPass = edtNewPass.getText().toString().trim();
                String renewPass = edtReNewPass.getText().toString().trim();

                if(oldPass.equals("") || newPass.equals("") ||renewPass.equals("")){
                    // there is no password
                    Toast.makeText(ResetPasswordActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
                } else {
                    if(!oldPass.equals(password_Prefs)){
                        Toast.makeText(ResetPasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                    } else {
                        // old password is correct
                        if(newPass.equals(renewPass)){
                            // the new passwords match
                            editor.remove("password_tag");
                            editor.putString("password_tag", newPass);
                            editor.commit();

                            // Enter the EnterPassword Activity
                            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // there is no match on the passwords
                            Toast.makeText(ResetPasswordActivity.this, "The new passwords doesn't match!", Toast.LENGTH_SHORT).show();
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