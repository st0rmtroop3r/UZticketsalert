package study.bionic.uzticketsalert.entity;

import java.util.List;

public class Booking {

    String deviceId;
    List<Train> trains;

    public Booking(String deviceId, List<Train> trains) {
        this.deviceId = deviceId;
        this.trains = trains;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public List<Train> getTrains() {
        return trains;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "deviceId='" + deviceId + '\'' +
                ", trains size=" + trains.size() +
                '}';
    }
}
