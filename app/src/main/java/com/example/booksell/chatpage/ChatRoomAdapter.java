package com.example.booksell.chatpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksell.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

//채팅룸기능 어뎁터
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private Context context;
    private List<ChatRoom> chatRooms;

    public ChatRoomAdapter(Context context, List<ChatRoom> chatRooms) {
        this.context = context;
        this.chatRooms = chatRooms;
    }

    //채팅룸을 가져오기
    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    //생성자
    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    //채팅방 묶어두기
    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    //채팅방 객체 판매자 구매자 책이름 가격 정보 가져오기
    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        private TextView sellerTextView;
        private TextView buyerTextView;
        private TextView bookNameTextView;
        private TextView priceTextView;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerTextView = itemView.findViewById(R.id.sellerTextView);
            bookNameTextView = itemView.findViewById(R.id.bookNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            buyerTextView = itemView.findViewById(R.id.buyerTextView);
        }

        //1대1채팅 버튼 누르고 넘어온 정보를 가지고 구매자 판매자 책이름 가격 정보 저장하기
        public void bind(ChatRoom chatRoom) {
            fetchNickname(chatRoom.getSeller(), sellerTextView, "판매자: ");
            fetchNickname(chatRoom.getBuyer(), buyerTextView, "구매자: ");
            bookNameTextView.setText("도서명: " + chatRoom.getBookName());

            // 가격 가져와서 TextView에 설정
            fetchPrice(chatRoom.getSeller(), chatRoom.getBookName(), priceTextView);

            // 클릭 이벤트 추가 (채팅방으로 이동)
            itemView.setOnClickListener(v -> {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference usersCollection = firestore.collection("users");

                String seller = chatRoom.getSeller();
                String buyer = chatRoom.getBuyer();

                Intent intent = new Intent(context, ChatActivity.class);

                // user DB에서 판매자 정보인 이메일 말고 닉네임으로 바꿔서 1대1 채팅방으로 보내기
                usersCollection
                        .whereEqualTo("email", seller)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String sellerNickname = documentSnapshot.getString("nickname");

                                if (sellerNickname != null) {
                                    intent.putExtra("seller", sellerNickname); // sellerNickname으로 수정
                                    intent.putExtra("sellerEmail", seller); // 이메일 추가

                                    // 구매자도 마찬가지로 닉네임 가져오기
                                    usersCollection
                                            .whereEqualTo("email", buyer)
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                if (!queryDocumentSnapshots1.isEmpty()) {
                                                    DocumentSnapshot documentSnapshot1 = queryDocumentSnapshots1.getDocuments().get(0);
                                                    String buyerNickname = documentSnapshot1.getString("nickname");

                                                    // 나머지 정보 저장해서 1대1채팅방으로 넘어가기
                                                    if (buyerNickname != null) {
                                                        intent.putExtra("buyer", buyerNickname);
                                                        intent.putExtra("buyerEmail", buyer);
                                                        intent.putExtra("bookName", chatRoom.getBookName());

                                                        context.startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            });
        }

        //채팅방 리스트를 보여줄 때 이메일을 닉네임으로 다 치환
        //위 코드와 중복되는 이유는 db를 설계할 때 pk를 만들지 않아서 데이터 중복이 발생했습니다(혼합키로 pk(불완전)를 설정했습니다).
        //이 점은 리펙토링을 해야하는 부분입니다.
        private void fetchNickname(String userEmail, TextView textView, String rolePrefix) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference usersCollection = firestore.collection("users");

            usersCollection
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String nickname = documentSnapshot.getString("nickname");

                            if (nickname != null) {
                                textView.setText(rolePrefix + nickname);
                            } else {
                                textView.setText(rolePrefix + "닉네임 없음");
                            }
                        } else {
                            textView.setText(rolePrefix + "닉네임 없음");
                        }
                    })
                    .addOnFailureListener(e -> {
                        textView.setText(rolePrefix + "닉네임 가져오기 실패");
                    });
        }

        //가격정보는 bookinfo DB에서 책 이름을 통해 가지고 가져옴
        private void fetchPrice(String seller, String bookName, TextView priceTextView) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference bookInfoCollection = firestore.collection("bookInfo");

            bookInfoCollection.whereEqualTo("email", seller)
                    .whereEqualTo("bookName", bookName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // 쿼리 결과에서 가격을 가져옴
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Long priceLong = documentSnapshot.getLong("bookPrice");

                            if (priceLong != null) {
                                int price = priceLong.intValue(); // long으로도 변경 가능
                                priceTextView.setText("가격: " + price + "원");
                            } else {
                                // 해당하는 도서 정보가 없는 경우
                                priceTextView.setText("가격 정보 없음");
                            }
                        } else {
                            // 해당하는 도서 정보가 없는 경우
                            priceTextView.setText("가격 정보 없음");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // 쿼리 실패한 경우
                        priceTextView.setText("가격 정보를 가져오지 못했습니다.");
                    });
        }
    }
}