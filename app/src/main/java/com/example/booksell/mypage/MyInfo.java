package com.example.booksell.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.booksell.R;
import com.example.booksell.service.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

//현재 로그인중인 정보 보여주는 페이지
public class MyInfo extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private TextView tv_nicname,tv_email,tv_login;
    Button btn_back;
    boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        tv_login = findViewById(R.id.tv_login);
        firestore = FirebaseFirestore.getInstance();
        tv_email = findViewById(R.id.tv_email);
        tv_nicname = findViewById(R.id.tv_nickname);
        btn_back = findViewById(R.id.btn_back);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        //현재 로그인한 내 정보 표시
        if (isLoggedIn) {
            String email = preferences.getString("email", "");
            String nickname = preferences.getString("nickname", "");

            firestore.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("nickname", nickname)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                String emailFromFirestore = task.getResult().getDocuments().get(0).getString("email");
                                String nicknameFromFirestore = task.getResult().getDocuments().get(0).getString("nickname");

                                tv_email.setText("이메일: " + emailFromFirestore);
                                tv_nicname.setText("닉네임: " + nicknameFromFirestore);
                            }
                        }
                    });

            tv_login.setVisibility(View.INVISIBLE);
            tv_email.setVisibility(View.VISIBLE);
            tv_nicname.setVisibility(View.VISIBLE);
        } else {
            // 타임아웃 되었을 때 로그인 화면으로
            tv_login.setVisibility(View.VISIBLE);
            tv_email.setVisibility(View.INVISIBLE);
            tv_nicname.setVisibility(View.INVISIBLE);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoggedIn) {
                    // 사용자가 로그인하지 않은 경우, 로그인 화면으로 이동
                    Intent intent = new Intent(MyInfo.this, loginActivity.class);
                    startActivity(intent);
                } else {
                    // 사용자가 로그인한 경우, 리스트뷰를 보이도록 설정
                    tv_login.setVisibility(View.INVISIBLE);
                    tv_email.setVisibility(View.VISIBLE);
                    tv_nicname.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}