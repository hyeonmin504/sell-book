package com.example.booksell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

// ...

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                                    Toast.makeText(loginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                    // 사용자 정보를 Firebase Firestore에 저장
                                    Map<String, String> user = new HashMap<>();
                                    user.put("email", email);
                                    user.put("password", password);

                                    // "users" 컬렉션에 사용자 정보 저장
                                    firestore.collection("users")
                                            .document(mAuth.getCurrentUser().getUid()) // 사용자의 고유 ID로 문서 생성
                                            .set(user);

                                    // 예: SharedPreferences를 사용하여 로그인 상태 저장
                                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();

                                    // 현재 액티비티(로그인 화면)를 종료하고 이전 화면으로 돌아감

                                    Intent intent = new Intent(loginActivity.this, MyPage.class);
                                    startActivity(intent);
                                } else {
                                    // 로그인 실패
                                    Toast.makeText(loginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
