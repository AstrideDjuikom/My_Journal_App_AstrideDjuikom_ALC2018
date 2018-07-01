package com.example.user.myjournal2018.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "myJounal.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 6;

    // Constructor
    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold JOURNAL data

        String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE " + JournalContract.JournalEntry.TABLE_NAME + " ("
                + JournalContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                JournalContract.JournalEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_HOUR + " TEXT NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                "); ";


        db.execSQL(SQL_CREATE_JOURNAL_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JournalContract.JournalEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
