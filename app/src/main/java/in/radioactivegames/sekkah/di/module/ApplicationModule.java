package in.radioactivegames.sekkah.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AntiSaby on 11/4/2017.
 * www.radioactivegames.in
 */

@Module
public class ApplicationModule
{
    private Context context;

    public ApplicationModule(Application application)
    {
        this.context = application.getApplicationContext();
    }

    @Provides
    @Named("ApplicationContext")
    public Context provideApplicationContext()
    {
        return context;
    }
}
