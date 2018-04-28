package in.radioactivegames.sekkah.ui.loginregister.forgotpassword;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import in.radioactivegames.sekkah.ui.loginregister.LoginRegisterContract;

/**
 * Created by hamza on 28/04/2018.
 */

public class ForgotPasswordContract {

    public interface View extends BaseMvpView
    {
        public void changePasswordSuccessful();
        public void changePasswordFailed(String message);

    }

    public interface Presenter extends BaseMvpPresenter<ForgotPasswordContract.View>
    {
        public void changePassword(String email, String password, String passwordConfirm);
    }
}
