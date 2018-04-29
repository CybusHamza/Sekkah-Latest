package in.radioactivegames.sekkah.ui.main.contact;

import org.json.JSONObject;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.User;
import in.radioactivegames.sekkah.ui.loginregister.forgotpassword.ForgotPasswordPresenter;

/**
 * Created by AntiSaby on 1/3/2018.
 * www.radioactivegames.in
 */

public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter
{

    private DataManager mDataManager;

    private static final String TAG = ContactPresenter.class.getSimpleName();

    @Inject
    public ContactPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;

    }

    @Override
    public void contactUs( String subject, String type, String message,JSONCallback jsonCallback) {


        final User user = mDataManager.getCurrentUser();

        mDataManager.contactUs(user.mAccessToken,subject, type, message,jsonCallback);
    }
}
