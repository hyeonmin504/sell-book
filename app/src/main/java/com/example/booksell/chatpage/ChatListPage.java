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
import android.widget.Toast;

import com.example.booksell.buypage.BuyPage;
import com.example.booksell.mypage.MyPage;
import com.example.booksell.R;
import com.example.booksell.sellpage.SellPage;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatListPage extends AppCompatActivity {
    Button btn_my, btn_chat, btn_sell, btn_buy;
    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    List<ChatRoom> chatRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_page);

        // 툴바를 액티비티에 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("채팅 List");

        recyclerView = findViewById(R.id.rv);
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        chatRooms = new ArrayList<>();
        adapter = new ChatRoomAdapter(this, chatRooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
        filterSellingChats();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메뉴 인플레이터를 사용하여 메뉴를 만듭니다.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴 아이템 선택에 대한 처리를 여기에 추가합니다.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_filter_selling:
                // Selling Chats 메뉴 선택 시
                filterSellingChats();
                return true;

            case R.id.menu_filter_buying:
                // Buying Chats 메뉴 선택 시
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

        // 중복 체크
        chatRoomsCollection.whereEqualTo("seller", seller)
                .whereEqualTo("bookName", bookName)
                .whereEqualTo("buyer", buyer)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // 중복된 데이터가 없으면 저장
                        if (seller.equals(buyer)) {
                            Toast.makeText(ChatListPage.this, "사용자가 등록한 책입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            ChatRoom chatRoom = new ChatRoom(seller, bookName, buyer);

                            chatRoomsCollection.add(chatRoom)
                                    .addOnSuccessListener(documentReference -> {
                                        // 성공적으로 저장된 경우
                                        String roomId = documentReference.getId();
                                        Toast.makeText(ChatListPage.this, "채팅방 생성 완료", Toast.LENGTH_SHORT).show();

                                        // 저장 후에 목록을 갱신
                                        filterBuyingChats();
                                    })
                                    .addOnFailureListener(e -> {
                                        // 저장 실패한 경우
                                        Toast.makeText(ChatListPage.this, "채팅방 생성 실패", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // 중복된 데이터가 있으면 메시지 표시
                        Toast.makeText(ChatListPage.this, "이미 존재하는 채팅방입니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // 쿼리 실패한 경우
                    Toast.makeText(ChatListPage.this, "중복 확인 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                });
    }

    // 판매 중인 채팅을 가져오는 메서드
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

    // 구매 중인 채팅을 가져오는 메서드
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