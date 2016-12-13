package study.bionic.uzticketsalert.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import study.bionic.uzticketsalert.R;
import study.bionic.uzticketsalert.db.DbContract;
import study.bionic.uzticketsalert.db.DbHelper;
import study.bionic.uzticketsalert.entity.Station;

public class AutoCompleteAdapter extends CursorAdapter implements AdapterView.OnItemClickListener {

    private DbHelper dbHelper;
    private Station station;
    private Context context;
    TextInputLayout inputLayout;

    public AutoCompleteAdapter(Context context, Cursor c, boolean autoRequery, Station station, TextInputLayout inputLayout) {
        super(context, c, autoRequery);
        dbHelper = DbHelper.getInstance(context);
        this.station = station;
        this.context = context;
        this.inputLayout = inputLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        text1.setText(cursor.getString(cursor.getColumnIndex(DbContract.StationNames.COLUMN_NAME_STATION_ID)));
        text2.setText(cursor.getString(cursor.getColumnIndex(DbContract.StationNames.COLUMN_NAME_TITLE_EN)));
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
                DbContract.StationNames._ID,
                DbContract.StationNames.COLUMN_NAME_STATION_ID,
                DbContract.StationNames.COLUMN_NAME_TITLE_EN
        };
        if (constraint != null) {
            Cursor cursor = rdb.query(
                    DbContract.StationNames.TABLE_NAME,
                    columns,
//                    DbContract.StationNames.COLUMN_NAME_TITLE_EN + " LIKE '%" + constraint.toString() + "%'",
                    DbContract.StationNames.COLUMN_NAME_TITLE_EN + " LIKE '" + constraint.toString() + "%'",
                    null,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() == 0) {
                cursor = rdb.query(
                        DbContract.StationNames.TABLE_NAME,
                        columns,
                        DbContract.StationNames.COLUMN_NAME_TITLE_EN + " LIKE '%" + constraint.toString() + "%'",
//                        DbContract.StationNames.COLUMN_NAME_TITLE_EN + " LIKE '" + constraint.toString() + "%'",
                        null,
                        null,
                        null,
                        null
                );
            }

            cursor.moveToFirst();

            Log.d("Return cursor", cursor.getString(cursor.getColumnIndex(DbContract.StationNames.COLUMN_NAME_TITLE_EN)));
            return cursor;
        }
        Log.d("Return cursor", " null");
        return null;
    }

    @Override
    public String convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StationNames.COLUMN_NAME_TITLE_EN));
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        Log.d("onItemClick", "" + position + ", " + id);
        // Get the cursor, positioned to the corresponding row in the result set
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);

        // Get the Item Number from this row in the database.
        String stationName = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StationNames.COLUMN_NAME_TITLE_EN));
        String stationId = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.StationNames.COLUMN_NAME_STATION_ID));
        Log.d("stationName", stationName);
        station.setName(stationName);
        station.setId(stationId);
        Log.d("station.getName()", station.getName());

        // Remove hint error
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);

        // Hide soft keyboard
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

        // Remove focus from autocomplete
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(R.id.activity_main);
        rootView.requestFocus();

        // Update the parent class's TextView
//        textView.setText(stationName);
    }

    public void close()
    {
        dbHelper.close();
    }
}