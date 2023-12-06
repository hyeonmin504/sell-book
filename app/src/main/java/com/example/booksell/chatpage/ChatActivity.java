package com.example.booksell.chatpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksell.R;
import com.example.booksell.buypage.BuyPage;
import com.example.booksell.mypage.TradeComplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick;
    private EditText EditText_chat;
    private Button Button_send,btn_back;
    private DatabaseReference myRef;
    private FirebaseFirestore db;
    boolean isLoggedIn = false;
    private Button btn_check;
    private boolean isBuyer;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);
        btn_back = findViewById(R.id.btn_back);
        btn_check = findViewById(R.id.btn_check);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String buyer = intent.getStringExtra("buyer");
        String seller = intent.getStringExtra("seller");
        String sellerEmail = intent.getStringExtra("sellerEmail");
        String buyerEmail = intent.getStringExtra("buyerEmail");
        String bookName = intent.getStringExtra("bookName");
        Log.d(buyer + seller, "onCreate: ");

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String nickname = preferences.getString("nickname","");
        nick = nickname;

        isBuyer = nick.equals(buyer);

        if (isBuyer) {
            btn_check.setText("구매확정버튼");
        } else {
            btn_check.setText("판매확정버튼");
        }

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBuyer) {
                    updateButtonStatus("buyerButton", true,sellerEmail,buyerEmail,bookName);
                } else {
                    updateButtonStatus("sellerButton", true,sellerEmail,buyerEmail,bookName);
                }
            }
        });

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();
                if(msg != null) {
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMsg(msg);
                    myRef.push().setValue(chat);

                    // 메시지를 전송한 후 EditText의 내용 지우기
                    EditText_chat.setText(""); // 빈 문자열로 설정하여 EditText 내용을 지움
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this,nick);
        mRecyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String sanitizedPath = buyer + "_" + seller + "_" + bookName;
        myRef = database.getReference("chatRooms").child(sanitizedPath);


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 버튼 상태를 업데이트하는 메서드
    private void updateButtonStatus(String buttonField, boolean isPressed,String seller,String buyer,String bookName) {
        // 파이어스토어에 업데이트
        db.collection("chatRoomNum").document(seller + "_" + bookName + "_" + buyer)
                .update(buttonField, isPressed)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showConfirmationDialog(seller, buyer, bookName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 실패 시 예외 처리
                        Toast.makeText(ChatActivity.this, "버튼 업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 두 버튼이 모두 눌렸는지 확인하는 메서드
    private void checkBothButtonsPressed(String seller, String buyer, String bookName) {
        // 파이어스토어에서 두 버튼 상태를 확인
        db.collection("chatRoomNum").document(seller + "_" + bookName + "_" + buyer)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            boolean buyerButton = document.getBoolean("buyerButton");
                            boolean sellerButton = document.getBoolean("sellerButton");

                            // 두 버튼이 모두 true이면 특정 동작 실행
                            if (buyerButton && sellerButton) {
                                Toast.makeText(ChatActivity.this, "거래확정", Toast.LENGTH_SHORT).show();

                                // Intent를 생성하고 정보를 추가
                                Intent newIntent = new Intent(ChatActivity.this, BuyPage.class);
                                // 다음 화면으로 전환
                                startActivity(newIntent);

                                // tradeList 컬렉션에 데이터 저장
                                saveToTradeList(seller, buyer, bookName);
                            }
                        }
                    }
                });
    }

    private void saveToTradeList(String seller, String buyer, String bookName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference tradeListCollection = firestore.collection("tradeList");

        Map<String, Object> tradeInfo = new HashMap<>();
        tradeInfo.put("seller", seller);
        tradeInfo.put("buyer", buyer);
        tradeInfo.put("bookName", bookName);

        tradeListCollection
                .add(tradeInfo)
                .addOnSuccessListener(documentReference -> {
                    // bookInfo 컬렉션에서 해당 정보 삭제
                    deleteFromBookInfo(bookName, seller);

                    // bookRoomNum 컬렉션에서 해당 정보 삭제
                    deleteFromchatRoomNum(bookName, seller);
                });
    }

    private void deleteFromBookInfo(String bookName, String seller) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference bookInfoCollection = firestore.collection("bookInfo");

        bookInfoCollection
                .whereEqualTo("bookName", bookName)
                .whereEqualTo("email", seller)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                });
    }

    private void deleteFromchatRoomNum(String bookName, String seller) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference chatRoomNumCollection = firestore.collection("chatRoomNum");

        chatRoomNumCollection
                .whereEqualTo("bookName", bookName)
                .whereEqualTo("seller", seller)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                        Log.d("DeleteFromChatRoomNum", "Document deleted from chatRoomNum");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteFromChatRoomNum", "Error deleting document from chatRoomNum", e);
                });
    }


    private void showConfirmationDialog(String seller, String buyer, String bookName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확정").setMessage("확정하시겠습니까?");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 버튼의 이름을 대기중..으로 변경
                btn_check.setText("대기중..");
                // 버튼 비활성화
                btn_check.setEnabled(false);
                checkBothButtonsPressed(seller, buyer, bookName);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소 버튼 클릭 시 아무 동작 없음
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}