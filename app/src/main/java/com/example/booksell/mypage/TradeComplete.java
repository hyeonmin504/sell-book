package com.example.booksell.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.booksell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//chatActivity에서 거래 완료된 정보를 보여줍니다
public class TradeComplete extends AppCompatActivity {

    private ListView lv_tradeList;
    private FirebaseFirestore db;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_complete);

        btn_back = findViewById(R.id.btn_back);
        lv_tradeList = findViewById(R.id.Lv_tradeList);
        db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = preferences.getString("email", "");

        //tradeList DB를 구매자 = 현재 접속한 사람 기준으로 구매내역을 보여줍니다
        db.collection("tradeList")
                .whereEqualTo("buyer", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> tradeDetails = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String bookName = document.getString("bookName");
                                String sellerEmail = document.getString("seller");

                                // 이메일을 사용하여 판매자의 닉네임 가져오기
                                getNicknameFromEmail(sellerEmail, new OnNicknameFetchedListener() {
                                    @Override
                                    public void onNicknameFetched(String nickname) {
                                        tradeDetails.add("Book: " + bookName + "\nSeller: " + nickname);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(TradeComplete.this, android.R.layout.simple_list_item_1, tradeDetails);
                                        lv_tradeList.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    }
                });

        //뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TradeComplete.this, MyPage.class);
                startActivity(intent);
            }
        });
    }

    //닉네임으로 바꾸는 메서드
    private void getNicknameFromEmail(String email, OnNicknameFetchedListener listener) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                            String nickname = userDocument.getString("nickname");
                            listener.onNicknameFetched(nickname);
                        }
                    }
                });
    }
    private interface OnNicknameFetchedListener {
        void onNicknameFetched(String nickname);
    }
}