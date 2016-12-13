package study.bionic.uzticketsalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DB.db";
    private Context context;
    private static DbHelper sInstance;

    public static DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context);
        }
        return sInstance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_ENTRIES);
        fillTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and searchTrains over
        db.execSQL(DbContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void fillTable(SQLiteDatabase db) {

        CsvHandler csv = new CsvHandler(context);
        List<String[]> stations = csv.readStationsCsv();

        if (stations == null) {
            Log.d("readStationsCsv", "returned null");
            return;
        }

        long result = 0;

//        db = this.getWritableDatabase();
        ContentValues values;
        for (String[] station : stations) {
            values = new ContentValues(4);
            values.put(DbContract.StationNames.COLUMN_NAME_STATION_ID, station[0]);
            values.put(DbContract.StationNames.COLUMN_NAME_TITLE_EN, station[1]);
            values.put(DbContract.StationNames.COLUMN_NAME_TITLE_UA, station[2]);
            values.put(DbContract.StationNames.COLUMN_NAME_TITLE_RU, station[3]);
            result = db.insert(DbContract.StationNames.TABLE_NAME,"", values);
        }
        Log.d("fillTable finished", "" + result + "added");
    }
}
