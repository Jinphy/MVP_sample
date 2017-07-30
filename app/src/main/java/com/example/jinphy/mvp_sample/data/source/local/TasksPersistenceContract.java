package com.example.jinphy.mvp_sample.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by jinphy on 2017/7/30.
 */

public final class TasksPersistenceContract {

    private TasksPersistenceContract(){}

    public static abstract class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRPITION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }






}
