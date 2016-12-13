package study.bionic.uzticketsalert.db;

import android.provider.BaseColumns;

public final class DbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DbContract() {}

    /* Inner class that defines the table contents */
    public static abstract class StationNames implements BaseColumns {
        public static final String TABLE_NAME = "stations";
        public static final String COLUMN_NAME_STATION_ID = "station_id";
        public static final String COLUMN_NAME_TITLE_EN = "title_en";
        public static final String COLUMN_NAME_TITLE_UA = "title_ua";
        public static final String COLUMN_NAME_TITLE_RU = "title_ru";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StationNames.TABLE_NAME + " (" +
                    StationNames._ID + " INTEGER PRIMARY KEY," +
                    StationNames.COLUMN_NAME_STATION_ID + TEXT_TYPE + COMMA_SEP +
                    StationNames.COLUMN_NAME_TITLE_EN + TEXT_TYPE + COMMA_SEP +
                    StationNames.COLUMN_NAME_TITLE_UA + TEXT_TYPE + COMMA_SEP +
                    StationNames.COLUMN_NAME_TITLE_RU + TEXT_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StationNames.TABLE_NAME;
}
