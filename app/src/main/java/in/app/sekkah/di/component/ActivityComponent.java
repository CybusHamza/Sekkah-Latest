package in.app.sekkah.di.component;

import dagger.Component;
import in.app.sekkah.di.module.ActivityModule;
import in.app.sekkah.di.scope.PerActivity;
import in.app.sekkah.ui.loginregister.LoginRegisterActivity;
import in.app.sekkah.ui.loginregister.forgotpassword.ForgotPasswordActivity;
import in.app.sekkah.ui.loginregister.login.LoginActivity;
import in.app.sekkah.ui.main.MainActivity;


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
    void inject(LoginActivity loginActivity);
    void inject(ForgotPasswordActivity forgotPasswordActivity);
}
