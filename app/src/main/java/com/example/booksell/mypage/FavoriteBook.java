package com.example.booksell.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksell.buypage.BookInfoPage;
import com.example.booksell.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

//mypage의 즐겨찾기페이지 페이지
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

        //현재 접속중인 사람 이메일
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String email = preferences.getString("email", "");

        adapter = new FavoriteBookAdapter(bookList);
        rv.setAdapter(adapter);

        // favorite DB에서 현재 로그인중인 이메일 기준으로 즐찾한 책 정보를 가져옵니다
        firestore.collection("favorite")
                .whereEqualTo("email", preferences.getString("email", ""))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookName = document.getString("bookName");
                        String bookAuthor = document.getString("bookAuthor");

                        FavoriteBookInfo bookInfo = new FavoriteBookInfo(bookName, bookAuthor, email);
                        bookList.add(bookInfo);
                    }
                    //데이터 최신화
                    adapter.notifyDataSetChanged();
                });

        //즐겨찾기 한 객체를 클릭했을 때
        adapter.setOnItemClickListener(new FavoriteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteBookInfo bookInfo) {
                String selectedBookName = bookInfo.getBookName();
                String selectedBookAuthor = bookInfo.getBookAuthor();

                //BookInfoPage로 가서 현재 즐찾한 책정보를 보여줍니다
                firestore.collection("bookInfo")
                        .whereEqualTo("bookName", selectedBookName)
                        .whereEqualTo("bookAuthor", selectedBookAuthor)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Intent intent = new Intent(FavoriteBook.this, BookInfoPage.class);
                                intent.putExtra("bookName", selectedBookName);
                                intent.putExtra("bookAuthor", selectedBookAuthor);
                                startActivity(intent);
                            } else {
                                // 책정보가 없으면 팔렸거나 판매자가 내렸으므로 즐겨찾기 데이터를 삭제합니다
                                firestore.collection("favorite")
                                        .whereEqualTo("email", preferences.getString("email", ""))
                                        .whereEqualTo("bookName", selectedBookName)
                                        .whereEqualTo("bookAuthor", selectedBookAuthor)
                                        .get()
                                        .addOnSuccessListener(deleteQueryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot deleteDocument : deleteQueryDocumentSnapshots) {
                                                firestore.collection("favorite")
                                                        .document(deleteDocument.getId())
                                                        .delete()
                                                        .addOnSuccessListener(aVoid -> {
                                                            bookList.remove(bookInfo);
                                                            adapter.notifyDataSetChanged();
                                                            showBookRemovedPopup();
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //정말 삭제할거냐고 팝업창을 띄웁니다
    private void showBookRemovedPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteBook.this);
        builder.setTitle("책이 판매되었습니다!");
        builder.setMessage("책이 판매되어서 즐겨찾기에서 항목이 삭제됩니다");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}