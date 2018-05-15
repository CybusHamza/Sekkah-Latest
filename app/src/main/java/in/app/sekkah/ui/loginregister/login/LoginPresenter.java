package in.app.sekkah.ui.loginregister.login;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.User;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter
{
    private DataManager mDataManager;

    private static final String TAG = LoginPresenter.class.getSimpleName();

    @Inject
    public LoginPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }

    @Override
    public void login(String username, String password)
    {
        mDataManager.login(username, password, new JSONCallback()
        {

            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                getMvpView().loginSuccessful();
                User user = new User();
                try
                {
                    user.mAccessToken = jsonObject.getString("token");
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Error");
                    e.printStackTrace();
                    return;
                }

                mDataManager.setCurrentUser(user);
            }

            @Override
            public void onFail(String errorMessage)
            {
                getMvpView().loginFailed(errorMessage);
            }
        });
    }
}
