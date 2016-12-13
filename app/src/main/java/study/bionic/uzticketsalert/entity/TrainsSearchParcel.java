package study.bionic.uzticketsalert.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class TrainsSearchParcel implements Parcelable {

    private From from;
    private To to;
    private Map<String, String> dates;

    public TrainsSearchParcel() {
        this.from = new TrainsSearchParcel.From();
        this.to = new TrainsSearchParcel.To();
        this.dates = new HashMap<>();
    }

    protected TrainsSearchParcel(Parcel in) {
        this.dates = new HashMap<>();
        in.readMap(dates, ClassLoader.getSystemClassLoader());
        String[] fields = in.createStringArray();

        from = new TrainsSearchParcel.From();
        to = new TrainsSearchParcel.To();

        from.setId(fields[0]);
        from.setName(fields[1]);
        to.setId(fields[2]);
        to.setName(fields[3]);
    }

    public static final Creator<TrainsSearchParcel> CREATOR = new Creator<TrainsSearchParcel>() {
        @Override
        public TrainsSearchParcel createFromParcel(Parcel in) {
            return new TrainsSearchParcel(in);
        }

        @Override
        public TrainsSearchParcel[] newArray(int size) {
            return new TrainsSearchParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeMap(dates);
        parcel.writeStringArray(new String[] {from.getId(), from.getName(), to.getId(), to.getName()});
    }

    public class From implements Station {
        private String stationId;
        private String nameFrom;

        @Override
        public void setName(String name) {
            nameFrom = name;
        }

        @Override
        public String getName() {
            return nameFrom;
        }

        @Override
        public void setId(String id) {
            stationId = id;
        }

        @Override
        public String getId() {
            return stationId;
        }

        @Override
        public String toString() {
            return "From{" +
                    "stationId='" + stationId + '\'' +
                    ", nameFrom='" + nameFrom + '\'' +
                    '}';
        }
    }
    public class To implements Station {
        private String stationId;
        private String nameTo;

        @Override
        public void setName(String name) {
            nameTo = name;
        }

        @Override
        public String getName() {
            return nameTo;
        }

        @Override
        public void setId(String id) {
            stationId = id;
        }

        @Override
        public String getId() {
            return stationId;
        }

        @Override
        public String toString() {
            return "To{" +
                    "stationId='" + stationId + '\'' +
                    ", nameTo='" + nameTo + '\'' +
                    '}';
        }
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public Map<String, String> getDates() {
        return dates;
    }

    @Override
    public String toString() {

        String d = "";
        for (String s : dates.keySet()) {
            d += dates.get(s) + ",";
        }

        return "TrainsSearchParcel{" +
                "from=" + from +
                ", to=" + to +
                ", dates=" + d +
                    '}';

    }

    public void setDates(Map<String, String> dates) {
        this.dates = dates;
    }

}
