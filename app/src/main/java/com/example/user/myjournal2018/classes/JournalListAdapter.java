package com.example.user.myjournal2018.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myjournal2018.R;
import com.example.user.myjournal2018.activities.DetailsActivity;
import com.example.user.myjournal2018.activities.EditActivity;


public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder> {

    private Cursor journalCursor;
    private SQLiteDatabase mDb;
    private String clickedID;
    private RecyclerView journalRecyclerView;
    private Context journalContext;


    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     * @param cursor  the db cursor with waitlist data to display
     */
    public JournalListAdapter(Context context, Cursor cursor, RecyclerView recycler) {
        this.journalContext = context;
        this.journalCursor = cursor;
        this.journalRecyclerView = recycler;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(journalContext);
        View view = inflater.inflate(R.layout.journal_list_item, parent, false);
        return new JournalViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final JournalViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Move the Cursor to the position of the item to be displayed
        int i=holder.getAdapterPosition();
        if (!journalCursor.moveToPosition(i))
            return; // bail if returned null


        // Update the view holder with the information needed to display

        String date = journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DATE));
        String mTitle = journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));
        String mContent = journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_CONTENT));
        // String hour=journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_HOUR));

        //  Retrieve the id from the cursor and
        long id = journalCursor.getLong(journalCursor.getColumnIndex(JournalContract.JournalEntry._ID));

        //  Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);

        //display entry title
        holder.titleTextView.setText(mTitle);

        //display entry DATE
        holder.dateTextView.setText(date);

        // Display the entry  content
        holder.contentTextView.setText(mContent);


        holder.imgDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(holder.imgDots);

            }
        });


        //LISTENER SUR UN ELEMENT DU HOLDER

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(journalContext, DetailsActivity.class);

                int id = position;

                journalCursor.moveToPosition(id);

                clickedID = journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry._ID));
                // String clickedName = rdvCursor.getString(rdvCursor.getColumnIndex(ContactContract.WaitlistEntry.COLUMN_CONTACT_NAME));

                //Log.i("positon-of-clicked-item", clickedID);
                mIntent.putExtra("clickedID", clickedID);


                journalContext.startActivity(mIntent);
                //affichage du detail dinformations

            }

        });
    }


    @Override
    public int getItemCount() {
        return journalCursor.getCount();
    }


    public void removeItem(int position) {
        //journalRecyclerView.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }


    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (journalCursor != null) journalCursor.close();
        journalCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    private void showPopup(View view) {
        // pass the imageview id
        PopupMenu popup = new PopupMenu(journalContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();

    }

    /**
     * delete record
     **/
    private void removeRecord(String id, Context context, JournalDbHelper dbHelper) {
        //Open the database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.execSQL("DELETE FROM " + JournalContract.JournalEntry.TABLE_NAME + " WHERE _id='" + id + "'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    public void updateRecord(String id, Context context, JournalDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE  " + JournalContract.JournalEntry.TABLE_NAME + " SET date ='" + JournalContract.JournalEntry.COLUMN_DATE + "', title ='" + JournalContract.JournalEntry.COLUMN_TITLE + "', content ='" + JournalContract.JournalEntry.COLUMN_CONTENT + "', time ='" + JournalContract.JournalEntry.COLUMN_TIMESTAMP + "'  WHERE _id='" + id + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class JournalViewHolder extends RecyclerView.ViewHolder {

        // Will display the entry date
        TextView dateTextView;

        // Will display the entry hour
        // TextView hourTextView;

        // Will display the entry title
        TextView titleTextView;

        //will display the entry content
        TextView contentTextView;

        // Will display ther entry IconeTextView
        TextView entryIconeTextView;


        ImageView imgDots;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link JournalListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        private JournalViewHolder(View itemView) {
            super(itemView);
            entryIconeTextView = (TextView) itemView.findViewById(R.id.journal_icone_text_view);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            contentTextView = (TextView) itemView.findViewById(R.id.content);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
            this.imgDots = (ImageView) itemView.findViewById(R.id.id_image_dots);
        }

    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:
                    Intent intent = new Intent(journalContext, EditActivity.class);


                    int elmentIdToEdit = journalCursor.getInt(journalCursor.getColumnIndex(JournalContract.JournalEntry._ID));

                    journalCursor.moveToPosition(elmentIdToEdit);
                    intent.putExtra("clickedID", elmentIdToEdit);

                    journalContext.startActivity(intent);
                    Toast.makeText(journalContext, "Edit this entry", Toast.LENGTH_SHORT).show();
                    return true;


                case R.id.delete:
                    final JournalDbHelper dbHelper;
                    dbHelper = new JournalDbHelper(journalContext);

                    // Keep a reference to the mDb until paused or killed. Get a writable database
                    // because you will be adding restaurant customers
                    mDb = dbHelper.getWritableDatabase();
                    //Toast.makeText(rdvContext, "Delete this rdv ", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(journalContext);
                    builder.setMessage("Are you sure to delete this entry?");
                    builder.setCancelable(true);


                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    JournalDbHelper journalDbHelper = new JournalDbHelper(journalContext);

                                    clickedID = journalCursor.getString(journalCursor.getColumnIndex(JournalContract.JournalEntry._ID));


                                    removeRecord(clickedID, journalContext, journalDbHelper);
                                    JournalListAdapter adapter = new JournalListAdapter(journalContext, journalCursor, journalRecyclerView);

                                    journalRecyclerView.setAdapter(adapter);
                                    new JournalListAdapter(journalContext, journalCursor, journalRecyclerView);
                                    adapter.notifyItemRangeRemoved(Integer.valueOf(clickedID), getItemCount());
                                    //journalCursor.moveToPosition(clickedID);
                                    //notifyItemRemoved(clickedID);
                                    //notifyItemRangeChanged(clickedID, journalCursor.getCount());
                                    notifyDataSetChanged();
                                    Toast.makeText(journalContext, "Deleted successfully", Toast.LENGTH_SHORT).show();


                                    mDb.close();
                                }
                            });

                    builder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                }
                            });

                    AlertDialog alert11 = builder.create();
                    alert11.show();
                    return true;
                default:


            }
            return false;
        }

    }
}






