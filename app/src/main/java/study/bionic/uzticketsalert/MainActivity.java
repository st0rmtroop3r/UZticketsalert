package study.bionic.uzticketsalert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.RawRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import study.bionic.uzticketsalert.db.CsvHandler;
import study.bionic.uzticketsalert.db.DbHelper;

public class MainActivity extends AppCompatActivity {
    private DbHelper mDBAdabter;  // database adapter / helper
    private study.bionic.uzticketsalert.AutoCompleteAdapter mCursorAdapter;  // cursor adapter
    private Cursor mItemCursor;    // and the cursor itself
    private static Button btnTime;
    private static Button btnDate1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView testView1 = (TextView) findViewById(R.id.testView1);
        TextView testView2 = (TextView) findViewById(R.id.testView2);
        TextView testView3 = (TextView) findViewById(R.id.testView3);

        btnTime = (Button) findViewById(R.id.btnTime);
        btnDate1 = (Button) findViewById(R.id.btnDate);
        AutoCompleteTextView fromStation = (AutoCompleteTextView) findViewById(R.id.fromStation);
        AutoCompleteTextView toStation = (AutoCompleteTextView) findViewById(R.id.toStation);

//        DbHelper dbHelper = new DbHelper(this);
//        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
//        Cursor cursor = dbHelper.ge
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, null, true, testView1);
        fromStation.setAdapter(adapter);
        toStation.setAdapter(adapter);

        CsvHandler csv = new CsvHandler(getApplicationContext());
        csv.updateCsv();

        Resources res = getApplicationContext().getResources();
        res.getIdentifier("stations", "raw", getPackageName());


//        File file = new File(res.getResourceName(resId));
//        getResources().getValue(R.raw.stations, new TypedValue(), false);
//        Log.d("file path", file.getAbsolutePath());
//        Log.d("file exists", "" + file.exists());
//        File fileCsv = new File(res.getResourceName(resId) + ".csv");
//        Log.d("file path", fileCsv.getAbsolutePath());
//        Log.d("file exists", "" + fileCsv.exists());
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            this.setStyle(DatePickerFragment.STYLE_NORMAL, R.style.AppTheme);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar c = new GregorianCalendar(year, month, day);

            btnDate1.setText(c.get(Calendar.DATE) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR));
//            new DateTime();
        }
    }
}
