package com.example.shoppinglistapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nazwa i wersja bazy danych
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    // Nazwa tabeli i jej kolumn
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category";

    // Zapytanie SQL do stworzenia tabeli
    private static final String TABLE_CREATE =
            "CREATE TABLE products (" +
                    "    _id INTEGER PRIMARY KEY," +
                    "    name TEXT NOT NULL," +
                    "    quantity INTEGER NOT NULL," +
                    "    category TEXT DEFAULT 'Inne');";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Wykonanie zapytania tworzącego tabelę
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Przy prostej aktualizacji można usunąć starą tabelę i stworzyć nową
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
}