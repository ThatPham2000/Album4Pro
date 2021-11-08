package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

public class EnterPasswordActivity extends AppCompatActivity {
    EditText edtPass;
    Button btnEnter, btnReset;
    Button btnYesDia, btnNoDia;
    ImageView imgShow;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        // load password
        sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);
        String pass = sharedPreferences.getString("password_tag", "");

        edtPass = (EditText) findViewById(R.id.editTextPassword);
        btnEnter = (Button) findViewById(R.id.buttonEnter);
        btnReset = (Button) findViewById(R.id.buttonReset);
        imgShow = (ImageView) findViewById(R.id.imageViewHideShowEnter);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPass = edtPass.getText().toString();

                if(pass.equals(enteredPass)){
                    // Enter the app, Finish Activity hiện tại
                    finish();
                } else {
                    Toast.makeText(EnterPasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogResetPass();
            }
        });

    }
    private void ShowDialogResetPass(){
        Dialog dialog = new Dialog(EnterPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Do not show Title of Dialog
        dialog.setContentView(R.layout.dialog_change_password);
        dialog.setCanceledOnTouchOutside(false);

        // Anh xa
        btnYesDia = (Button) dialog.findViewById(R.id.buttonYes);
        btnNoDia = (Button) dialog.findViewById(R.id.buttonNo);

        // Reset the Password
        btnYesDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void ShowHidePass(View view){

        if(view.getId()== imgShow.getId()){

            if(edtPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.icon_hide_24);

                //Show Password
                edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.icon_show);

                //Hide Password
                edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}