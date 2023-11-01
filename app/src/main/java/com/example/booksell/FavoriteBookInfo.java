package com.example.booksell;

public class FavoriteBookInfo {
    private String bookName;
    private String bookAuthor;
    // 다른 필요한 필드 추가

    public FavoriteBookInfo() {
        // 기본 생성자
    }

    public FavoriteBookInfo(String bookName, String bookAuthor) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        // 다른 필드 초기화
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }
    // 다른 필요한 getter 및 setter 메서드 추가
}

