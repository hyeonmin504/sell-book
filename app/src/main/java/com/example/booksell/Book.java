package com.example.booksell;

public class Book {
    private String bookName;
    private String bookAuthor;
    private String publisher, publisher_date,state;
    private double bookPrice;
    private boolean state1,state2,state3;
    private boolean write1,write2,write3;
    private int imageResource;
    public Book() {
    }

    public Book(String title, String author, double price, String publisher, String publisher_date, String state,
                boolean state1, boolean state2, boolean state3, boolean write1, boolean write2, boolean write3) {
        this.bookName = title;
        this.bookAuthor = author;
        this.bookPrice = price;
        this.publisher = publisher;
        this.publisher_date = publisher_date;
        this.state = state;
        this.state1 = state1;
        this.state2 = state2;
        this.state3 = state3;
        this.write1 = write1;
        this.write2 = write2;
        this.write3 = write3;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }
    public String getPublisher() { return publisher; }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getPublisher_date() {
        return publisher_date;
    }

    public void setPublisher_date(String publisher_date) {
        this.publisher_date = publisher_date;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public boolean getState1(){ return state1; }
    public void setState1(boolean state1){ this.state1 = state1; }
    public boolean getState2(){ return state2; }
    public void setState2(boolean state2){ this.state2 = state2; }
    public boolean getState3(){ return state3; }
    public void setState3(boolean state3){ this.state3 = state3; }
    public boolean getWrite1(){ return write1; }
    public void setWrite1(boolean write1){ this.write1 = write1; }
    public boolean getWrite2(){ return write2; }
    public void setWrite2(boolean write2){ this.write2 = write2; }
    public boolean getWrite3(){ return write3; }
    public void setWrite3(boolean write3){ this.write3 = write3; }
}