package study.bionic.uzticketsalert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import study.bionic.uzticketsalert.entity.Train;
import study.bionic.uzticketsalert.ui.TrainDetails;

public class TrainsSelectionActivity extends AppCompatActivity implements ActionBar.TabListener {

    private Set<Train> trainsSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trains_list_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        List<Train> trains = intent.getParcelableArrayListExtra("list");

        trainsSelected = new HashSet<>();

        LinearLayout layout = (LinearLayout) findViewById(R.id.trains_layout);
        LayoutInflater inflater = getLayoutInflater();
        TrainDetails myView;

        TextView textView = (TextView) inflater.inflate(R.layout.activity_test, null);
        textView.setText(DateFormat.format(getString(R.string.date_format), new Date(Long.parseLong(date))));
        layout.addView(textView);

        for (Train train : trains) {

            Log.d("train", train.toString());

            myView = new TrainDetails(this, train, trainsSelected);
            layout.addView(myView);
        }
    }

    public void applySelectedTrains(View view) {

        if (trainsSelected.size() > 0) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("trains", new ArrayList<Parcelable>(trainsSelected));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
