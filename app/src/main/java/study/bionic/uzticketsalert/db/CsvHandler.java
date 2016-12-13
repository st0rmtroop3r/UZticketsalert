package study.bionic.uzticketsalert.db;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler extends Application {

    private Context context;

    public CsvHandler(Context applicationContext) {
        super();
        context = applicationContext;
    }

    public List<String[]> readStationsCsv() {

        int resId = context.getResources().getIdentifier("stations", "raw", context.getPackageName());
        InputStream is = context.getResources().openRawResource(resId);
        if (is == null) {
            return null;
        }
        CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(is)), ';');

        List<String[]> stations = new ArrayList<>();
        try {
            stations = csvReader.readAll();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Csv read all", e.getMessage());
        }
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                Log.e("Reader close", e.getMessage());
            }
        }

        Log.d("stations size", "" + stations.size());
        Log.d("stations.get(0)", (stations.get(0) != null) ? stations.get(0)[1] : "null");

        return stations;
    }

}
