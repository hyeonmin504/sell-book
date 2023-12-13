package com.example.booksell.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.booksell.R;

//앱 실행시 맨 초기 화면 이미지 슬라이더 어뎁터
public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image4,R.drawable.image5,R.drawable.image6};

    public ImageSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //이미지 슬라이드 되도록 만든 메서드
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_slider_item, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
