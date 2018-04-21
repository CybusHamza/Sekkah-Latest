package in.radioactivegames.sekkah.ui.loginregister.login;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.User;
import retrofit2.Retrofit;

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
