package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;

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

    String answerInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                answerInput = edtAnswer.getText().toString().trim();
                if(answerInput.equals(answer)){
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    intent.putExtra("check_out", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Câu trả lời của bạn không đúng!!!", Toast.LENGTH_SHORT).show();
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