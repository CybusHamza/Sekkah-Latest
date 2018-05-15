package in.radioactivegames.sekkah.di.module;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import in.radioactivegames.sekkah.data.network.api.ApiInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AntiSaby on 12/10/2017.
 * www.radioactivegames.in
 */

@Module
public class NetworkModule
{

    private static final String BASE_URL = "http://sekkah-api-proto.herokuapp.com/api/v1/";
    private static final String TAG = NetworkModule.class.getSimpleName();

    @Provides
    @Singleton
    public TwitterAuthClient provideTwitterAuthClient()
    {
        return new TwitterAuthClient();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient()
    {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder().header("content-type", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);

        return okHttpClient.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofitClient(OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ApiInterface provideProfileApi(Retrofit retrofit)
    {
        return retrofit.create(ApiInterface.class);
    }

}
