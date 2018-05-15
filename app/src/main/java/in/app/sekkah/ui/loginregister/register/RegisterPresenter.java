package in.app.sekkah.ui.loginregister.register;

import org.json.JSONObject;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.callbacks.JSONCallback;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter
{
    private DataManager mDataManager;

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    @Inject
    public RegisterPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }

    @Override
    public void register(String username, String firstName, String lastName, String email, long dateOfBirth, String password, String confirmPassword)
    {
        mDataManager.registerUser(username, firstName, lastName, email, dateOfBirth, password, confirmPassword, new JSONCallback()
        {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                getMvpView().registrationSuccessful();
            }

            @Override
            public void onFail(String errorMessage)
            {
                getMvpView().registrationFailed(errorMessage);
            }
        });
    }
}
