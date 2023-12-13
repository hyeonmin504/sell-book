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

//판매중인 책 정보 보여주는 페이지
public class SellBook extends AppCompatActivity {
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
        adapter = new FavoriteBookAdapter(bookList);
        rv.setAdapter(adapter);

        //책 정보 DB에서 내가 판매중인 책의 이름 저자 이미지 정보만 가져옵니다
        firestore.collection("bookInfo") // favorite 컬렉션 이름
                .whereEqualTo("email", preferences.getString("email", ""))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookName = document.getString("bookName");
                        String bookAuthor = document.getString("bookAuthor");
                        String imageUrl = document.getString("imageUrl");
                        FavoriteBookInfo bookInfo = new FavoriteBookInfo(bookName, bookAuthor, email, imageUrl);

                        bookInfo.setDocumentId(document.getId());
                        bookList.add(bookInfo);
                    }
                    //최신화
                    adapter.notifyDataSetChanged();
                });

        //판매중인 책 리스트 클릭 시 showDeleteConfirmationDialog로 이동
        adapter.setOnItemClickListener(new FavoriteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteBookInfo bookInfo) {
                showDeleteConfirmationDialog(bookInfo);
            }
        });

        //마이페이지로 돌아가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //정말 삭제할건지 팝업창 띄우는 메서드
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

    // 책정보 DB에서 책 삭제
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
