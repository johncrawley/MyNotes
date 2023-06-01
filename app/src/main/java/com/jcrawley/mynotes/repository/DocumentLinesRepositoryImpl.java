package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
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
                String text = getString(cursor, DbContract.DocumentLinesEntry.COL_CONTENTS);
                long entryId = getLong(cursor, DbContract.DocumentLinesEntry._ID);
                listItems.add(new ListItem(text, entryId));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return listItems;
        }
        cursor.close();
        return listItems;
    }


    @Override
    public void add(String contents, long documentId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DocumentLinesEntry.COL_DOCUMENT_ID, documentId);
        contentValues.put(DbContract.DocumentLinesEntry.COL_CONTENTS, contents);
        DbUtils.addValuesToTable(db, DbContract.DocumentLinesEntry.TABLE_NAME, contentValues);
    }


    @Override
    public void update(long lineId, long documentId, String contents) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DocumentLinesEntry.COL_DOCUMENT_ID, documentId);
        contentValues.put(DbContract.DocumentLinesEntry.COL_CONTENTS, contents);

        String[] args = {String.valueOf(lineId)};

        try {
            db.update(DbContract.DocumentLinesEntry.TABLE_NAME,
                   contentValues, DbContract.DocumentLinesEntry._ID + " = ?", args);
        }
        catch(SQLException e){
            System.out.println("^^^ Error updating document line");
            System.out.println("^^^ " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void delete(long lineId) {
        System.out.println("Deleting document line with lineID : " + lineId);
        String query = "DELETE FROM "
                + DbContract.DocumentLinesEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentLinesEntry._ID
                + " = "  + lineId
                + ";";
        try {
            db.execSQL(query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public void deleteAllWithDocumentId(long documentId) {
        String query = "DELETE FROM "
                + DbContract.DocumentLinesEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentLinesEntry.COL_DOCUMENT_ID
                + " = "  + documentId
                + ";";
        try {
            db.execSQL(query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public void deleteAllWithCategoryId(long categoryId) {
        try {
            db.delete(DbContract.DocumentLinesEntry.TABLE_NAME,
                    "document_id IN ( SELECT _id FROM Documents WHERE category_id = ? );",
                    new String[]{String.valueOf(categoryId)});
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    private String getString(Cursor cursor, String name){
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }


    private long getLong(Cursor cursor, String name){
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }

}
