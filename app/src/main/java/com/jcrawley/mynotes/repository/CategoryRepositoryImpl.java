package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jcrawley.mynotes.list.ListItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final SQLiteDatabase db;
    private final DocumentRepository documentRepository;

    public CategoryRepositoryImpl(Context context){
        DbHelper dbHelper = DbHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        documentRepository = new DocumentRepositoryImpl(context);
    }


    @Override
    public long create(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.CategoriesEntry.COL_CATEGORY_NAME, name);
        return DbUtils.addValuesToTable(db, DbContract.CategoriesEntry.TABLE_NAME, contentValues);
    }


    @Override
    public boolean exists(String name) {
        return false;
    }


    @Override
    public List<ListItem> getCategories() {
        List<ListItem> listItems = new ArrayList<>();
        Cursor cursor;
        String query = "SELECT * FROM " + DbContract.CategoriesEntry.TABLE_NAME;

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


    private String getString(Cursor cursor, String name){
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }


    private long getLong(Cursor cursor, String name){
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }


    @Override
    public boolean delete(long categoryId) {
        documentRepository.deleteAllWithCategoryId(categoryId);
        String deleteDocumentQuery = "DELETE FROM "
                + DbContract.CategoriesEntry.TABLE_NAME
                + " WHERE " + DbContract.CategoriesEntry._ID
                + " = "  + categoryId
                + ";";
        try {
            db.execSQL(deleteDocumentQuery);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
