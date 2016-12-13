package study.bionic.uzticketsalert.api;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import study.bionic.uzticketsalert.entity.Booking;
import study.bionic.uzticketsalert.entity.TrainsSearchParcel;
import study.bionic.uzticketsalert.entity.Train;

public interface RestApi {

    @GET("app/api/stations/version")
    Call<Long> getVersion();

    @GET("app/api/registration/")
    Call<String> registerDevice();

    @POST("app/api/trains/")
    Call<Map<String, List<Train>>> getTrains(@Body TrainsSearchParcel trainsSearchParcel);

    @POST("app/api/booking/")
    Call<Boolean> booking(@Body Booking booking);

    @POST("app/api/booking/my/")
    Call<Booking> getMyBooking(@Body String deviceId);

}
