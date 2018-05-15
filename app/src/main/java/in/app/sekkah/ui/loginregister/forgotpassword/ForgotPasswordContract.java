package in.app.sekkah.ui.loginregister.forgotpassword;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;

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
