package in.app.sekkah.data.sharedpref;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import in.app.sekkah.base.BaseSharedPrefsHelper;
import in.app.sekkah.data.model.User;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

public class SharedPrefsHelper implements BaseSharedPrefsHelper
{
    private SharedPreferences mSharedPreferences;

    private static final String KEY_CURRENT_USER = "CURRENT_USER";
    private static final String TAG = SharedPrefsHelper.class.getSimpleName();

    @Inject
    public SharedPrefsHelper(SharedPreferences sharedPreferences)
    {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void setCurrentUser(User user)
    {
        String userString = new Gson().toJson(user);
        mSharedPreferences.edit()
                .putString(KEY_CURRENT_USER, userString)
                .apply();
    }

    @Override
    public User getCurrentUser()
    {
        String userString = mSharedPreferences.getString(KEY_CURRENT_USER, null);
        if(userString == null)
            return null;
        User user = new Gson().fromJson(userString, User.class);
        return user;
    }
}
