package com.example.booksell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.booksell.buypage.BuyPage;
import com.example.booksell.service.ImageSliderAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageSliderAdapter adapter;
    private int currentPage = 0;
    private Handler handler;
    private Runnable runnable;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        adapter = new ImageSliderAdapter(this);
        viewPager.setAdapter(adapter);
        textView = findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭 이벤트 발생 시, 다른 액티비티로 전환
                Intent intent = new Intent(MainActivity.this, BuyPage.class);
                startActivity(intent);
            }
        });
        handler = new Handler(Looper.getMainLooper());

        // 타이머를 사용하여 이미지 자동 전환
        runnable = new Runnable() {
            public void run() {
                if (currentPage == adapter.getCount() - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 2500); // 2.5초마다 전환
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // 액티비티가 활성화되면 타이머 시작
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // 액티비티가 비활성화되면 타이머 중지
    }
}