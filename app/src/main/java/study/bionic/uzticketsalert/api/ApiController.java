package study.bionic.uzticketsalert.api;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController extends Application {

    private static ApiController controller;

    private RestApi restApi;

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102/app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApi = retrofit.create(RestApi.class);
    }

    public static ApiController getInstance() {
        return controller;
    }

    public RestApi getRestApi() {
        return restApi;
    }
}
