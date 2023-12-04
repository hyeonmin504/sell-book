package com.example.booksell.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booksell.R;
import com.example.booksell.buypage.BuyPage;
import com.example.booksell.mypage.MyPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

// ...

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, buttonsignup;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonsignup = findViewById(R.id.buttonsignup);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공
                                    // 사용자의 이메일을 기반으로 Firestore에서 닉네임을 가져오기
                                    firestore.collection("users")
                                            .whereEqualTo("email", email)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult() != null) {
                                                        QuerySnapshot querySnapshot = task.getResult();
                                                        if (!querySnapshot.isEmpty()) {
                                                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                                            String nickname = documentSnapshot.getString("nickname");

                                                            // 예: SharedPreferences를 사용하여 로그인 상태 및 정보 저장
                                                            Log.d("LoginActivity", "Nickname: " + nickname);

                                                            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = preferences.edit();
                                                            editor.putBoolean("isLoggedIn", true);
                                                            editor.putString("email", email);
                                                            editor.putString("nickname", nickname);
                                                            editor.apply();

                                                            // 로그인 성공
                                                            Toast.makeText(loginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                                            // 로그인 성공 시 MyPage 액티비티로 이동
                                                            Intent intent = new Intent(loginActivity.this, BuyPage.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.d("LoginActivity", "사용자 정보가 없습니다.");
                                                            Toast.makeText(loginActivity.this, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Log.e("LoginActivity", "Firebase에서 사용자 정보 가져오기 실패: " + task.getException().getMessage());
                                                        Toast.makeText(loginActivity.this, "Firebase에서 사용자 정보 가져오기 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // 로그인 실패
                                    Toast.makeText(loginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 회원가입 버튼 리스너 설정
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}
