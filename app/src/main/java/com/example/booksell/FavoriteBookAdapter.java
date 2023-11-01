package com.example.booksell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.ViewHolder> {
    private List<FavoriteBookInfo> bookList;

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
        // 필요한 다른 필드를 여기에 설정할 수 있습니다.
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

