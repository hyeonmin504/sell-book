package com.example.booksell.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksell.R;

public class termsofServicee extends AppCompatActivity {

    TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_constructionpage);
        tv_back = findViewById(R.id.agreeButton);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(termsofServicee.this, "동의 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}