package com.example.booksell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.ViewHolder> {
    private List<FavoriteBookInfo> bookList;
    private OnItemClickListener onItemClickListener; // 클릭 리스너 인터페이스
    private OnItemLongClickListener onItemLongClickListener;


    // 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(FavoriteBookInfo bookInfo);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(FavoriteBookInfo bookInfo);
    }

    // 클릭 리스너 설정 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // 롱클릭 리스너 설정 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public FavoriteBookAdapter(List<FavoriteBookInfo> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
        return new ViewHolder(view);
    }

    public void removeItem(int position) {
        bookList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteBookInfo bookInfo = bookList.get(position);
        holder.bookNameTextView.setText(bookInfo.getBookName());
        holder.bookAuthorTextView.setText(bookInfo.getBookAuthor());

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인터페이스를 통해 클릭 이벤트를 외부로 전달
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameTextView;
        TextView bookAuthorTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookNameTextView = itemView.findViewById(R.id.name);
            bookAuthorTextView = itemView.findViewById(R.id.person);
            // 필요한 다른 뷰를 여기에 연결하세요.
        }
    }
}

