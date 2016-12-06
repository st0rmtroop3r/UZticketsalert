package study.bionic.uzticketsalert;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import study.bionic.uzticketsalert.db.DbContract;
import study.bionic.uzticketsalert.db.DbHelper;

public class AutoCompleteAdapter extends CursorAdapter implements AdapterView.OnItemClickListener {

    private DbHelper dbHelper;
    private TextView textView;

    public AutoCompleteAdapter(Context context, Cursor c, boolean autoRequery, TextView textView) {
        super(context, c, autoRequery);
        dbHelper = new DbHelper(context);
        this.textView = textView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        text1.setText(cursor.getString(cursor.getColumnIndex(DbContract.FeedEntry.COLUMN_NAME_ENTRY_ID)));
        text2.setText(cursor.getString(cursor.getColumnIndex(DbContract.FeedEntry.COLUMN_NAME_TITLE)));
//        Log.d("BindView", text2.getText().toString());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.autocomplete_list, parent, false);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        String[] columns = {
                DbContract.FeedEntry._ID,
                DbContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                DbContract.FeedEntry.COLUMN_NAME_TITLE
        };
        if (constraint != null) {
            Cursor cursor = rdb.query(
                    DbContract.FeedEntry.TABLE_NAME,
                    columns,
                    DbContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE '%" + constraint.toString() + "%'",
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();

            Log.d("Return cursor", cursor.getString(cursor.getColumnIndex(DbContract.FeedEntry.COLUMN_NAME_TITLE)));
            return cursor;
        }
        Log.d("Return cursor", " null");
        return null;
    }

    @Override
    public String convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FeedEntry.COLUMN_NAME_TITLE));
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        Log.d("onItemClick", view.getTransitionName() + ", " + position + ", " + id);
        // Get the cursor, positioned to the corresponding row in the result set
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);

        // Get the Item Number from this row in the database.
        String itemNumber = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.FeedEntry.COLUMN_NAME_TITLE));
        Log.d("itemNumber", itemNumber);
        // Update the parent class's TextView
        textView.setText(itemNumber);
    }

    public void close()
    {
        dbHelper.close();
    }
}