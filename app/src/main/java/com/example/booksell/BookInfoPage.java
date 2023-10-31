package com.example.booksell;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookInfoPage extends AppCompatActivity {
    Button btn_star, btn_sub;

    // Firestore 관련
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_page);

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance();

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
                Intent intent = new Intent(view.getContext(), FavoriteBook.class);
                view.getContext().startActivity(intent);
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
        bookAuthorTextView.setText("담당교수: " + bookAuthor);

        // Firestore에서 책 가격(bookPrice) 가져오기
        firestore.collection("bookInfo")
                .whereEqualTo("bookName", bookName)
                .whereEqualTo("bookAuthor", bookAuthor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double bookPrice = document.getDouble("bookPrice");
                                String publisher = document.getString("publisher");
                                String publisherDate = document.getString("publisher_date");
                                String state = document.getString("state");
                                boolean state1 = document.getBoolean("state1");
                                boolean state2 = document.getBoolean("state2");
                                boolean state3 = document.getBoolean("state3");
                                boolean write1 = document.getBoolean("write1");
                                boolean write2 = document.getBoolean("write2");
                                boolean write3 = document.getBoolean("write3");

                                if (bookPrice != null) {
                                    TextView bookPriceTextView = findViewById(R.id.priceTextView);
                                    bookPriceTextView.setText("가격: " + bookPrice);
                                }
                                // publisher, publisher_date, state를 TextView 등을 사용하여 표시
                                TextView publisherTextView = findViewById(R.id.publisherTextView);
                                TextView publisherDateTextView = findViewById(R.id.publisher_dateTextView);
                                TextView stateTextView = findViewById(R.id.stateTextView);

                                publisherTextView.setText("출판사: " + publisher);
                                publisherDateTextView.setText("출판일: " + publisherDate);
                                stateTextView.setText("판매자의 상태 설명 ↓ " + state);

                                // 체크박스 정보를 사용하여 원하는 동작 수행
                                if (state1) {
                                    CheckBox CB_state1 = findViewById(R.id.CB_state1);
                                    CB_state1.setChecked(state1);
                                }
                                if (state2) {
                                    CheckBox CB_state2 = findViewById(R.id.CB_state2);
                                    CB_state2.setChecked(state2);
                                }
                                if (state3) {
                                    CheckBox CB_state3 = findViewById(R.id.CB_state3);
                                    CB_state3.setChecked(state3);
                                }
                                if (write1) {
                                    CheckBox CB_write1 = findViewById(R.id.CB_write1);
                                    CB_write1.setChecked(write1);
                                }
                                if (write2) {
                                    CheckBox CB_write2 = findViewById(R.id.CB_write2);
                                    CB_write2.setChecked(write2);
                                }
                                if (write3) {
                                    CheckBox CB_write3 = findViewById(R.id.CB_write3);
                                    CB_write3.setChecked(write3);
                                }
                            }
                        } else {
                            // 검색 실패 처리
                        }
                    }
                });

    }
}
