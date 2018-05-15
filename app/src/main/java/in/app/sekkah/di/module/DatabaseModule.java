package in.app.sekkah.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

@Module
public class DatabaseModule
{
    private static final String PREFS_USER = "PREFS_USER";
    private static final String TAG = DatabaseModule.class.getSimpleName();

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@Named("ApplicationContext") Context applicationContext)
    {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

}
