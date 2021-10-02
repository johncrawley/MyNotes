package com.jcrawley.mynotes.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcrawley.mynotes.list.ListItem;

import java.util.ArrayList;
import java.util.List;

public class DocumentLinesRepositoryImpl implements DocumentLinesRepository{


    private final SQLiteDatabase db;


    public DocumentLinesRepositoryImpl(Context context){
        DbHelper dbHelper = DbHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public List<ListItem> getDocumentLines(long documentId) {
        List<ListItem> listItems = new ArrayList<>();
        Cursor cursor;
        String query = "SELECT * FROM " + DbContract.DocumentLinesEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentLinesEntry.COL_DOCUMENT_ID + " = "  + documentId + ";";

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

    @Override
    public void add(String contents) {

    }

    @Override
    public void edit(long id, String contents) {

    }

    @Override
    public void delete(long id) {

    }




    private String getString(Cursor cursor, String name){
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }


    private long getLong(Cursor cursor, String name){
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }

}
