package in.radioactivegames.sekkah.ui.main;

import org.json.JSONObject;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.User;

/**
 * Created by AntiSaby on 11/27/2017.
 * www.radioactivegames.in
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void getUser() {

        final User user = mDataManager.getCurrentUser();

        if (user != null) {

            mDataManager.getUserData(user.mAccessToken, new JSONCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {

                    User userNew = new User();
                    userNew.email = jsonObject.optString("email", "");
                    userNew.firstName = jsonObject.optString("firstName", "");
                    userNew.lastName = jsonObject.optString("lastName", "");
                    userNew.userName = jsonObject.optString("userName", "");
                    userNew.mAccessToken = user.mAccessToken;
                    userNew.mUserId = user.mUserId;
                    mDataManager.setCurrentUser(userNew);

                    getMvpView().setUser(userNew);
                }

                @Override
                public void onFail(String errorMessage) {

                }
            });
        }

    }
}


