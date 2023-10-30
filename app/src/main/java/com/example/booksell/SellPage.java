package com.example.booksell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SellPage extends AppCompatActivity {
    private FirebaseFirestore firestore;
    Button btn_my, btn_chat,btn_sell,btn_buy;
    private Button btn_sub;
    EditText edt_publisher,edt_publisher_date;
    private EditText edt_bookname,edt_writer,edt_price;
    private final String TAG = this.getClass().getSimpleName();
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_page);
        firestore = FirebaseFirestore.getInstance();
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        btn_sub = findViewById(R.id.btn_sub);
        edt_bookname = findViewById(R.id.edt_bookname);
        edt_writer = findViewById(R.id.edt_writer);
        edt_publisher = findViewById(R.id.edt_publisher);
        edt_publisher_date = findViewById(R.id.edt_publisher_date);
        edt_price = findViewById(R.id.edt_price);

        imageview = findViewById(R.id.iv_book);

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName = edt_bookname.getText().toString();
                String bookAuthor =  edt_writer.getText().toString();
                double price = Double.parseDouble(edt_price.getText().toString());

                // 데이터를 Firestore에 업로드
                uploadBookData(bookName, bookAuthor, price);
            }
        });
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

    private void uploadBookData(String title, String author, double price) {
        // Firestore에 업로드할 데이터 생성
        Book book = new Book(title, author); // Book 클래스의 생성자에 title과 author 전달

        // Firestore에 데이터 업로드
        firestore.collection("bookInfo")
                .document() // Firestore에서 자동 생성된 고유 ID로 문서 추가
                .set(book, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 업로드 성공
                            // 필요한 처리를 수행하세요

                        } else {
                            // 업로드 실패
                            // 오류 처리를 수행하세요
                        }
                    }
                });
    }
}