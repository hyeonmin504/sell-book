package com.example.booksell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BuyPage extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String searchOption = "bookName";
    RecyclerView recyclerView;
    Button btn_my, btn_chat,btn_sell,btn_buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);
        btn_my = findViewById(R.id.btn_my);
        btn_chat = findViewById(R.id.btn_chat);
        btn_sell = findViewById(R.id.btn_sell);
        btn_buy = findViewById(R.id.btn_buy);
        recyclerView = findViewById(R.id.recyclerview);
        // Firestore 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance();

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new RecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 스피너 초기화
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // "이름" 선택
                        searchOption = "bookName";
                        break;
                    case 1: // "번호" 선택
                        searchOption = "bookAuthor";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 검색 버튼 초기화
        Button searchBtn = findViewById(R.id.searchBtn);
        EditText searchWord = findViewById(R.id.searchWord);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchWord.getText().toString();
                RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                adapter.search(keyword, searchOption);
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,BuyPage.class);
                startActivity(intent);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,SellPage.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,ChatListPage.class);
                startActivity(intent);
            }
        });

        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyPage.this,MyPage.class);
                startActivity(intent);
            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Book> bookList = new ArrayList<>();
        private ArrayList<Book> originalBookList = new ArrayList<>();

        public RecyclerViewAdapter() {
            // Firestore 컬렉션 리스너 초기화
            firestore.collection("bookInfo").addSnapshotListener((queryDocumentSnapshots, e) -> {
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

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            View itemView = viewHolder.itemView;

            // 데이터와 뷰를 연결
            TextView bookNameTextView = itemView.findViewById(R.id.name);
            TextView bookAuthorTextView = itemView.findViewById(R.id.person);

            bookNameTextView.setText(bookList.get(holder.getAdapterPosition()).getBookName());
            bookAuthorTextView.setText(bookList.get(holder.getAdapterPosition()).getBookAuthor());

            // 아이템 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭한 아이템의 정보 가져오기
                    String selectedBookName = bookList.get(holder.getAdapterPosition()).getBookName();
                    String selectedBookAuthor = bookList.get(holder.getAdapterPosition()).getBookAuthor();

                    // 다음 페이지로 정보 전달
                    Intent intent = new Intent(itemView.getContext(), BookInfoPage.class);
                    intent.putExtra("bookName", selectedBookName);
                    intent.putExtra("bookAuthor", selectedBookAuthor);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

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
}
