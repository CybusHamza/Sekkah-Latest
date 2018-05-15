package in.radioactivegames.sekkah.di.component;

import dagger.Component;
import in.radioactivegames.sekkah.di.module.FragmentModule;
import in.radioactivegames.sekkah.di.scope.PerFragment;
import in.radioactivegames.sekkah.ui.loginregister.forgotpassword.ForgotPasswordActivity;
import in.radioactivegames.sekkah.ui.loginregister.login.LoginFragment;
import in.radioactivegames.sekkah.ui.loginregister.register.RegisterFragment;
import in.radioactivegames.sekkah.ui.main.contact.ContactFragment;
import in.radioactivegames.sekkah.ui.main.home.HomeFragment;
import in.radioactivegames.sekkah.ui.main.report.ReportFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.ui.main.track.map.MapFragment;
import in.radioactivegames.sekkah.ui.main.track.station.StationFragment;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;


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
