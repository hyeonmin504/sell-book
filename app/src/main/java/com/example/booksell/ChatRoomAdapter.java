package com.example.booksell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        private TextView bookNameTextView;
        private TextView buyerTextView;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            sellerTextView = itemView.findViewById(R.id.sellerTextView);
            bookNameTextView = itemView.findViewById(R.id.bookNameTextView);
            buyerTextView = itemView.findViewById(R.id.buyerTextView);
        }

        public void bind(ChatRoom chatRoom) {
            sellerTextView.setText("판매자: " + chatRoom.getSeller());
            bookNameTextView.setText("도서명: " + chatRoom.getBookName());
            buyerTextView.setText("구매자: " + chatRoom.getBuyer());

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
    }
}