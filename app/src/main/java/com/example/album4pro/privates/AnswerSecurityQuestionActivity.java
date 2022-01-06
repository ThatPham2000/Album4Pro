package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album4pro.R;

public class AnswerSecurityQuestionActivity extends AppCompatActivity {

    TextView txtQuestion;
    EditText edtAnswer;
    Button btnConfirm;
    SharedPreferences sharedPreferences;

    String answerInput;

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

        setContentView(R.layout.activity_answer_security_question);

        SharedPreferences sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);
        String question = sharedPreferences.getString("question_tag", "");
        String answer = sharedPreferences.getString("answer_tag", "");

        // Anh xa
        txtQuestion = (TextView) findViewById(R.id.textviewQuestion);
        edtAnswer = (EditText) findViewById(R.id.editTextAnswer);
        btnConfirm = (Button) findViewById(R.id.buttonConfirmQuestionA);

        txtQuestion.setText(question);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempAnswer = edtAnswer.getText().toString().trim();
                answerInput = CreatePasswordActivity.md5HashandPepper(tempAnswer + CreatePasswordActivity.pepperHasing);
                if(answerInput.equals(answer)){
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    intent.putExtra("check_out", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.rong_answer, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}