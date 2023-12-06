package com.example.booksell.chatpage;

import android.widget.Button;

public class ChatRoom {
    private String seller;
    private String bookName;
    private String buyer;
    private boolean sellerButton;
    private boolean buyerButton;

    public ChatRoom() {
        // Default constructor required for calls to DataSnapshot.getValue(ChatRoom.class)
    }

    public ChatRoom(String seller, String bookName, String buyer, boolean sellerButton, boolean buyerButton) {
        this.seller = seller;
        this.bookName = bookName;
        this.buyer = buyer;
        this.sellerButton = sellerButton;
        this.buyerButton = buyerButton;
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

    public boolean isBuyerButton() {
        return buyerButton;
    }
    public void setBuyerButton(boolean buyerButton) {
        this.buyerButton = buyerButton;
    }

    public boolean isSellerButton() {
        return sellerButton;
    }

    public void setSellerButton(boolean sellerButton) {
        this.sellerButton = sellerButton;
    }
}
