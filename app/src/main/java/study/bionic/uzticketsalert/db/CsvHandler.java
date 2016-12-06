package study.bionic.uzticketsalert.db;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import study.bionic.uzticketsalert.R;

public class CsvHandler extends Application {

    private Context context;

    public CsvHandler(Context applicationContext) {
        super();
        context = applicationContext;
    }

    public void updateCsv() {

        int resId = context.getResources().getIdentifier("stations", "raw", context.getPackageName());
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        int counter = 0;
        List<String> lines = new ArrayList<>();
        try {
            while (((line = br.readLine()) != null) && counter < 5) {
                lines.add(line);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("lines size", "" + lines.size());
        Log.d("", (lines.get(0) != null) ? lines.get(0) : "null");
    }

}
