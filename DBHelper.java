package com.example.myapplication;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GroceryDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "grocery";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_COST = "cost";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ITEM + " TEXT PRIMARY KEY, " +
                COLUMN_COST + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addItem(String item, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM, item);
        values.put(COLUMN_COST, cost);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getItems() {
        ArrayList<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            String item = cursor.getString(0) + " - ₹" + cursor.getDouble(1);
            itemList.add(item);
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public double getTotalCost(ArrayList<String> selectedItems) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        for (String item : selectedItems) {
            String[] parts = item.split(" - ₹");
            String itemName = parts[0];
            Cursor cursor = db.rawQuery("SELECT cost FROM " + TABLE_NAME + " WHERE item=?", new String[]{itemName});
            if (cursor.moveToFirst()) {
                total += cursor.getDouble(0);
            }
            cursor.close();
        }
        db.close();
        return total;
    }
}