package com.example.user.myjournal2018.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.myjournal2018.R;
import com.example.user.myjournal2018.classes.JournalContract;
import com.example.user.myjournal2018.classes.JournalDbHelper;

import java.util.Calendar;
import java.util.TimeZone;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public SQLiteDatabase mDb;
    public Button btnSaveToEntry, btnCancel;
    public EditText entryDateEdit;
    public EditText entryHourEdit;
    public EditText entryTitleEdit;
    public EditText entryContentEdit;
    int clickedEventId;
    private String date, time, content, title;

    private int _day;
    private int _month;
    private int _year;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        JournalDbHelper dbHelper = new JournalDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        entryDateEdit = (EditText) findViewById(R.id.entry_date_edit_text);
        entryHourEdit = (EditText) findViewById(R.id.entry_hour_edit_text);
        entryTitleEdit = (EditText) findViewById(R.id.entry_title_edit_text);
        entryContentEdit = (EditText) findViewById(R.id.entry_content_edit_text);
        btnSaveToEntry = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        entryDateEdit.setOnClickListener(this);


        Intent eventIntent = getIntent();

        if (eventIntent.hasExtra("clickedID")) {

            clickedEventId = eventIntent.getIntExtra("clickedID", 0);
            Cursor eventCursor = getEventById(String.valueOf(clickedEventId));

            if (eventCursor.moveToFirst()) {
                try {

                    date = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));


                    title = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));


                    content = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_CONTENT));


                    time = eventCursor.getString(eventCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_HOUR));


                } finally {
                    eventCursor.close();
                }

            }

            entryDateEdit.setText(date);
            entryContentEdit.setText(content);
            entryHourEdit.setText(time);
            entryTitleEdit.setText(title);


        }

        entryHourEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 12 && selectedHour >= 0) {
                            entryHourEdit.setText(selectedHour + ":" + selectedMinute + " AM ");
                        } else {
                            selectedHour -= 12;
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            entryHourEdit.setText(selectedHour + ":" + selectedMinute + " PM ");

                            // rdvHourEdit.setText(selectedHour + ":" + selectedMinute);
                        }
                    }

                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        btnSaveToEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JournalDbHelper dbHelper = new JournalDbHelper(EditActivity.this);
                // updateRecord(clickedEventId,dbHelper,EditActivity.this ,date,title,content,time);

                Toast.makeText(getApplicationContext(), "Updated successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }


    public Cursor getEventById(String clickedEventId) {

        return mDb.query(JournalContract.JournalEntry.TABLE_NAME, null, JournalContract.JournalEntry._ID + " = " + clickedEventId, null, null, null, null);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDisplay() {

        entryDateEdit.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_year).append(" "));

    }

   /* public void updateRecord(int id ,JournalDbHelper dbHelper,Context context,String date,String title,String content,String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE" +JournalContract.JournalEntry.TABLE_NAME +"SET"+ JournalContract.JournalEntry.COLUMN_DATE + =
                        date +JournalContract.JournalEntry.COLUMN_TITLE + = title + JournalContract.JournalEntry.COLUMN_CONTENT+ =content
                + JournalContract.JournalEntry.COLUMN_HOUR+ = time +" WHERE _ID='" + id + "'");

        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();

    }*/


}
