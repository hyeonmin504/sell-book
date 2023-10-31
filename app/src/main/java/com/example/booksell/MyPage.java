package com.example.booksell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MyPage extends AppCompatActivity {
    ListView Lv_mypage;
    Button btn_my, btn_chat,btn_sell,btn_buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Lv_mypage = findViewById(R.id.Lv_mypage);
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);

        String[] items = {"판매중인 책","즐겨찾기","내정보","구매내역","문의하기","이용약관"};

        // items 배열을 ArrayList로 변환
        ArrayList<String> itemList = new ArrayList<>();
        for (String item : items) {
            itemList.add(item);
        }

        // ArrayAdapter를 사용하여 ListView에 아이템을 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        Lv_mypage.setAdapter(adapter);

        Lv_mypage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭된 항목에 따라 원하는 작업 수행
                if (position == 0) {
                    Intent intent = new Intent(MyPage.this, SellBook.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MyPage.this, FavoriteBook.class);
                    startActivity(intent);
                } else if (position == 2 || position == 3 || position == 4 || position == 5){
                    Intent intent = new Intent(MyPage.this, Before_constructionpage.class);
                    startActivity(intent);
                }
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this,BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this,SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this,ChatListPage.class);
                startActivity(intent);
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this,MyPage.class);
                startActivity(intent);
            }
        });
    }
}