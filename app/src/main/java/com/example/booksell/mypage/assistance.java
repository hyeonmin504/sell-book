package com.example.booksell.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booksell.R;

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

        // 로그인된 이메일을 여기에서 설정하거나, 로그인 기능이 구현되었다면 해당 정보로 설정할 수 있습니다.
        String loggedInEmail = email;
        displayedEmailTextView.setText(loggedInEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void onInquiryButtonClick(View view) {
        // 문의하기 버튼이 클릭되었을 때 실행되는 메서드
        String inquiryText = inquiryEditText.getText().toString();
        // inquiryText를 사용하여 문의 처리 로직을 추가할 수 있습니다.
    }
}