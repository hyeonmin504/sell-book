package com.example.booksell.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.booksell.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SellBook extends AppCompatActivity {

    private static final long DOUBLE_CLICK_INTERVAL = 2000; // 2 seconds
    private long lastClickTime = 0;
    RecyclerView rv;
    Button btn_back;
    List<FavoriteBookInfo> bookList;

    private FirebaseFirestore firestore;
    FavoriteBookAdapter adapter;
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);
        btn_back = findViewById(R.id.btn_back);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String email = preferences.getString("email","");

        // RecyclerView 어댑터를 초기화
        adapter = new FavoriteBookAdapter(bookList);
        rv.setAdapter(adapter);
        // Firestore에서 데이터를 가져옵니다.
        firestore.collection("bookInfo") // favorite 컬렉션 이름
                .whereEqualTo("email", preferences.getString("email", "")) // 현재 로그인한 사용자의 이메일과 일치하는 문서만 가져오기
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookName = document.getString("bookName");
                        String bookAuthor = document.getString("bookAuthor");
                        String imageUrl = document.getString("imageUrl");
                        // 필요한 다른 필드도 가져올 수 있습니다.

                        // 가져온 데이터를 모델 객체에 추가
                        FavoriteBookInfo bookInfo = new FavoriteBookInfo(bookName, bookAuthor, email, imageUrl);

                        // 문서 ID 설정
                        bookInfo.setDocumentId(document.getId());

                        bookList.add(bookInfo);
                    }

                    // 데이터가 변경되었음을 어댑터에 알림
                    adapter.notifyDataSetChanged();
                });

        // 클릭 리스너 설정
        adapter.setOnItemClickListener(new FavoriteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteBookInfo bookInfo) {
                showDeleteConfirmationDialog(bookInfo);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 삭제 확인 다이얼로그 표시
    private void showDeleteConfirmationDialog(final FavoriteBookInfo bookInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 확인");
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 삭제 작업 수행
                deleteBookFromDatabase(bookInfo);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // 데이터베이스에서 책 삭제
    private void deleteBookFromDatabase(FavoriteBookInfo bookInfo) {
        // bookInfo의 documentId를 가져옴 (FavoriteBookInfo 클래스에 getDocumentId 메서드 필요)
        String documentId = bookInfo.getDocumentId();

        Log.d(documentId, "deleteBookFromDatabase: ");

        // Firestore에서 해당 문서 삭제
        firestore.collection("bookInfo").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // 삭제 성공 시 RecyclerView에서도 삭제
                        adapter.removeItem(bookList.indexOf(bookInfo));
                        Toast.makeText(SellBook.this, "책이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 삭제 실패 시 메시지 표시
                        Toast.makeText(SellBook.this, "책 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
