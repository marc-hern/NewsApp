package com.example.marcus.newsapp.data;
import android.provider.BaseColumns;

/**
 * Created by Marcus on 7/28/2017.
 */

public class Contract{
    public static class OBJECT_TABLES implements BaseColumns{
        // Column names for the database
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_DATE = "publishdate";

    }

}
