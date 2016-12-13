package study.bionic.uzticketsalert.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.text.format.DateFormat;

import java.util.Set;

import study.bionic.uzticketsalert.R;
import study.bionic.uzticketsalert.entity.Train;

public class TrainDetails extends RelativeLayout {

    private Train train;
    private boolean isSelected;
    private Set<Train> trainsSelected;

    private void init(final Context context) {
        inflate(getContext(), R.layout.train_details_layout, this);
        ((TextView) findViewById(R.id.train_number)).setText(train.getNum());
        ((TextView) findViewById(R.id.train_station_from)).setText(train.getFrom().getStation());
        ((TextView) findViewById(R.id.train_station_till)).setText(train.getTill().getStation());
        ((TextView) findViewById(R.id.train_departue)).setText(
                DateFormat.format(
                        context.getString(R.string.date_format),
                        Long.valueOf(train.getFrom().getDate() + "000")));
        ((TextView) findViewById(R.id.train_arrival)).setText(
                DateFormat.format(
                        context.getString(R.string.date_format),
                        Long.valueOf(train.getTill().getDate() + "000")));

        String places = train.getTypes().get(0).getLetter();
        places += " " + train.getTypes().get(0).getPlaces();
        ((TextView) findViewById(R.id.tickets_class1)).setText(places);

        if (train.getTypes().size() > 1) {
            places = train.getTypes().get(1).getLetter();
            places += " " + train.getTypes().get(1).getPlaces();
            ((TextView) findViewById(R.id.tickets_class2)).setText(places);
        }

        if (train.getTypes().size() > 2) {
            places = train.getTypes().get(2).getLetter();
            places += " " + train.getTypes().get(2).getPlaces();
            ((TextView) findViewById(R.id.tickets_class3)).setText(places);
        }

        isSelected = false;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TypedValue a = new TypedValue();
                int color;
                if(isSelected) {
                    isSelected = false;
                    Log.d("onClick", "isSelected = false");
                    context.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
                    if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                        // windowBackground is a color
                        color = a.data;
                        setBackgroundColor(color);
                        if (trainsSelected.contains(train)) {
                            trainsSelected.remove(train);
                        }
                    } else {
                        // windowBackground is not a color, probably a drawable, set transparent
                        setBackgroundColor(0x00000000);
                    }

                } else {
                    isSelected = true;
                    Log.d("onClick", "isSelected = true");
                    setBackgroundColor(0xFF424242);
                    trainsSelected.add(train);
                }
            }
        });

    }

    public TrainDetails(Context context, Train train, Set<Train> trainsSelected) {
        super(context);
        this.train = train;
        this.trainsSelected = trainsSelected;
        init(context);

    }

    public TrainDetails(Context context) {
        super(context);
        init(context);
    }

    public TrainDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TrainDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}
