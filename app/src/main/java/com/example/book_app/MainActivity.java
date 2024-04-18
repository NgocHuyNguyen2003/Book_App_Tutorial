package com.example.book_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDatabaseHelper;
    ArrayList<String> book_id,book_title, book_author, book_pages;

    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById((R.id.add_button));
        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        myDatabaseHelper = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();//0
        book_title = new ArrayList<>();//1
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        store_all_data_inArrays();
        myAdapter=new MyAdapter(MainActivity.this,book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(myAdapter);//gán vào recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//display

    }
    void store_all_data_inArrays(){
        Cursor cursor = myDatabaseHelper.read_display_AllData();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }
    }
}
