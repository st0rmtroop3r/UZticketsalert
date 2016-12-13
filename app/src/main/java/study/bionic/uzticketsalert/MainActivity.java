package study.bionic.uzticketsalert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import study.bionic.uzticketsalert.api.ApiController;
import study.bionic.uzticketsalert.core.UpdateService;
import study.bionic.uzticketsalert.db.DbHelper;
import study.bionic.uzticketsalert.entity.TrainsSearchParcel;
import study.bionic.uzticketsalert.entity.Train;
import study.bionic.uzticketsalert.ui.AutoCompleteAdapter;
import study.bionic.uzticketsalert.ui.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int TRAINS_REQUEST = 1;
    public static final String PREF_IS_MONOTORING = "isMonitoring";
    public static final String PREF_DEVICE_ID = "deviceId";
    private DbHelper mDBAdabter;  // database adapter / helper
    private AutoCompleteAdapter mCursorAdapter;  // cursor adapter
    private SharedPreferences localPref;
    private TrainsSearchParcel trainsSearchParcel;
    private static Map<String, String> dates;
    private static final String TAG = MainActivity.class.getSimpleName();
    private UpdateService uSrevice;
    boolean uBound = false;
    boolean isMonitoring = false;

    @BindView(R.id.inputLayoutFrom) TextInputLayout inputLayoutFrom;
    @BindView(R.id.inputLayoutTo) TextInputLayout inputLayoutTo;
    @BindView(R.id.autoCompleteFrom) AutoCompleteTextView autoCompleteFrom;
    @BindView(R.id.autoCompleteTo) AutoCompleteTextView autoCompleteTo;
    @BindView(R.id.textDate) TextView txtDate;
    @BindView(R.id.recycler) RecyclerView recyclerView;

    @BindView(R.id.testView1) TextView testView1;
    @BindView(R.id.testView2) TextView testView2;
    @BindView(R.id.testView3) TextView testView3;
    @BindView(R.id.testView4) TextView testView4;
    @BindView(R.id.testView5) TextView testView5;
    @BindView(R.id.testView6) TextView testView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DbHelper.getInstance(getApplicationContext());

        localPref = getApplicationContext().getSharedPreferences("MainActivity",
                Context.MODE_PRIVATE);
        isMonitoring = localPref.getBoolean(PREF_IS_MONOTORING, false);

        if (localPref.getString(PREF_DEVICE_ID, null) == null) {
            Log.d("pref deviceId", "is null");

            ApiController.getInstance().getRestApi().registerDevice().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "Get id success: " + response.code());
                    final String devId = response.body();
                    Log.d(TAG, "devId: " + devId);
                    localPref.edit().putString("deviceId", devId).commit();
//                    testView1.setText(devId);
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "Get dev id failed", t);
                }
            });
        } else {
//            Log.d("pref deviceId", localPref.getString("deviceId", null));
//            testView2.setText(localPref.getString("deviceId", null));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trainsSearchParcel = new TrainsSearchParcel();
        dates = trainsSearchParcel.getDates();

        AutoCompleteAdapter adapterFrom = new AutoCompleteAdapter(this, null, true, trainsSearchParcel.getFrom(), inputLayoutFrom);
        AutoCompleteAdapter adapterTo = new AutoCompleteAdapter(this, null, true, trainsSearchParcel.getTo(), inputLayoutTo);

        autoCompleteFrom.setAdapter(adapterFrom);
        autoCompleteFrom.setOnItemClickListener(adapterFrom);

        autoCompleteTo.setAdapter(adapterTo);
        autoCompleteTo.setOnItemClickListener(adapterTo);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = "" + day + "." + (month + 1) + "." + year;

        txtDate.setText(DateFormat.format(getString(R.string.date_format), c));
        dates.put(String.valueOf(R.id.textDate), date);


    }

    public void searchTrains(View v) {

        if (trainsSearchParcel.getFrom().getId() == null || !trainsSearchParcel.getFrom().getName().equals(autoCompleteFrom.getText().toString())) {
            if (!inputLayoutFrom.isErrorEnabled()) {
                inputLayoutFrom.setErrorEnabled(true);
            }
            inputLayoutFrom.setError("Select station from the suggestion list");
            return;
        }
        if (trainsSearchParcel.getTo().getId() == null || !trainsSearchParcel.getTo().getName().equals(autoCompleteTo.getText().toString())) {
            if (!inputLayoutTo.isErrorEnabled()) {
                inputLayoutTo.setErrorEnabled(true);
            }
            inputLayoutTo.setError("Select station from the suggestion list");
            return;
        }
        trainsSearchParcel.setDates(dates);
        if (trainsSearchParcel.getDates().size() == 0) {
            Toast.makeText(getApplicationContext(), "Select date", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("searchTrains", trainsSearchParcel.toString());

        ApiController.getInstance().getRestApi().getTrains(trainsSearchParcel).enqueue(new Callback<Map<String, List<Train>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Train>>> call, Response<Map<String, List<Train>>> response) {

                Map<String, List<Train>> map = response.body();
                Intent intent = new Intent(MainActivity.this, TrainsSelectionActivity.class);
                String date = map.keySet().iterator().next();
                intent.putExtra("date", date);
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) map.get(date));
                startActivityForResult(intent, TRAINS_REQUEST);
            }
            @Override
            public void onFailure(Call<Map<String, List<Train>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Server connection failure", Toast.LENGTH_SHORT).show();
            }
        });

