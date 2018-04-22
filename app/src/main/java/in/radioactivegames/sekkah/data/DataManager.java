package in.radioactivegames.sekkah.data;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseDataManager;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import in.radioactivegames.sekkah.data.model.User;
import in.radioactivegames.sekkah.data.network.ApiHelper;
import in.radioactivegames.sekkah.data.network.WebSocketHelper;
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsHelper;

/**
 * Created by AntiSaby on 12/15/2017.
 * www.radioactivegames.in
 */

public class DataManager implements BaseDataManager
{
    private ApiHelper mApiHelper;
    private SharedPrefsHelper mSharedPrefsHelper;
    private WebSocketHelper mWebSocketHelper;

    @Inject
    public DataManager(ApiHelper apiHelper, SharedPrefsHelper sharedPrefsHelper, WebSocketHelper webSocketHelper)
    {
        mApiHelper = apiHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
        mWebSocketHelper = webSocketHelper;
    }

    @Override
    public void registerUser(String username, String firstName, String lastName, String email, long dateOfBirth, String password, String confirmPassword, JSONCallback callback)
    {
        mApiHelper.registerUser(username, firstName, lastName, email, dateOfBirth, password, confirmPassword, callback);
    }

    @Override
    public void login(String username, String password, JSONCallback callback)
    {
        mApiHelper.loginUser(username, password, callback);
    }

    @Override
    public void setCurrentUser(User user)
    {
        mSharedPrefsHelper.setCurrentUser(user);
    }

    @Override
    public User getCurrentUser()
    {
        return mSharedPrefsHelper.getCurrentUser();
    }

    @Override
    public void trackTrain(String trainId, String userAccessToken, TrainLocationCallback callback)
    {
        mWebSocketHelper.trackTrain(trainId, userAccessToken, callback);
    }

    @Override
    public void trainLocationReport(String stationId, String ts, String userAccessToken, TrainLocationCallback callback) {
        mWebSocketHelper.trainLocationReport(stationId,ts,userAccessToken, callback);
    }

    @Override
    public void stopTrackTrain(String trainId)
    {
        mWebSocketHelper.stopTrackTrain(trainId);
    }


    @Override
    public void startTrackUser(String trainId, String userAccessToken)
    {
        mWebSocketHelper.startTrackUser(trainId, userAccessToken);
    }

    @Override
    public void updateUser(LatLng location)
    {
        mWebSocketHelper.updateUser(location);
    }

    @Override
    public void stopTrackUser()
    {
        mWebSocketHelper.stopTrackUser();
    }
}
