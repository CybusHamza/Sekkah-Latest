package in.app.sekkah.ui.loginregister.register;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class RegisterContract
{
    public interface View extends BaseMvpView
    {
        public void registrationSuccessful();
        public void registrationFailed(String message);
    }

    public interface Presenter extends BaseMvpPresenter<RegisterContract.View>
    {
        public void register(String username, String firstName, String lastName, String email, long dateOfBirth, String password, String confirmPassword);
    }
}
