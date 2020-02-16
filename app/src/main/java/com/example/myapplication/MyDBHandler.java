package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_PRIX = "prix";
    public static final String COLUMN_CODE = "code";


    public MyDBHandler(Context context, String name,
                       CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_PRIX + " INTEGER," + COLUMN_CODE + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);

    }

    public boolean deleteProduct(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME  +" like '%"+productname+"%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            product.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    public ArrayList<String> findProduct (String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME +" like '%"+productname+"%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> fileName = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do
                {
                   Product p = new Product();

                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME));
                    String prix = cursor.getString(cursor.getColumnIndex(COLUMN_PRIX));

                    fileName.add(name);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            fileName = null;
        }
        db.close();
        return fileName;
    }

    public void addProductwithcode(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.get_productname());
        values.put(COLUMN_PRIX, product.getPrix());
        values.put(COLUMN_CODE,product.getCode());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.get_productname());
        values.put(COLUMN_PRIX, product.getPrix());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }
    public Product findProductdatil(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME +" like '%"+productname+"%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product fileName = new Product();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do
                {
                    fileName.set_productname(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME)));
                    fileName.setPrix(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRIX))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            fileName = null;
        }
        db.close();
        return fileName;
    }
    public Product findProductdatilwithcode(String productcode) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_CODE +" = "+productcode+"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product fileName = new Product();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do
                {
                    fileName.set_productname(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME)));
                    fileName.setPrix(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRIX))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            fileName = null;
        }
        db.close();
        return fileName;
    }

    public Product findProductdatilwithname(String productcname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME +" = '"+productcname+"'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product fileName = new Product();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do
                {
                    fileName.set_productname(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME)));
                    fileName.setPrix(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRIX))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            fileName = null;
        }
        db.close();
        return fileName;
    }

}