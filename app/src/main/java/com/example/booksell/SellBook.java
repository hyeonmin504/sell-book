package com.example.booksell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SellBook extends AppCompatActivity {

    Button btn_back;
    ListView Lv_sellbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);
        btn_back = findViewById(R.id.btn_back);
        Lv_sellbook = findViewById(R.id.Lv_sellbook);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}