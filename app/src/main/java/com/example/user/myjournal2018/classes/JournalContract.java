package com.example.user.myjournal2018.classes;

import android.provider.BaseColumns;

public class JournalContract {
    public static final class JournalEntry implements BaseColumns {

        public static final String TABLE_NAME = "myjournal";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT  = "content";
        public static final String COLUMN_TIMESTAMP  ="timestamp";


    }
}
