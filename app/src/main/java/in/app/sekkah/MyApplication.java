package in.app.sekkah;

import android.support.multidex.MultiDexApplication;

import com.twitter.sdk.android.core.Twitter;

import in.app.sekkah.di.component.ApplicationComponent;
import in.app.sekkah.di.component.DaggerApplicationComponent;
import in.app.sekkah.di.module.ApplicationModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by AntiSaby on 11/4/2017.
 */

public class MyApplication extends MultiDexApplication
{
    private ApplicationComponent applicationComponent;
    public static final String REALM_DB="sekkah.realm";

    @Override
    public void onCreate()
    {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        Twitter.initialize(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name(REALM_DB).build();
        Realm.setDefaultConfiguration(config);

    }

    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }
}
