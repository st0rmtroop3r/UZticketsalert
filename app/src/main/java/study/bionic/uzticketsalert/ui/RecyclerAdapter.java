package study.bionic.uzticketsalert.ui;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import study.bionic.uzticketsalert.R;
import study.bionic.uzticketsalert.entity.Train;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.TrainViewHolder>{

    List<Train> trains;
    ViewGroup parent;

    public RecyclerAdapter(List<Train> trains) {
        this.trains = trains;
    }

    @Override
    public TrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_details_layout, parent, false);
        return new TrainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrainViewHolder holder, int position) {
//        holder.trainDetails = trains.get(position);
        holder.train_number.setText(trains.get(position).getNum());
        holder.train_station_from.setText(trains.get(position).getFrom().getStation());
        holder.train_station_till.setText(trains.get(position).getTill().getStation());

        holder.train_departue.setText(DateFormat.format(
                parent.getResources().getString(R.string.date_format),
                Long.valueOf(trains.get(position).getFrom().getDate() + "000")));
        holder.train_arrival.setText(DateFormat.format(
                parent.getResources().getString(R.string.date_format),
                Long.valueOf(trains.get(position).getTill().getDate() + "000")));
        holder.tickets_title.setText(parent.getResources().getString(R.string.tickets_available));

        String places = trains.get(position).getTypes().get(0).getLetter();
        places += " " + trains.get(position).getTypes().get(0).getPlaces();
        holder.tickets_class1.setText(places);

        if (trains.get(position).getTypes().size() > 1) {
            places = trains.get(position).getTypes().get(1).getLetter();
            places += " " + trains.get(position).getTypes().get(1).getPlaces();
            holder.tickets_class2.setText(places);
        }

        if (trains.get(position).getTypes().size() > 2) {
            places = trains.get(position).getTypes().get(2).getLetter();
            places += " " + trains.get(position).getTypes().get(2).getPlaces();
            holder.tickets_class3.setText(places);
        }
    }

    @Override
    public int getItemCount() {
        return trains.size();
    }

    public static class TrainViewHolder extends RecyclerView.ViewHolder {

        TextView train_number;
        TextView train_station_from;
        TextView train_station_till;
        TextView train_departue;
        TextView train_arrival;
        TextView tickets_title;
        TextView tickets_class1;
        TextView tickets_class2;
        TextView tickets_class3;

        private boolean isSelected;
        private Set<Train> trainsSelected;

        public TrainViewHolder(View itemView) {
            super(itemView);

            train_number = (TextView) itemView.findViewById(R.id.train_number);
            train_station_from = (TextView) itemView.findViewById(R.id.train_station_from);
            train_station_till = (TextView) itemView.findViewById(R.id.train_station_till);
            train_departue = (TextView) itemView.findViewById(R.id.train_departue);
            train_arrival = (TextView) itemView.findViewById(R.id.train_arrival);
            tickets_title = (TextView) itemView.findViewById(R.id.tickets_title);
            tickets_class1 = (TextView) itemView.findViewById(R.id.tickets_class1);
            tickets_class2 = (TextView) itemView.findViewById(R.id.tickets_class2);
            tickets_class3 = (TextView) itemView.findViewById(R.id.tickets_class3);

            isSelected = false;
        }
    }

}
