package com.jcrawley.mynotes.repository;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract(){}

    static class CategoriesEntry implements BaseColumns {
        static final String TABLE_NAME = "Categories";
        static final String COL_CATEGORY_NAME = "name";
    }


    static class DocumentsEntry implements BaseColumns {
        static final String TABLE_NAME = "Documents";
        static final String COL_PATH = "path";
        static final String COL_NAME = "name";
        static final String COL_CATEGORY_ID = "category_id";
    }


    static class DocumentLinesEntry implements BaseColumns {
        static final String TABLE_NAME = "Document_lines";
        static final String COL_DOCUMENT_ID = "document_id";
        static final String COL_CONTENTS = "contents";
    }

}
