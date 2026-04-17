package com.example.shoppinglistapp;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView contactsRecyclerView;
    private ProductAdapter adapter;
    private List<Product> product;
    private TextView emptyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        contactsRecyclerView = findViewById(R.id.rvProducts);
        Button addContactButton = findViewById(R.id.btnAdd);

        product = new ArrayList<>();

        adapter = new ProductAdapter(product, this::deleteContact);
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        product.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_PRODUCT_NAME,
                DatabaseHelper.COLUMN_QUANTITY,
                DatabaseHelper.COLUMN_CATEGORY
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PRODUCTS,
                projection,
                null, null, null, null,
                DatabaseHelper.COLUMN_PRODUCT_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));

            product.add(new Product(id, name, quantity, category));
        }

        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();

        if (product.isEmpty()) {
            contactsRecyclerView.setVisibility(View.GONE);
            emptyMessageTextView.setVisibility(View.VISIBLE);
        } else {
            contactsRecyclerView.setVisibility(View.VISIBLE);
            emptyMessageTextView.setVisibility(View.GONE);
        }
    }

    public void deleteContact(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

        loadContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}