//        Intent intent = new Intent(this, TrainsSelectionActivity.class);
//        intent.putExtra("extras", trainsSearchParcel);
//        startActivityForResult(intent, TRAINS_REQUEST);
    }

//    public void manageSecondDate(View v) {
//        LinearLayout layout = (LinearLayout) findViewById(R.id.dateSelection2);
//        ImageButton ib = (ImageButton) findViewById(R.id.btnDate2);
//        manageDates(layout, ib);
//    }
//    public void manageThirdDate(View v) {
//        LinearLayout layout = (LinearLayout) findViewById(R.id.dateSelection3);
//        ImageButton ib = (ImageButton) findViewById(R.id.btnDate3);
//        manageDates(layout, ib);
//    }
//    private void manageDates(LinearLayout layout, ImageButton ib) {
//        switch (layout.getVisibility()) {
//            case View.GONE:
//                layout.setVisibility(View.VISIBLE);
//                ib.setImageResource(android.R.drawable.presence_offline);
//                // TODO add date to map and view
//                break;
//            case View.VISIBLE:
//                layout.setVisibility(View.GONE);
//                ib.setImageResource(android.R.drawable.ic_input_add);
//                // TODO remove date from map and view
//                break;
//            default:
//                Log.wtf("visibility", "" + getString(View.INVISIBLE));
//        }
//    }

    public void showDatePickerDialog(View v) {

        Log.d("view id", "" + v.getId());
        Bundle bundle = new Bundle();
        bundle.putInt("viewId", v.getId());

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            this.setStyle(DatePickerFragment.STYLE_NORMAL, R.style.AppTheme);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar c = new GregorianCalendar(year, month, day);
            Bundle bundle = this.getArguments();
            int viewId = bundle.getInt("viewId");
            TextView dateTextView = (TextView) getActivity().findViewById(viewId);

            dateTextView.setText(DateFormat.format(getString(R.string.date_format), c));
            String date = "" + day + "." + (month + 1) + "." + year;
            dates.put(String.valueOf(viewId), date);

//            String btnId = bundle.getString("date Button Id");
//            String date = (String) DateFormat.format(getString(R.string.date_format), c);
//            testView3.setText(c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.settings);
//        item.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.stopAll:
                stopService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "req " + requestCode + ", res " + resultCode);
        if (requestCode == TRAINS_REQUEST) {
            if (data != null) {
                ArrayList<Train> trains = data.getParcelableArrayListExtra("trains");
                Log.d("trains size", String.valueOf(trains.size()));

                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(trains);
                recyclerView.setAdapter(recyclerAdapter);

                Intent intent = new Intent(this, UpdateService.class);
                intent.putParcelableArrayListExtra("list", trains);

                startService(intent);
                Log.d(TAG, "onActivityResult: startService");
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "onActivityResult: bindService");
                uBound = true;
                localPref.edit().putBoolean(PREF_IS_MONOTORING, isMonitoring = true).commit();
            }
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpdateService.LocalBinder bindera = (UpdateService.LocalBinder) iBinder;
            uSrevice = bindera.getService();
            uBound = true;
            Log.d(TAG, "onServiceConnected: true");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            uBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume: ");
        if (uBound) {
            String s= "null";
            s = (uSrevice == null) ? "uSrevice == null" : "n" + uSrevice.getCounter();
            Log.d("getCouner", s);
            testView1.setText(s);
        }
        testView2.setText("PREF_IS_MONOTORING:" + localPref.getBoolean(PREF_IS_MONOTORING, false));
//        testView4.setText("name: " + Thread.currentThread().getName() + ", id=" + Thread.currentThread().getId());
//        testView5.setText((uSrevice == null) ? "uSrevice == null" : uSrevice.getNameAndId());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart: ");
        if (isMonitoring) {
//            Log.d(TAG, "onStart: isMonitor true");
            if (!uBound) {
//                Log.d(TAG, "onStart: bind service");
                Intent intent = new Intent(this, UpdateService.class);
//            if (uSrevice == nu)
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                if (uSrevice == null) {
                    Log.d(TAG, "onStart: uSrevice == null, start");
                    startService(intent);
                }
                uBound = true;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.d(TAG, "onStop: ");
        if (uBound) {
            unbindService(mConnection);
            uBound = false;
        }
    }

    public void stopService() {
//        Log.d(TAG, "stopService: ");
        if (uBound) {
            unbindService(mConnection);
            uBound = false;
        }
        stopService(new Intent(this, UpdateService.class));
        localPref.edit().putBoolean(PREF_IS_MONOTORING, isMonitoring = false).apply();
    }
}
