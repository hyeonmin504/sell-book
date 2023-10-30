package com.example.booksell;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class BookAdapter extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextPrice;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_adapter);

        firestore = FirebaseFirestore.getInstance();
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextPrice = findViewById(R.id.editTextPrice);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 데이터 가져오기
                String bookName = editTextTitle.getText().toString();
                String bookAuthor = editTextAuthor.getText().toString();
                double price = Double.parseDouble(editTextPrice.getText().toString());

                // 데이터를 Firestore에 업로드
                uploadBookData(bookName, bookAuthor, price);
            }
        });
    }

    private void uploadBookData(String title, String author, double price) {
        // Firestore에 업로드할 데이터 생성
        Book book = new Book(title, author); // Book 클래스의 생성자에 title과 author 전달

        // Firestore에 데이터 업로드
        firestore.collection("bookInfo")
                .document() // Firestore에서 자동 생성된 고유 ID로 문서 추가
                .set(book, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 업로드 성공
                            // 필요한 처리를 수행하세요

                        } else {
                            // 업로드 실패
                            // 오류 처리를 수행하세요
                        }
                    }
                });
    }
}
