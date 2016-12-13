package study.bionic.uzticketsalert.core;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import study.bionic.uzticketsalert.MainActivity;
import study.bionic.uzticketsalert.api.ApiController;
import study.bionic.uzticketsalert.api.RestApi;
import study.bionic.uzticketsalert.entity.Booking;
import study.bionic.uzticketsalert.entity.Train;

import static android.content.ContentValues.TAG;

public class UpdateService extends IntentService {

    private final IBinder bindera = new LocalBinder();
    RestApi restApi = ApiController.getInstance().getRestApi();
    private List<Train> trains = new ArrayList<>();
    String deviceId;
    private SharedPreferences localPref;// = getApplicationContext().getSharedPreferences("MainActivity", Context.MODE_PRIVATE);

    private int counter = 100;
//    String name;
//    Long id;

    public UpdateService() {
        super("UpdateService");
        Log.d(TAG, "UpdateService: constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("onHandleIntent", "method start");
        localPref = getApplicationContext().getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        if (deviceId == null) {
            Log.d(TAG, "onHandleIntent: deviceId == null");
            if ( (deviceId = localPref.getString(MainActivity.PREF_DEVICE_ID, null)) == null) {
                Log.d(TAG, "onHandleIntent: devId = localPref");
                while (deviceId == null) {
                    getDeviceId();
                    synchronized (this) {
                        try {
                            wait(10000);
                        } catch (Exception e) {
                        }
                    }
                    Log.d(TAG, "onHandleIntent: deviceId in loop" + ((deviceId == null) ? "null" : deviceId));
                }
            }
        }
        if (intent.hasExtra("list")) {
            ArrayList<Train> newList = intent.getParcelableArrayListExtra("list");
            Log.d(TAG, "onHandleIntent: intent.hasExtra " + newList.size() + " size");
            trains.addAll(newList);
            try {
                wait(3000);
            } catch (Exception e) {
            }
            bookingPost();
        }
        if (trains.size() == 0) {
            Log.d(TAG, "onHandleIntent: trains.size() == 0");
            if (localPref.getBoolean(MainActivity.PREF_IS_MONOTORING, false)) {
                getMyBooking();
                try {
                    wait(3000);
                } catch (Exception e) {
                }
                if (trains.size() == 0) {
                    stopSelf();
                } else {
                    startMonitoring();
                }
            } else {
                stopSelf();
            }
        } else {
            Log.d(TAG, "onHandleIntent: trains.size() != 0");
            startMonitoring();
        }



//        while (true) {

//            name = Thread.currentThread().getName();
//            id = Thread.currentThread().getId();
//        }
    }

    public class LocalBinder extends Binder {
        public UpdateService getService() {
//            Log.d(TAG, "getService: method");
            return UpdateService.this;
        }
    }

    private void startMonitoring() {
        Log.d(TAG, "startMonitoring: ");
        while (trains.size() != 0) {
            // TODO implement server side
            counter++;
            synchronized (this) {
                try {
                    wait(2000);
                } catch (Exception e) {
                }
            }
        }
    }

    private void getDeviceId() {

        restApi.registerDevice().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "Get id success: " + response.code());
                deviceId = response.body();
                Log.d(TAG, "devId: " + deviceId);
                localPref.edit().putString("deviceId", deviceId).commit();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Get dev id failed", t);
            }
        });
    }

    private void bookingPost() {

        Booking b = new Booking(deviceId, trains);
        Log.d(TAG, "bookingPost: " + b.toString());

        restApi.booking(b).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d(TAG, "bookingPost onResponse: " + response.body());

                Log.d(TAG, "bookingPost onResponse: " + response.code() + ", " + response.message());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "bookingPost onFailure: ");
            }
        });
    }

    private void getMyBooking() {
        restApi.getMyBooking(deviceId).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {

                trains = response.body().getTrains();
                Log.d(TAG, "getMyBooking onResponse: " + trains.size());
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.d(TAG, "getMyBooking onFailure: ");
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate: ");
    }

    public UpdateService(String name) {
        super(name);
//        Log.d(TAG, "UpdateService(straing name): ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
//        Log.d(TAG, "setIntentRedelivery: ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onDestroy() {
//        startService(new Intent(this, UpdateService.class));
        super.onDestroy();
//        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: " + super.onStartCommand(intent, Service.START_FLAG_RETRY, startId));
//        START_REDELIVER_INTENT
        super.onStartCommand(intent, Service.START_FLAG_RETRY, startId);
//        return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.d(TAG, "onBind: method");
        return bindera;
    }

    public int getCounter() {
        return counter;
    }
//    public String getNameAndId() {
//        return "name: " + name  + ", id=" + id;
//    }

    public List<Train> getTrains() {
        return trains;
    }
}
