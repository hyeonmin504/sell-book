package com.example.booksell.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksell.R;

import java.util.List;

//즐겨찾기 목록 리사이클뷰 어뎁터
public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.ViewHolder> {
    private List<FavoriteBookInfo> bookList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(FavoriteBookInfo bookInfo);
    }

    //클릭했을 때 정보 반환
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public FavoriteBookAdapter(List<FavoriteBookInfo> bookList) {
        this.bookList = bookList;
    }

    //즐찾 목록을 가져옵니다
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
        return new ViewHolder(view);
    }

    //데이터가 없을 때 삭제하고 다시 최신화 과정
    public void removeItem(int position) {
        bookList.remove(position);
        notifyItemRemoved(position);
    }

    //해당 책의 사진을 올리기 위한 부분 하지만 화면에 띄우진 못 했습니다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteBookInfo bookInfo = bookList.get(position);
        holder.bookNameTextView.setText(bookInfo.getBookName());
        holder.bookAuthorTextView.setText(bookInfo.getBookAuthor());

        if (bookInfo.getImageUrl() != null && !bookInfo.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(bookInfo.getImageUrl())
                    .into(holder.bookImageView);
        }

        // 책정보를 클릭했을 때
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bookInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    //즐찾 리사이클 뷰 객체 저장
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameTextView;
        TextView bookAuthorTextView;

        ImageView bookImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookNameTextView = itemView.findViewById(R.id.name);
            bookAuthorTextView = itemView.findViewById(R.id.person);
            bookImageView = itemView.findViewById(R.id.book_iv);
        }
    }
}

