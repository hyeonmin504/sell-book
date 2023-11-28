package com.example.booksell.service;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nicknameEditText;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore; // firestore 인스턴스를 클래스 레벨로 이동

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance(); // firestore 초기화

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        nicknameEditText = findViewById(R.id.editTextNickname);
        signUpButton = findViewById(R.id.buttonSignUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String nickname = nicknameEditText.getText().toString();

                // 이메일, 비밀번호, 닉네임이 비어있는지 확인
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nickname)) {
                    Toast.makeText(Signup.this, "이메일, 비밀번호, 닉네임을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase Authentication을 사용하여 회원가입
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // 사용자 정보를 Firebase Firestore에 저장
                                    Map<String, String> user_store = new HashMap<>();
                                    user_store.put("email", email);
                                    user_store.put("nickname", nickname);

                                    // "users" 컬렉션에 사용자 정보 저장
                                    firestore.collection("users")
                                            .document(user.getUid()) // 사용자의 고유 ID로 문서 생성
                                            .set(user_store)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Signup.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                        // 여기서 닉네임 등의 추가 정보를 저장하거나 다른 작업을 수행할 수 있습니다.
                                                    } else {
                                                        Log.e("Firestore", "Firestore에 사용자 정보 저장 실패", task.getException());
                                                    }
                                                }
                                            });
                                    Intent intent = new Intent(Signup.this, loginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // 회원가입 실패
                                    Log.e("Signup", "회원가입 실패", task.getException());
                                    Toast.makeText(Signup.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
