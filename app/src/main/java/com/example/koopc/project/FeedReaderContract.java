package com.example.koopc.project;

import android.provider.BaseColumns;

/**
 * Created by kooPC on 2017-10-28.
 */

public final class FeedReaderContract {

    public FeedReaderContract(){};

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.USER_TABLE_NAME + " ( " +
                    FeedEntry._ID + "INTEGER PRIMARY KEY," +
                    FeedEntry.USER_COLUMN_NAME_ID + " text," +
                    FeedEntry.USER_COLUMN_NAME_PASSWORD + " text," +
                    FeedEntry.USER_COLUMN_NAME_STUDENTID + " text," + " ) ";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.USER_TABLE_NAME;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String USER_TABLE_NAME = "user_table";
        public static final String USER_COLUMN_NAME_ID = "user_id";
        public static final String USER_COLUMN_NAME_PASSWORD = "user_password";
        public static final String USER_COLUMN_NAME_STUDENTID = "user_studentid";
    }
}
