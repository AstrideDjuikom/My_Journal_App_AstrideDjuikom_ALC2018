package com.example.user.myjournal2018.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.myjournal2018.R;
import com.example.user.myjournal2018.classes.JournalContract;
import com.example.user.myjournal2018.classes.JournalDbHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {


    String clickedEventId, date, time,content,title;
    public SQLiteDatabase mDb;
    private ContentResolver contentResolver;


    public final String TAG = DetailsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ArrayList<String> eventDetails;

        eventDetails = new ArrayList<>();

        JournalDbHelper dbHelper = new JournalDbHelper(this);
        mDb = dbHelper.getReadableDatabase();
        contentResolver = getContentResolver();


        Intent eventIntent = getIntent();

        if (eventIntent != null) {

            clickedEventId = eventIntent.getStringExtra("clickedID");
            Cursor eventCursor = getEventById(clickedEventId);
            if (eventCursor.moveToFirst()) {
                do {
                    try {
                         date = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));
                        eventDetails.add(date);

                         title = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));
                        eventDetails.add(title);

                         content = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_CONTENT));
                        eventDetails.add(content);

                         time = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_HOUR));
                        eventDetails.add(time);

                    } finally {
                        eventCursor.close();
                    }
                } while (eventCursor.moveToNext());
            }


            ListView j_detail_listview = (ListView) findViewById(R.id.rdv_detail_listView);
            ArrayAdapter<String> j_detail_adapter = new ArrayAdapter<String>(DetailsActivity.this, android.R.layout.simple_list_item_1, eventDetails);
            j_detail_listview.setAdapter(j_detail_adapter);


        }


    }


    public Cursor getEventById(String clickedEventId) {

        return mDb.query(JournalContract.JournalEntry.TABLE_NAME, null, JournalContract.JournalEntry._ID + " = " + clickedEventId, null, null, null, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            String string= date+ title+ content +time;
            shareContent(string);

            return true;
        }

        return super.onOptionsItemSelected(item);


    }


    private void shareContent(String textToShare) {
        String mimeType = "text/plain";
        /* This is just the title of the window that will pop up when we call startActivity */
        String title = "Learning How to Share";
        /* ShareCompat.IntentBuilder provides a fluent API for creating Intents */
        ShareCompat.IntentBuilder
                /* This from method specifies the Context from which this share is coming from */
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();


    }
}

