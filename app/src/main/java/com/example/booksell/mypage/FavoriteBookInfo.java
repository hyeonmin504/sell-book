package com.example.booksell.mypage;

public class FavoriteBookInfo {
    private String bookName;
    private String bookAuthor;
    private String email;
    private String documentId;

    public String getEmail() {
        return email;
    }

    public FavoriteBookInfo() {
        // 기본 생성자
    }

    public FavoriteBookInfo(String bookName, String bookAuthor,String email) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.email = email;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
    // 다른 필요한 getter 및 setter 메서드 추가
}

