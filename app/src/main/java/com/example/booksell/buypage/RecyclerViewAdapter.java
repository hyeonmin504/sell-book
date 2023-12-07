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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Book> bookList = new ArrayList<>();
    private ArrayList<Book> originalBookList = new ArrayList<>();
    private BuyPage buyPage; // BuyPage 클래스의 인스턴스

    // BuyPage의 인스턴스를 받아오는 생성자
    public RecyclerViewAdapter(BuyPage buyPage) {
        this.buyPage = buyPage;

        // Firestore 컬렉션 리스너 초기화
        FirebaseFirestore.getInstance().collection("bookInfo").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Error getting data: " + e.getMessage());
                return;
            }

            // ArrayList 초기화
            bookList.clear();
            originalBookList.clear();

            // Firestore 문서를 가져와서 ArrayList에 추가
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

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameTextView;
        TextView bookAuthorTextView;
        ImageView bookImageView; // 추가된 ImageView

        public ViewHolder(View itemView) {
            super(itemView);
            // View에서 각 항목에 대한 참조를 가져오기
            bookNameTextView = itemView.findViewById(R.id.name);
            bookAuthorTextView = itemView.findViewById(R.id.person);
            bookImageView = itemView.findViewById(R.id.book_iv);
        }
    }

    // onCreateViewHolder: ViewHolder 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 새로운 View 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
        // ViewHolder 객체 생성 후 반환
        return new ViewHolder(view);
    }

    // onBindViewHolder: ViewHolder의 데이터를 설정
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 리스트에서 현재 위치에 해당하는 데이터 가져오기
        Book currentBook = bookList.get(position);

        // 데이터를 ViewHolder의 각 요소에 설정
        holder.bookNameTextView.setText(currentBook.getBookName());
        holder.bookAuthorTextView.setText(currentBook.getBookAuthor());


        if (currentBook.getImageUrl() != null && !currentBook.getImageUrl().isEmpty()) {
            // Glide를 사용하여 이미지 로드
            Glide.with(holder.itemView.getContext())
                    .load(currentBook.getImageUrl())
                    .into(holder.bookImageView);
        }

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 아이템의 정보 가져오기
                String selectedBookName = currentBook.getBookName();
                String selectedBookAuthor = currentBook.getBookAuthor();

                // 다음 페이지로 정보 전달
                Intent intent = new Intent(holder.itemView.getContext(), BookInfoPage.class);
                intent.putExtra("bookName", selectedBookName);
                intent.putExtra("bookAuthor", selectedBookAuthor);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    // getItemCount: 데이터 아이템의 총 개수 반환
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // search: 키워드와 검색 옵션에 따라 검색 수행
    public void search(String keyword, String searchOption) {
        ArrayList<Book> searchResults = new ArrayList<>();
        for (Book book : originalBookList) {
            if (searchOption.equals("bookName") && (book.getBookName().toLowerCase().contains(keyword.toLowerCase()))) {
                searchResults.add(book);
            } else if (searchOption.equals("bookAuthor") && (book.getBookAuthor().toLowerCase().contains(keyword.toLowerCase()))) {
                searchResults.add(book);
            }
        }
        // 검색 결과를 리스트에 설정하고 어댑터 갱신
        bookList.clear();
        bookList.addAll(searchResults);
        notifyDataSetChanged();
    }
}