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

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private Context context;
    private List<ChatRoom> chatRooms;

    public ChatRoomAdapter(Context context, List<ChatRoom> chatRooms) {
        this.context = context;
        this.chatRooms = chatRooms;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

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

        public void bind(ChatRoom chatRoom) {
            fetchNickname(chatRoom.getSeller(), sellerTextView, "판매자: ");
            fetchNickname(chatRoom.getBuyer(), buyerTextView, "구매자: ");
            bookNameTextView.setText("도서명: " + chatRoom.getBookName());

            // 가격 가져와서 TextView에 설정
            fetchPrice(chatRoom.getSeller(), chatRoom.getBookName(), priceTextView);

            // 클릭 이벤트 추가 (채팅방으로 이동)
            itemView.setOnClickListener(v -> {
                // 채팅방으로 이동하는 로직 추가
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("seller", chatRoom.getSeller());
                intent.putExtra("bookName", chatRoom.getBookName());
                intent.putExtra("buyer", chatRoom.getBuyer());
                context.startActivity(intent);
            });
        }



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

        // 가격 가져와서 TextView에 설정
        private void fetchPrice(String seller, String bookName, TextView priceTextView) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference bookInfoCollection = firestore.collection("bookInfo");

            // bookInfo 컬렉션에서 해당 판매자와 도서명에 맞는 가격을 가져옴
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