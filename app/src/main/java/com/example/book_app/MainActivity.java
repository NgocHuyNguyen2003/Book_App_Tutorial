package com.example.book_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    Button delete_button1;
    MyAdapter myAdapter;
    ImageView empty_imageview;
    TextView no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delete_button1 = findViewById(R.id.delete_all1);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data=findViewById(R.id.no_data);


        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById((R.id.add_button));
        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        delete_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog1();
            }
        });


        myDatabaseHelper = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();//0
        book_title = new ArrayList<>();//1
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        store_all_data_inArrays();
        myAdapter=new MyAdapter(MainActivity.this,this, book_id, book_title, book_author,
                book_pages);
        recyclerView.setAdapter(myAdapter);//gán vào recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//display

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            recreate();
        }
    }//generate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//delete menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    void store_all_data_inArrays(){
        Cursor cursor = myDatabaseHelper.read_display_AllData();
        if(cursor.getCount() == 0)
        {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }
    void confirmDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
