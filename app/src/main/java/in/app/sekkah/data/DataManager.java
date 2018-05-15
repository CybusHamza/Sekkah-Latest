package in.app.sekkah.data;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import in.app.sekkah.base.BaseDataManager;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.callbacks.TrainLocationCallback;
import in.app.sekkah.data.model.User;
import in.app.sekkah.data.network.ApiHelper;
import in.app.sekkah.data.network.WebSocketHelper;
import in.app.sekkah.data.sharedpref.SharedPrefsHelper;

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
    public void forgotPassword(String email, String password, String confirmPassword, JSONCallback callback) {
        mApiHelper.forgotPassword(email, password,confirmPassword, callback);
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
    public void trackTrain(String trainId, String userAccessToken, JSONCallback callback)
    {
        mWebSocketHelper.trackTrain(trainId, userAccessToken, callback);
    }

    @Override
    public void trainLocationReport(String stationId, String ts, String userAccessToken, TrainLocationCallback callback) {

        //String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";

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

    @Override
    public void getUserData(String auth, JSONCallback callback) {

        mApiHelper.getProfile(auth,callback);

    }

    @Override
    public void contactUs(String auth, String subject, String type, String message, JSONCallback callback) {

        mApiHelper.contactUs(auth,subject,type,message,callback);
    }

    @Override
    public void sendPushToken(String auth, String pntoken, JSONCallback callback) {
        mApiHelper.pntoken(auth,pntoken,callback);

    }

    @Override
    public void userroute(String auth, String source, String destination, String currentLocation, String selectedLocation, String trainId, JSONCallback callback) {
        mApiHelper.userroute(auth,source,destination,currentLocation,selectedLocation,trainId,callback);

    }


}
