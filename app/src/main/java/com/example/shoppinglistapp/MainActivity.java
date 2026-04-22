package com.example.shoppinglistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView   rvProducts;
    private ProductAdapter adapter;
    private List<Product>  productList;

    private EditText etProductName;
    private EditText etProductQuantity;
    private Spinner  spinnerCategory;
    private TextView tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja bazy
        dbHelper = new DatabaseHelper(this);

        // Inicjalizacja widoków formularza
        etProductName     = findViewById(R.id.etProductName);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        spinnerCategory   = findViewById(R.id.spinnerCategory);
        tvEmptyMessage    = findViewById(R.id.tvEmptyMessage);

        // Inicjalizacja RecyclerView
        rvProducts  = findViewById(R.id.rvProducts);
        productList = new ArrayList<>();
        adapter     = new ProductAdapter(productList, this::deleteProduct);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        // Listener przycisku DODAJ
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addProduct());

        // Listener przycisku WYCZYŚĆ LISTĘ
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> showClearConfirmDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    /** Wczytuje wszystkie produkty z bazy i odświeża RecyclerView. */
    private void loadProducts() {
        productList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PRODUCTS,
                new String[]{
                        DatabaseHelper.COLUMN_ID,
                        DatabaseHelper.COLUMN_PRODUCT_NAME,
                        DatabaseHelper.COLUMN_QUANTITY,
                        DatabaseHelper.COLUMN_CATEGORY
                },
                null, null, null, null,
                DatabaseHelper.COLUMN_PRODUCT_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            long   id       = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name     = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
            int    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
            productList.add(new Product(id, name, quantity, category));
        }

        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    /** Waliduje formularz, dodaje produkt do bazy i odświeża listę. */
    private void addProduct() {
        String name    = etProductName.getText().toString().trim();
        String qtyText = etProductQuantity.getText().toString().trim();

        // Walidacja: nazwa nie może być pusta
        if (name.isEmpty()) {
            Toast.makeText(this, "Nazwa produktu nie może być pusta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Walidacja: ilość musi być liczbą
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Podaj prawidłową ilość", Toast.LENGTH_SHORT).show();
            return;
        }

        // Walidacja: ilość musi być > 0
        if (quantity <= 0) {
            Toast.makeText(this, "Ilość musi być większa od zera", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();

        long newId = dbHelper.insertProduct(name, quantity, category);
        if (newId == -1) {
            Toast.makeText(this, "Błąd podczas dodawania produktu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Wyczyść pola po dodaniu
        etProductName.setText("");
        etProductQuantity.setText("");
        spinnerCategory.setSelection(0);

        loadProducts();
    }

    /** Usuwa jeden produkt z bazy po kliknięciu przycisku Usuń w wierszu. */
    private void deleteProduct(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
        loadProducts();
    }

    /** Pokazuje AlertDialog z potwierdzeniem usunięcia całej listy. */
    private void showClearConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_confirm))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dbHelper.deleteAllProducts();
                    loadProducts();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .show();
    }

    /** Pokazuje/ukrywa komunikat o pustej liście. */
    private void updateEmptyState() {
        if (tvEmptyMessage == null) return;
        boolean empty = productList.isEmpty();
        rvProducts.setVisibility(empty ? View.GONE  : View.VISIBLE);
        tvEmptyMessage.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) dbHelper.close();
    }
}
