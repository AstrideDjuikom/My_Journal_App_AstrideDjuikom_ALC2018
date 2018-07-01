package com.example.user.myjournal2018.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class AddEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public SQLiteDatabase mDb;

    public Button btnAddToEntryList;
    public EditText entryDateEdit;
    public EditText entryHourEdit;
    public EditText entryTitleEdit;
    public EditText entryContentEdit;

    public Calendar myCalendar;

    private int _day;
    private int _month;
    private int _year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);


        JournalDbHelper dbHelper = new JournalDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        entryDateEdit = (EditText) findViewById(R.id.entry_date_edit_text);
        entryHourEdit = (EditText) findViewById(R.id.entry_hour_edit_text);
        entryTitleEdit = (EditText) findViewById(R.id.entry_title_edit_text);
        entryContentEdit = (EditText) findViewById(R.id.entry_content_edit_text);
        btnAddToEntryList = (Button) findViewById(R.id.btn_add_to_entrylist);

        entryDateEdit.setOnClickListener(this);





        btnAddToEntryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entryDateEdit.getText().length() == 0 ||
                        entryContentEdit.getText().length() == 0
                        ) {
                    return;
                }


                addNewEntry(entryDateEdit.getText().toString(), entryTitleEdit.getText().toString(),
                        entryContentEdit.getText().toString(),entryHourEdit.getText().toString());


                Toast mtoast = Toast.makeText(getApplicationContext(), "Insertion reussi...", Toast.LENGTH_SHORT);
                mtoast.show();

                //clear ui text fields

                entryDateEdit.getText().clear();
                entryHourEdit.getText().clear();
                entryTitleEdit.getText().clear();
                entryContentEdit.getText().clear();


                startActivity(new Intent(AddEntryActivity.this, MainActivity.class));

            }
        });
        entryHourEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
    }





        private long addNewEntry(String date, String title, String content, String time) {
        ContentValues cv = new ContentValues();
        cv.put(JournalContract.JournalEntry.COLUMN_DATE, date);
        cv.put(JournalContract.JournalEntry.COLUMN_TITLE, title);
        cv.put(JournalContract.JournalEntry.COLUMN_CONTENT, content);
        cv.put(JournalContract.JournalEntry.COLUMN_HOUR, time);

        return mDb.insert(JournalContract.JournalEntry.TABLE_NAME, null, cv);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }


    @Override
    public void onClick (View v){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDisplay() {

        entryDateEdit.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_year).append(" "));

    }
}
