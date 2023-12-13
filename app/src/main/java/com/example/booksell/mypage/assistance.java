package com.example.booksell.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksell.R;

//문의 페이지
public class assistance extends AppCompatActivity {
    boolean isLoggedIn = false;
    private TextView displayedEmailTextView;
    private EditText inquiryEditText;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);
        back = findViewById(R.id.btn_back);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String email = preferences.getString("email", ""); // 이메일 가져오기

        displayedEmailTextView = findViewById(R.id.displayedEmailTextView);
        inquiryEditText = findViewById(R.id.inquiryEditText);

        String loggedInEmail = email;
        displayedEmailTextView.setText(loggedInEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //따로 제출을 눌러도 정보를 전달하진 않습니다. (핵심 기능이 아니기에 구현하지 않았습니다)
    public void onInquiryButtonClick(View view) {
        Toast.makeText(assistance.this, "문의 완료", Toast.LENGTH_SHORT).show();
        String inquiryText = inquiryEditText.getText().toString();
    }
}