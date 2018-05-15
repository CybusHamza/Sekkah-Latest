package in.app.sekkah.ui.loginregister.login;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class LoginContract
{
    public interface View extends BaseMvpView
    {
        public void loginSuccessful();
        public void loginFailed(String message);
    }

    public interface Presenter extends BaseMvpPresenter<LoginContract.View>
    {
        public void login(String username, String password);
    }
}
