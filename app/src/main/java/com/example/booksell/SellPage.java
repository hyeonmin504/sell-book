package com.example.booksell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SellPage extends AppCompatActivity {
    Button btn_my, btn_chat,btn_sell,btn_buy;
    EditText edt_bookname,edt_writer,edt_publisher,edt_publisher_date,edt_price;
    private final String TAG = this.getClass().getSimpleName();
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_page);

        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        edt_bookname = findViewById(R.id.edt_bookname);
        edt_writer = findViewById(R.id.edt_writer);
        edt_publisher = findViewById(R.id.edt_publisher);
        edt_publisher_date = findViewById(R.id.edt_publisher_date);
        edt_price = findViewById(R.id.edt_price);

        imageview = findViewById(R.id.iv_book);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),new ActivityResultCallback<ActivityResult>(){
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if (result.getResultCode() == RESULT_OK)
                        {
                            Log.e(TAG, "result : " + result);
                            Intent intent = result.getData();
                            Log.e(TAG, "intent : " + intent);
                            Uri uri = intent.getData();
                            Log.e(TAG, "uri : " + uri);
//                        imageview.setImageURI(uri);
                            Glide.with(SellPage.this)
                                    .load(uri)
                                    .into(imageview);
                        }
                    }
                });

        imageview.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(intent);
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellPage.this,BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellPage.this,SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellPage.this,ChatListPage.class);
                startActivity(intent);
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellPage.this,MyPage.class);
                startActivity(intent);
            }
        });
    }
}