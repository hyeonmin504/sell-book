package com.example.booksell.buypage;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksell.sellpage.Book;
import com.example.booksell.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//책 정보를 파이어베이스에서 가져오는 내부로직
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Book> bookList = new ArrayList<>();
    private ArrayList<Book> originalBookList = new ArrayList<>();
    private BuyPage buyPage;

    // BuyPage의 인스턴스를 받아오는 생성자
    public RecyclerViewAdapter(BuyPage buyPage) {
        this.buyPage = buyPage;
        FirebaseFirestore.getInstance().collection("bookInfo").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Error getting data: " + e.getMessage());
                return;
            }

            bookList.clear();
            originalBookList.clear();

            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                Book item = snapshot.toObject(Book.class);
                if (item != null) {
                    bookList.add(item);
                    originalBookList.add(item);
                }
            }
            notifyDataSetChanged();
        });
    }

    // 책정보를 목록을 보여주는 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    //책 객체를 가져와서 반환하는 뷰홀더
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
        return new ViewHolder(view);
    }

    // 책 객체의 이미지를 책 객체에 묶어두는 함수
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book currentBook = bookList.get(position);

        holder.bookNameTextView.setText(currentBook.getBookName());
        holder.bookAuthorTextView.setText(currentBook.getBookAuthor());


        if (currentBook.getImageUrl() != null && !currentBook.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(currentBook.getImageUrl())
                    .into(holder.bookImageView);
        }

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBookName = currentBook.getBookName();
                String selectedBookAuthor = currentBook.getBookAuthor();

                Intent intent = new Intent(holder.itemView.getContext(), BookInfoPage.class);
                intent.putExtra("bookName", selectedBookName);
                intent.putExtra("bookAuthor", selectedBookAuthor);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    // 키워드와 검색 옵션에 따라 검색 수행
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void search(String keyword, String searchOption) {
        ArrayList<Book> searchResults = new ArrayList<>();
        for (Book book : originalBookList) {
            if (searchOption.equals("bookName") && (book.getBookName().toLowerCase().contains(keyword.toLowerCase()))) {
                searchResults.add(book);
            } else if (searchOption.equals("bookAuthor") && (book.getBookAuthor().toLowerCase().contains(keyword.toLowerCase()))) {
                searchResults.add(book);
            }
        }

        bookList.clear();
        bookList.addAll(searchResults);
        notifyDataSetChanged();
    }
}