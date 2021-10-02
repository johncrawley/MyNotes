package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcrawley.mynotes.list.ListItem;

import java.util.ArrayList;
import java.util.List;

public class FileRepositoryImpl implements  FileRepository {

    private final SQLiteDatabase db;


    public FileRepositoryImpl(Context context){
        DbHelper dbHelper = DbHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public boolean exists(String filename, long categoryId) {
        String query =  "SELECT * FROM " + DbContract.DocumentsEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentsEntry.COL_NAME + " = "  + inBrackets(filename)
                + " AND " + DbContract.DocumentsEntry.COL_CATEGORY_ID +  " = " + categoryId + ";";
        Cursor cursor = db.rawQuery(query, null);
        boolean doesFileAlreadyExist =  cursor.getCount() != 0;
        cursor.close();
        return doesFileAlreadyExist;
    }


    public String inBrackets(String str){
        return "'" + str + "'";
    }


    @Override
    public boolean create(String filename, long categoryId) {
        if(exists(filename, categoryId)){
            return false;
        }
        DbUtils.addValuesToTable(db, DbContract.DocumentsEntry.TABLE_NAME, createContentValuesFor(categoryId, filename));
        return true;
    }

    @Override
    public List<ListItem> getFiles(long categoryId) {
        List<ListItem> listItems = new ArrayList<>();
        Cursor cursor;
        String query = "SELECT * FROM " + DbContract.DocumentsEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentsEntry.COL_CATEGORY_ID + " = " + categoryId + ";";

        try {
            cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()){
                String text = getString(cursor, DbContract.CategoriesEntry.COL_CATEGORY_NAME);
                long id = getLong(cursor, DbContract.CategoriesEntry._ID);
                listItems.add(new ListItem(text, id));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return listItems;
        }
        cursor.close();
        return  listItems;
    }


    private ContentValues createContentValuesFor(long categoryId, String filename){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DocumentsEntry.COL_NAME, filename);
        contentValues.put(DbContract.DocumentsEntry.COL_CATEGORY_ID, categoryId);
        return contentValues;
    }


    @Override
    public boolean delete(String filename, long categoryId) {
        return false;
    }

    @Override
    public String getFilepath(String filename, long categoryId) {
        return null;
    }




    private String getString(Cursor cursor, String name){
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }


    private long getLong(Cursor cursor, String name){
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }


}
