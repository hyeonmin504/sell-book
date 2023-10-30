package com.example.booksell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class BookInfoPage extends AppCompatActivity {
    Button btn_star, btn_sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_page);
        btn_star = findViewById(R.id.btn_star);
        btn_sub = findViewById(R.id.btn_sub);

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 정보 받아오기
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String bookAuthor = intent.getStringExtra("bookAuthor");

        // 받아온 정보를 페이지의 TextView 등을 사용하여 표시
        TextView bookNameTextView = findViewById(R.id.bookNameTextView);
        TextView bookAuthorTextView = findViewById(R.id.bookAuthorTextView);

        bookNameTextView.setText("책 이름: " + bookName);
        bookAuthorTextView.setText("저자: " + bookAuthor);


    }
}