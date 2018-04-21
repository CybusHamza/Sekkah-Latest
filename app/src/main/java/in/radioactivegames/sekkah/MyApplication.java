package in.radioactivegames.sekkah;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

import in.radioactivegames.sekkah.di.component.ApplicationComponent;
import in.radioactivegames.sekkah.di.component.DaggerApplicationComponent;
import in.radioactivegames.sekkah.di.module.ApplicationModule;


/**
 * Created by AntiSaby on 11/4/2017.
 */

public class MyApplication extends Application
{
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        Twitter.initialize(this);
    }

    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }
}
