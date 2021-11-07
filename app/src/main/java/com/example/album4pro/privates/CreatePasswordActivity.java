package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText edtNewPass, edtRePass;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        SharedPreferences sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);

        edtNewPass = (EditText) findViewById(R.id.editTextNumberPasswordFirst);
        edtRePass = (EditText) findViewById(R.id.editTextNumberPasswordSecond);
        btnConfirm = (Button) findViewById(R.id.buttonConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firtpass = edtNewPass.getText().toString().trim();
                String secondpass = edtRePass.getText().toString().trim();

                if(firtpass.equals("") || secondpass.equals("")){
                    // there is no password
                    Toast.makeText(CreatePasswordActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
                } else {
                    if(firtpass.equals(secondpass)){
                        // Save password
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password_tag", secondpass);
                        editor.commit();

                        // Enter the app
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // there is no match on the passwords
                        Toast.makeText(CreatePasswordActivity.this, "Sorry! Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}