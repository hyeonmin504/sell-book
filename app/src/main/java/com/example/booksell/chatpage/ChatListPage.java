package com.example.booksell.chatpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksell.buypage.BuyPage;
import com.example.booksell.mypage.MyPage;
import com.example.booksell.R;
import com.example.booksell.sellpage.SellPage;
import com.example.booksell.service.loginActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//채팅방 리스트 페이지
public class ChatListPage extends AppCompatActivity {
    Button btn_my, btn_chat, btn_sell, btn_buy;
    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    List<ChatRoom> chatRooms;
    boolean isLoggedIn = false;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_page);

        // 메뉴(툴바)바 = 액티비티에 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("채팅 List");

        recyclerView = findViewById(R.id.rv);
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        chatRooms = new ArrayList<>();
        tv_login = findViewById(R.id.tv_login);
        adapter = new ChatRoomAdapter(this, chatRooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 현재 로그인 정보 가져오기
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        //로그인 유무에 따른 뷰
        if (isLoggedIn) {
            tv_login.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            tv_login.setVisibility(View.VISIBLE);
        }

        //로그인 안했을 때 보이는 뷰에서 로그인하기 버튼을 눌렀을 때
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoggedIn) {
                    Intent intent = new Intent(ChatListPage.this, loginActivity.class);
                    startActivity(intent);
                } else {
                    tv_login.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // ChatListPage에서 seller, bookName, buyer 정보 가져오기
        Intent intent = getIntent();
        String seller = intent.getStringExtra("seller");
        String bookName = intent.getStringExtra("bookName");
        String buyer = intent.getStringExtra("buyer");

        // Intent로 왔을 때만 저장
        if (seller != null && bookName != null && buyer != null) {
            saveChatRoomInfo(seller, bookName, buyer);
        }

        // 데이터 가져오기
        filterBuyingChats();

        //메뉴바
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListPage.this, BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListPage.this, SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListPage.this,ChatListPage.class);
                startActivity(intent);
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListPage.this, MyPage.class);
                startActivity(intent);
            }
        });
    }

    //메뉴 툴바 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
        return true;
    }

    //메뉴바 판매중인 책 구매중인 책
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_filter_selling:
                filterSellingChats();
                return true;

            case R.id.menu_filter_buying:
                filterBuyingChats();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

// 채팅방 정보를 파이어베이스에 저장하는 메서드
    private void saveChatRoomInfo(String seller, String bookName, String buyer) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference chatRoomsCollection = firestore.collection("chatRoomNum");

        String documentId = seller + "_" + bookName + "_" + buyer;

        chatRoomsCollection.document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Toast.makeText(ChatListPage.this, "이미 존재하는 채팅방입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (seller.equals(buyer)) {
                                Toast.makeText(ChatListPage.this, "사용자가 등록한 책입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                ChatRoom chatRoom = new ChatRoom(seller, bookName, buyer, false, false);
                                chatRoomsCollection.document(documentId)
                                        .set(chatRoom)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ChatListPage.this, "채팅방 생성 완료", Toast.LENGTH_SHORT).show();

                                            filterBuyingChats();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ChatListPage.this, "채팅방 생성 실패", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    } else {
                        // 쿼리 실패한 경우
                        Toast.makeText(ChatListPage.this, "중복 확인 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 판매 중인 채팅목록을 가져오는 메서드
    private void filterSellingChats() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference chatRoomsCollection = firestore.collection("chatRoomNum");
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String currentUserEmail = preferences.getString("email", "");

        chatRoomsCollection
                .whereEqualTo("seller", currentUserEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    chatRooms.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ChatRoom chatRoom = documentSnapshot.toObject(ChatRoom.class);
                        chatRooms.add(chatRoom);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatListPage.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                });
    }

    // 구매 중인 채팅목록을 가져오는 메서드
    private void filterBuyingChats() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference chatRoomsCollection = firestore.collection("chatRoomNum");
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String currentUserEmail = preferences.getString("email", "");

        chatRoomsCollection
                .whereEqualTo("buyer", currentUserEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    chatRooms.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ChatRoom chatRoom = documentSnapshot.toObject(ChatRoom.class);
                        chatRooms.add(chatRoom);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChatListPage.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                });
    }
}