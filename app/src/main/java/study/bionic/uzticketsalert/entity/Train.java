package study.bionic.uzticketsalert.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Train implements Parcelable{

    private String num;
    private String model;
    private String category;
    private String travel_time;
    private From from;
    private Till till;
    private List<Type> types = new ArrayList<>();

    protected Train(Parcel in) {
        num = in.readString();
        model = in.readString();
        category = in.readString();
        travel_time = in.readString();
        from = new From();
        from.station_id = in.readString();
        from.station = in.readString();
        from.date = in.readString();
        from.src_date = in.readString();
        till = new Till();
        till.station_id = in.readString();
        till.station = in.readString();
        till.date = in.readString();
        till.src_date = in.readString();
        types = new ArrayList<>();
        int typesSize = in.readInt();
        Type type;
        for (int i = 0; i < typesSize; i++) {
            type = new Type();
            type.title = in.readString();
            type.letter = in.readString();
            type.places = in.readInt();
            types.add(type);
        }

    }

    public static final Creator<Train> CREATOR = new Creator<Train>() {
        @Override
        public Train createFromParcel(Parcel in) {
            return new Train(in);
        }

        @Override
        public Train[] newArray(int size) {
            return new Train[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(num);
        parcel.writeString(model);
        parcel.writeString(category);
        parcel.writeString(travel_time);
        //from
        parcel.writeString(from.station_id);
        parcel.writeString(from.station);
        parcel.writeString(from.date);
        parcel.writeString(from.src_date);
        //till
        parcel.writeString(till.station_id);
        parcel.writeString(till.station);
        parcel.writeString(till.date);
        parcel.writeString(till.src_date);
        //types
        parcel.writeInt(types.size());
        for (Type type : types) {
            parcel.writeString(type.title);
            parcel.writeString(type.letter);
            parcel.writeInt(type.places);
        }
//        parcel.writeTypedList();
//        parcel.writeStringList();
    }

    public class From {
        String station_id;
        String station;
        String date;
        String src_date;

        public String getStation_id() {
            return station_id;
        }

        public String getStation() {
            return station;
        }

        public String getDate() {
            return date;
        }

        public String getSrc_date() {
            return src_date;
        }

        @Override
        public String toString() {
            return "From{" +
                    "station_id='" + station_id + '\'' +
                    ", station='" + station + '\'' +
                    ", date='" + date + '\'' +
                    ", src_date='" + src_date + '\'' +
                    '}';
        }
    }

    public class Till {
        String station_id;
        String station;
        String date;
        String src_date;

        public String getStation_id() {
            return station_id;
        }

        public String getStation() {
            return station;
        }

        public String getDate() {
            return date;
        }

        public String getSrc_date() {
            return src_date;
        }

        @Override
        public String toString() {
            return "Till{" +
                    "station_id='" + station_id + '\'' +
                    ", station='" + station + '\'' +
                    ", date='" + date + '\'' +
                    ", src_date='" + src_date + '\'' +
                    '}';
        }
    }
    public class Type {
        String title;
        String letter;
        int places;

        public String getTitle() {
            return title;
        }

        public String getLetter() {
            return letter;
        }

        public int getPlaces() {
            return places;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "title='" + title + '\'' +
                    ", letter='" + letter + '\'' +
                    ", places=" + places +
                    '}';
        }
    }
    public String getNum() {
        return num;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public From getFrom() {
        return from;
    }

    public Till getTill() {
        return till;
    }

    public List<Type> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return "Train{" +
                "num='" + num + '\'' +
                ", model='" + model + '\'' +
                ", category='" + category + '\'' +
                ", travel_time='" + travel_time + '\'' +
                ", from=" + from +
                ", till=" + till +
                ", types=" + types +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Train)) return false;

        Train train = (Train) o;

        if (num != null ? !num.equals(train.num) : train.num != null) return false;
        if (model != null ? !model.equals(train.model) : train.model != null) return false;
        if (category != null ? !category.equals(train.category) : train.category != null)
            return false;
        if (travel_time != null ? !travel_time.equals(train.travel_time) : train.travel_time != null)
            return false;
        if (from != null ? !from.equals(train.from) : train.from != null) return false;
        if (till != null ? !till.equals(train.till) : train.till != null) return false;
        return types != null ? types.equals(train.types) : train.types == null;

    }

    @Override
    public int hashCode() {
        int result = num != null ? num.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (travel_time != null ? travel_time.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (till != null ? till.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        return result;
    }
}
