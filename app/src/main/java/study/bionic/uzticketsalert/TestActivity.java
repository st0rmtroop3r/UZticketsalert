package study.bionic.uzticketsalert;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import study.bionic.uzticketsalert.db.DbContract;
import study.bionic.uzticketsalert.db.DbHelper;

/**
 * Created by admin on 27.11.2016.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView testView1 = (TextView) findViewById(R.id.testView1);
        TextView testView2 = (TextView) findViewById(R.id.testView2);
        TextView testView3 = (TextView) findViewById(R.id.testView3);

//        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
//            @Override
//            public Cursor runQuery(CharSequence charSequence) {
//                return null;
//            }
//        };


        DbHelper mDbHelper = new DbHelper(this);

        SQLiteDatabase rdb = mDbHelper.getReadableDatabase();
        String[] columns = {
                DbContract.FeedEntry._ID,
                DbContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                DbContract.FeedEntry.COLUMN_NAME_TITLE
        };
        Cursor cursor = rdb.query(
                DbContract.FeedEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        try {
            int cID = cursor.getColumnIndexOrThrow(DbContract.FeedEntry._ID);
            int cEntryID = cursor.getColumnIndexOrThrow(DbContract.FeedEntry.COLUMN_NAME_ENTRY_ID);
            int cTitle = cursor.getColumnIndexOrThrow(DbContract.FeedEntry.COLUMN_NAME_TITLE);
            String result = "Cursor result:";
            while (cursor.moveToNext()) {
                result += "\n"
                        + cursor.getInt(cID) + " | "
                        + cursor.getString(cEntryID) + " | "
                        + cursor.getString(cTitle);
            }
            testView3.setText(result);
        } finally {
            cursor.close();
        }

        AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.fromStation);
        AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.toStation);

        AutoCompleteAdapter fromAdapter = new AutoCompleteAdapter(this, null, true, testView1);
        AutoCompleteAdapter toAdapter = new AutoCompleteAdapter(this, null, true, testView2);

        from.setAdapter(fromAdapter);
        to.setAdapter(toAdapter);
        from.setOnItemClickListener(fromAdapter);
        to.setOnItemClickListener(toAdapter);

    }
}
