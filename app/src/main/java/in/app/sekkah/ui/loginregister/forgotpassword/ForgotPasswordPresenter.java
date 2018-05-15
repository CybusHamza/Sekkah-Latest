package in.app.sekkah.ui.loginregister.forgotpassword;

import org.json.JSONObject;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.callbacks.JSONCallback;

/**
 * Created by hamza on 28/04/2018.
 */

public class ForgotPasswordPresenter  extends BasePresenter<ForgotPasswordContract.View> implements ForgotPasswordContract.Presenter {

    private DataManager mDataManager;

    private static final String TAG = ForgotPasswordPresenter.class.getSimpleName();

    @Inject
    public ForgotPasswordPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }

    @Override
    public void onAttach(ForgotPasswordContract.View mvpView) {
        super.onAttach(mvpView);
    }

    @Override
    public void changePassword(String email, String password, String passwordConfirm) {

        mDataManager.forgotPassword(email, password, password, new JSONCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                getMvpView().changePasswordSuccessful();
            }

            @Override
            public void onFail(String errorMessage) {
                getMvpView().changePasswordFailed(errorMessage);
            }
        });
    }
}
