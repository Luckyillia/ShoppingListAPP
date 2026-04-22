package com.example.shoppinglistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Klasa odpowiedzialna za obsługę lokalnej bazy danych SQLite.
 * Przechowuje listę zakupów z nazwą, ilością i kategorią produktu.
 *
 * Imię i Nazwisko: Jan Kowalski
 * PESEL: 00000000000
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "products.db";
    private static final int    DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTS     = "products";
    public static final String COLUMN_ID           = "_id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_QUANTITY     = "quantity";
    public static final String COLUMN_CATEGORY     = "category";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_ID           + " INTEGER PRIMARY KEY, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            COLUMN_QUANTITY     + " INTEGER NOT NULL, " +
            COLUMN_CATEGORY     + " TEXT DEFAULT 'Inne');";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    /**
     * Wstawia nowy produkt do bazy danych.
     * @return nowe _id rekordu lub -1 w razie błędu
     */
    public long insertProduct(String name, int quantity, String category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_QUANTITY,     quantity);
        values.put(COLUMN_CATEGORY,     category);

        long newId = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return newId;
    }

    /**
     * Usuwa wszystkie produkty z tabeli.
     */
    public void deleteAllProducts() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
    }
}
