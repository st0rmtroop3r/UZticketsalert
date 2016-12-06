package study.bionic.uzticketsalert.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("api/stations/version")
    Call<Long> getVertion();

}
