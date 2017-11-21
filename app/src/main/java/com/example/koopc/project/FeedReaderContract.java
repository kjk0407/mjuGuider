package com.example.koopc.project;

import android.provider.BaseColumns;

/**
 * Created by kooPC on 2017-10-28.
 */

public final class FeedReaderContract {
    public FeedReaderContract(){};


    public static final String SQL_CREATE_CLASS =
            "CREATE TABLE IF NOT EXISTS " + FeedEntry.USER_TABLE_NAME2 + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.USER_COLUMN_NAME_GRID + " INTEGER, " +
                    FeedEntry.USER_COLUMN_NAME_CLASSNAME + " text, " +
                    FeedEntry.USER_COLUMN_NAME_LOCATION + " text, " +
                    FeedEntry.USER_COLUMN_NAME_PROFESSOR + " text, " +
                    FeedEntry.USER_COLUMN_NAME_COLOR + " text, " +
                    FeedEntry.USER_COLUMN_NAME_TIME + " text, " +
                    FeedEntry.USER_COLUMN_NAME_BUILDING + " text, " +
                    FeedEntry.USER_COLUMN_NAME_PERMISSION + " text " +
                    " ) ";


    public static final String SQL_DELETE_CLASS =
            "DROP TABLE IF EXISTS " + FeedEntry.USER_TABLE_NAME2;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String USER_TABLE_NAME2 = "class";
        public static final String USER_COLUMN_NAME_CLASSNAME = "classname"; // 강좌 이름
        public static final String USER_COLUMN_NAME_LOCATION = "location"; // 강의실 위치
        public static final String USER_COLUMN_NAME_PROFESSOR = "professor"; // 담당교수 명
        public static final String USER_COLUMN_NAME_COLOR = "color"; // 각 칸의 색깔
        public static final String USER_COLUMN_NAME_TIME = "time"; // 칸  포지션
        public static final String USER_COLUMN_NAME_BUILDING = "building"; // 칸  포지션
        public static final String USER_COLUMN_NAME_GRID = "grid"; // 칸  포지션
        public static final String USER_COLUMN_NAME_PERMISSION = "permission"; // 데이터가 들어있는지 안 들어있는지

    }
}
