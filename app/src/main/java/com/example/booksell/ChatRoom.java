package com.example.booksell;

public class ChatRoom {
    private String seller;
    private String bookName;
    private String buyer;
    private String roomId;

    public ChatRoom() {
        // Default constructor required for calls to DataSnapshot.getValue(ChatRoom.class)
    }

    public ChatRoom(String seller, String bookName, String buyer) {
        this.seller = seller;
        this.bookName = bookName;
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
