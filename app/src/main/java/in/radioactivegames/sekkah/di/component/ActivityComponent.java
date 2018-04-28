package in.radioactivegames.sekkah.di.component;

import dagger.Component;
import in.radioactivegames.sekkah.di.module.ActivityModule;
import in.radioactivegames.sekkah.di.scope.PerActivity;
import in.radioactivegames.sekkah.ui.loginregister.LoginRegisterActivity;
import in.radioactivegames.sekkah.ui.loginregister.forgotpassword.ForgotPasswordActivity;
import in.radioactivegames.sekkah.ui.main.MainActivity;


/**
 * Created by AntiSaby on 11/5/2017.
 * www.radioactivegames.in
 */

@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent
{
    void inject(MainActivity mainActivity);
    void inject(LoginRegisterActivity loginActivity);
    void inject(ForgotPasswordActivity forgotPasswordActivity);
}
