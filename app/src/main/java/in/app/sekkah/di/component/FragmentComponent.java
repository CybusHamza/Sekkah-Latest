package in.app.sekkah.di.component;

import dagger.Component;
import in.app.sekkah.di.module.FragmentModule;
import in.app.sekkah.di.scope.PerFragment;
import in.app.sekkah.ui.loginregister.login.LoginFragment;
import in.app.sekkah.ui.loginregister.register.RegisterFragment;
import in.app.sekkah.ui.main.contact.ContactFragment;
import in.app.sekkah.ui.main.home.HomeFragment;
import in.app.sekkah.ui.main.report.ReportFragment;
import in.app.sekkah.ui.main.track.TrackFragment;
import in.app.sekkah.ui.main.track.map.MapFragment;
import in.app.sekkah.ui.main.track.station.StationFragment;
import in.app.sekkah.ui.main.trainlist.TrainsFragment;


/**
 * Created by AntiSaby on 11/5/2017.
 * www.radioactivegames.in
 */

@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent
{
    void inject(LoginFragment fragment);
    void inject(RegisterFragment fragment);
    void inject(HomeFragment fragment);
    void inject(TrainsFragment fragment);
    void inject(TrackFragment fragment);
    void inject(MapFragment fragment);
    void inject(StationFragment fragment);
    void inject(ReportFragment fragment);
    void inject(ContactFragment fragment);
}
