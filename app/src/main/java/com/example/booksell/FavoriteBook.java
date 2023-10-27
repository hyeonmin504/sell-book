package com.example.booksell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class FavoriteBook extends AppCompatActivity {

    Button btn_back;
    ListView Lv_fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book);
        btn_back = findViewById(R.id.btn_back);
        Lv_fav = findViewById(R.id.Lv_fav);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}