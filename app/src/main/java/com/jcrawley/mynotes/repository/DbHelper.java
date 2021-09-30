package com.jcrawley.mynotes.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance;


    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Quiz.db";

    private static final String OPENING_BRACKET = " (";
    private static final String CLOSING_BRACKET = " );";
    private static final  String INTEGER = " INTEGER";
    private static final String TEXT = " TEXT";
    private static final String BLOB = " BLOB";
    private static final String COMMA = ",";
    public static final String UNIQUE = " UNIQUE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";


    private static final String SQL_CREATE_CATEGORIES_TABLE =
            CREATE_TABLE_IF_NOT_EXISTS + DbContract.CategoriesEntry.TABLE_NAME + OPENING_BRACKET +
                    DbContract.CategoriesEntry._ID                          + INTEGER + PRIMARY_KEY + COMMA +
                    DbContract.CategoriesEntry.COL_CATEGORY_NAME   + TEXT + CLOSING_BRACKET;

    private static final String SQL_CREATE_FILES_TABLE =
            CREATE_TABLE_IF_NOT_EXISTS + DbContract.FilesEntry.TABLE_NAME + OPENING_BRACKET +
                    DbContract.FilesEntry._ID + INTEGER + PRIMARY_KEY + COMMA +
                    DbContract.FilesEntry.COL_NAME + TEXT + UNIQUE +  COMMA +
                    DbContract.FilesEntry.COL_PATH + TEXT + COMMA +
                    DbContract.FilesEntry.COL_CATEGORY_ID +  INTEGER + CLOSING_BRACKET;


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbContract.CategoriesEntry.TABLE_NAME;


    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context){
        if(instance == null){
            instance = new DbHelper(context);
        }
        return instance;
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_FILES_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}