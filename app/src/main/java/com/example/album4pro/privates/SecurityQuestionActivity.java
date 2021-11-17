package com.example.album4pro.privates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.album4pro.R;

import java.util.ArrayList;

public class SecurityQuestionActivity extends AppCompatActivity {

    Spinner snQuestion;
    EditText edtAnswer, edtQuestion;
    Button btnConfirm;

    ArrayList<String> arrQuestion;

    private String question;
    private String answer;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);


        snQuestion = (Spinner) findViewById(R.id.spinnerQuestion);
        edtAnswer = (EditText) findViewById(R.id.editTextEnterAnswer);
        edtQuestion = (EditText) findViewById(R.id.editTextEnterQuestion);
        btnConfirm = (Button) findViewById(R.id.buttonConfirmQuestion);

        arrQuestion = new ArrayList<>();
        AddQuestion();

        // Set apdapter cho Spiner
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arrQuestion);
        snQuestion.setAdapter(arrayAdapter);

        sharedPreferences = getSharedPreferences("PASSPREF", MODE_PRIVATE);

        snQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < arrQuestion.size() - 1) { // Tùy chỉnh
                    edtQuestion.setVisibility(View.GONE);
                    question = arrQuestion.get(i);
                } else{
                    edtQuestion.setVisibility(View.VISIBLE);
                    question = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing to do
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.equals("")){
                    question = edtQuestion.getText().toString().trim();
                }
                answer = edtAnswer.getText().toString().trim();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("question_tag", question);
                editor.putString("answer_tag", answer);
                editor.commit();

                // Enter the app
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Do not do anything
        //Còn vướng chỗ nếu tắt luôn app mà chưa đặt câu hỏi
    }

    private void AddQuestion() {
        arrQuestion.add("Món ăn yêu thích của bạn là gì?");
        arrQuestion.add("Tên bộ phim mà bạn yêu thích nhất?");
        arrQuestion.add("Tên người bạn thân nhất của bạn?");
        arrQuestion.add("Bài hát yêu thích của bạn là gì?");
        arrQuestion.add("Con số may mắn của bạn là số mấy?");
        arrQuestion.add("Màu bạn thích nhất?");
        arrQuestion.add("Tùy chỉnh");
    }
}