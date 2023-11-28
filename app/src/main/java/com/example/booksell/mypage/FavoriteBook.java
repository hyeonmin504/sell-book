package com.example.booksell.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksell.buypage.BookInfoPage;
import com.example.booksell.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;



public class FavoriteBook extends AppCompatActivity {

    RecyclerView rv;
    Button btn_back;
    List<FavoriteBookInfo> bookList;
    private FirebaseFirestore firestore;
    FavoriteBookAdapter adapter; // 수정된 부분
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);
        btn_back = findViewById(R.id.btn_back);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String email = preferences.getString("email", "");

        // RecyclerView 어댑터를 초기화
        adapter = new FavoriteBookAdapter(bookList); // 수정된 부분
        rv.setAdapter(adapter);

        // Firestore에서 데이터를 가져옵니다.
        firestore.collection("favorite") // favorite 컬렉션 이름
                .whereEqualTo("email", preferences.getString("email", "")) // 현재 로그인한 사용자의 이메일과 일치하는 문서만 가져오기
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookName = document.getString("bookName");
                        String bookAuthor = document.getString("bookAuthor");
                        // 필요한 다른 필드도 가져올 수 있습니다.

                        // 가져온 데이터를 모델 객체에 추가
                        FavoriteBookInfo bookInfo = new FavoriteBookInfo(bookName, bookAuthor, email);
                        bookList.add(bookInfo);
                    }

                    // 데이터가 변경되었음을 어댑터에 알림
                    adapter.notifyDataSetChanged();
                });

        // 클릭 리스너 설정
        adapter.setOnItemClickListener(new FavoriteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteBookInfo bookInfo) {
                // 클릭 이벤트 처리
                // bookInfo를 이용하여 필요한 작업 수행
                String selectedBookName = bookInfo.getBookName();
                String selectedBookAuthor = bookInfo.getBookAuthor();

                // 다음 페이지로 정보 전달
                Intent intent = new Intent(FavoriteBook.this, BookInfoPage.class);
                intent.putExtra("bookName", selectedBookName);
                intent.putExtra("bookAuthor", selectedBookAuthor);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}