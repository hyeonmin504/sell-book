package com.example.booksell;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;



public class FavoriteBook extends AppCompatActivity {
    RecyclerView rv;
    Button btn_back,remove_button;
    List<FavoriteBookInfo> bookList;
    private FirebaseFirestore firestore;
    FavoriteBookAdapter adapter = new FavoriteBookAdapter(bookList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);
        btn_back = findViewById(R.id.btn_back);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        // Firestore에서 데이터를 가져옵니다.
        firestore.collection("favorite") // favorite 컬렉션 이름
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookName = document.getString("bookName");
                        String bookAuthor = document.getString("bookAuthor");

                        // 필요한 다른 필드도 가져올 수 있습니다.

                        // 가져온 데이터를 모델 객체에 추가
                        FavoriteBookInfo bookInfo = new FavoriteBookInfo(bookName, bookAuthor);
                        bookList.add(bookInfo);
                    }

                    // RecyclerView 어댑터를 설정하여 데이터를 표시
                    FavoriteBookAdapter adapter = new FavoriteBookAdapter(bookList);
                    rv.setAdapter(adapter);
                });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

