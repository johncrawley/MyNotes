package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final SQLiteDatabase db;


    public CategoryRepositoryImpl(Context context){
        DbHelper dbHelper = DbHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public boolean create(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.CategoriesEntry.COL_CATEGORY_NAME, name);
        addValuesToTable(DbContract.CategoriesEntry.TABLE_NAME, contentValues);

        return false;
    }

    @Override
    public boolean exists(String name) {
        return false;
    }

    @Override
    public List<String> getFiles(String name) {
        return null;
    }

    @Override
    public boolean delete(String name) {
        return false;
    }


    long addValuesToTable(String tableName, ContentValues contentValues){
        db.beginTransaction();
        long id = -1;
        try {
            id = db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.endTransaction();
        return id;
    }

}
