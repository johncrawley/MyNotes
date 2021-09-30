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


    public static class AnswerPoolNamesEntry implements BaseColumns {
        public static final String TABLE_NAME = "answer_pool_names";
        public static final String COLUMN_NAME_APOOL_NAME = "name";
    }

    public static class AnswerPoolItemsEntry implements BaseColumns {

        public static final String TABLE_NAME = "answer_pool_items";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_APOOL_ID = "answer_pool_ID";
        public static final String COLUMN_NAME_U_VAL = "u_val";
    }


    public static class QuestionGeneratorEntry implements BaseColumns {
        public static final String TABLE_NAME = "question_generators";
        public static final String COLUMN_NAME_GENERATOR_NAME = "name";
        public static final String COLUMN_NAME_QUESTION_PACK_NAME = "question_pack_name";
    }

    public static class QuestionGeneratorSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "question_generator_sets";
        public static final String COLUMN_NAME_GENERATOR_ID = "generator_id";
        public static final String COLUMN_NAME_SET_NAME = "name";
        public static final String COLUMN_NAME_QUESTION_TEMPLATE= "question_template";
    }

    public static class QuestionGeneratorChunkEntry implements BaseColumns {
        public static final String TABLE_NAME = "q_generator_chunks";
        public static final String COLUMN_NAME_QUESTION_SET_ID = "question_set_id";
        public static final String COLUMN_NAME_SUBJECT = "q_subject";
        public static final String COLUMN_NAME_ANSWER= "q_answer";
        public static final String COLUMN_NAME_TRIVIA= "q_trivia";
    }

}
