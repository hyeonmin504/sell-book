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

public class BuyPage extends AppCompatActivity {

    ListView Lv_Buy;
    Button btn_my, btn_chat,btn_sell,btn_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);

        Lv_Buy = findViewById(R.id.Lv_buy);
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        String[] items = {"책 1","책 2","책 3","책 4","책 5","책 6"};

        // items 배열을 ArrayList로 변환
        ArrayList<String> itemList = new ArrayList<>();
        for (String item : items) {
            itemList.add(item);
        }

        // ArrayAdapter를 사용하여 ListView에 아이템을 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        Lv_Buy.setAdapter(adapter);

        Lv_Buy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭된 항목에 따라 원하는 작업 수행
                if (position == 0) {
                    Intent intent = new Intent(BuyPage.this, SellBook.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(BuyPage.this, FavoriteBook.class);
                    startActivity(intent);
                }
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,ChatListPage.class);
                startActivity(intent);
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,MyPage.class);
                startActivity(intent);
            }
        });
    }
}