package in.app.sekkah.di.component;

import android.content.SharedPreferences;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import javax.inject.Singleton;

import dagger.Component;
import in.app.sekkah.data.network.api.ApiInterface;
import in.app.sekkah.di.module.ApplicationModule;
import in.app.sekkah.di.module.DatabaseModule;
import in.app.sekkah.di.module.NetworkModule;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


/**
 * Created by AntiSaby on 11/5/2017.
 * www.radioactivegames.in
 */

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, DatabaseModule.class})
public interface ApplicationComponent
{
    TwitterAuthClient twitterAuthClient();
    Retrofit retrofit();
    ApiInterface profileApi();
    OkHttpClient okHttpClient();
    SharedPreferences sharedPreferences();
}
