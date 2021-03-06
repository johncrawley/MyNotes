package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcrawley.mynotes.list.ListItem;

import java.util.ArrayList;
import java.util.List;

public class DocumentRepositoryImpl implements DocumentRepository {

    private final SQLiteDatabase db;
    private final DocumentLinesRepository documentLinesRepository;


    public DocumentRepositoryImpl(Context context){
        DbHelper dbHelper = DbHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        documentLinesRepository = new DocumentLinesRepositoryImpl(context);
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
    public long create(String documentName, long categoryId) {

        return exists(documentName, categoryId) ?
                -1
                : DbUtils.addValuesToTable(db, DbContract.DocumentsEntry.TABLE_NAME,
                createContentValuesFor(categoryId, documentName));
    }


    @Override
    public List<ListItem> getDocuments(long categoryId) {
        List<ListItem> listItems = new ArrayList<>();
        Cursor cursor;
        String query = "SELECT * FROM " + DbContract.DocumentsEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentsEntry.COL_CATEGORY_ID + " = " + categoryId + ";";

        try {
            cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()){
                String text = getString(cursor, DbContract.DocumentsEntry.COL_NAME);
                long id = getLong(cursor, DbContract.DocumentsEntry._ID);
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
    public void delete(long documentId) {
        documentLinesRepository.deleteAllWithDocumentId(documentId);
        String deleteDocumentQuery = "DELETE FROM "
                + DbContract.DocumentsEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentsEntry._ID
                + " = "  + documentId
                + ";";
        try {
            db.execSQL(deleteDocumentQuery);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public String getFilepath(String filename, long categoryId) {
        return null;
    }


    @Override
    public void deleteAllWithCategoryId(long categoryId) {
        documentLinesRepository.deleteAllWithCategoryId(categoryId);
        String deleteDocumentQuery = "DELETE FROM "
                + DbContract.DocumentsEntry.TABLE_NAME
                + " WHERE " + DbContract.DocumentsEntry.COL_CATEGORY_ID
                + " = "  + categoryId
                + ";";
        try {
            db.execSQL(deleteDocumentQuery);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    private ContentValues createContentValuesFor(long categoryId, String filename){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DocumentsEntry.COL_NAME, filename);
        contentValues.put(DbContract.DocumentsEntry.COL_CATEGORY_ID, categoryId);
        return contentValues;
    }


    private String getString(Cursor cursor, String name){
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }


    private long getLong(Cursor cursor, String name){
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }


}
