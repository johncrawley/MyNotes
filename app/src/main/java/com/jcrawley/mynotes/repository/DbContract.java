package com.jcrawley.mynotes.repository;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract(){}

    static class CategoriesEntry implements BaseColumns {
        static final String TABLE_NAME = "Categories";
        static final String COL_CATEGORY_NAME = "name";
    }


    static class FilesEntry implements BaseColumns {
        static final String TABLE_NAME = "Files";
        static final String COL_PATH = "path";
        static final String COL_NAME = "name";
        static final String COL_CATEGORY_ID = "category_id";
    }

}
