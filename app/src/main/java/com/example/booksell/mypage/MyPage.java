package com.example.booksell.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.booksell.service.termsofServicee;
import com.example.booksell.R;
import com.example.booksell.buypage.BuyPage;
import com.example.booksell.chatpage.ChatListPage;
import com.example.booksell.service.loginActivity;
import com.example.booksell.sellpage.SellPage;

import java.util.ArrayList;

//마이페이지 리스트뷰 부분
public class MyPage extends AppCompatActivity {
    ListView Lv_mypage;
    TextView tv_login;
    boolean isLoggedIn = false;
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
        tv_login = findViewById(R.id.tv_login);

        String[] items = {"판매중인 책","즐겨찾기","내정보","구매내역","문의하기","이용약관","로그아웃"};

        // items 배열을 ArrayList로 변환
        ArrayList<String> itemList = new ArrayList<>();
        for (String item : items) {
            itemList.add(item);
        }

        Log.d(String.valueOf(isLoggedIn), "onCreate: ");

        //로그인 안한 경우 다른 화면 바꾸기
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoggedIn) {
                    Intent intent = new Intent(MyPage.this, loginActivity.class);
                    startActivity(intent);
                } else {
                    tv_login.setVisibility(View.INVISIBLE);
                    Lv_mypage.setVisibility(View.VISIBLE);
                }
            }
        });

        // 로그인 상태 체크
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String email = preferences.getString("email", "");

        Log.d("LoginStatus", "isLoggedIn: " + isLoggedIn);

        // 로그인에 따라 뷰 설정
        if (isLoggedIn) {
            tv_login.setVisibility(View.INVISIBLE);
            Lv_mypage.setVisibility(View.VISIBLE);
        } else {
            tv_login.setVisibility(View.VISIBLE);
        }

        // 리스트뷰 생성
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
                } else if (position == 2){
                    Intent intent = new Intent(MyPage.this, MyInfo.class);
                    startActivity(intent);
                } else if(position == 3){
                    Intent intent = new Intent(MyPage.this, TradeComplete.class);
                    startActivity(intent);
                } else if(position == 4){
                    Intent intent = new Intent(MyPage.this, assistance.class);
                    startActivity(intent);
                } else if(position == 5){
                    Intent intent = new Intent(MyPage.this, termsofServicee.class);
                    startActivity(intent);
                } else if( position == 6){
                    Lv_mypage.setVisibility(View.INVISIBLE);
                    tv_login.setVisibility(View.VISIBLE);
                    logout();
                }
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this, BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this, SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPage.this, ChatListPage.class);
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

    //로그아웃 버튼을 눌렀을 때 preference 정보 삭제및 로그아웃 적용
    private void logout() {
        // SharedPreferences를 통해 로그인 상태를 변경
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // 현재 액티비티를 종료하고 이전 화면으로 돌아감
        Intent intent = new Intent(MyPage.this,MyPage.class);
        startActivity(intent);
    }
}