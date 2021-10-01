package com.jcrawley.mynotes.repository;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbUtils {


    static long addValuesToTable(SQLiteDatabase db, String tableName, ContentValues contentValues){
        long id = -1;
        db.beginTransaction();
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
