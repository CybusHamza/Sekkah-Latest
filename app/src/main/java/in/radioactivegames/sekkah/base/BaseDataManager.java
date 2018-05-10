package in.radioactivegames.sekkah.base;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import in.radioactivegames.sekkah.data.model.User;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface BaseDataManager
{
    void registerUser(String username, String firstName, String lastName, String email, long dateOfBirth, String password, String confirmPassword, JSONCallback callback);
    void login(String username, String password, JSONCallback callback);
    void forgotPassword(String email, String password, String confirmPassword, JSONCallback callback);
    void setCurrentUser(User user);
    User getCurrentUser();
    void trackTrain(String trainId, String userAccessToken, TrainLocationCallback callback);
    void trainLocationReport(String stationId, String ts,String userAccessToken, final TrainLocationCallback callback);
    void stopTrackTrain(String trainId);
    void startTrackUser(String trainId, String userAccessToken);
    void updateUser(LatLng location);
    void stopTrackUser();
    void getUserData(String auth,JSONCallback callback);
    void contactUs(String auth,String subject, String type, String message,JSONCallback callback);
    void sendPushToken(String auth,String pntoken ,JSONCallback callback);
    void userroute(String auth,String source ,String destination,String currentLocation,String selectedLocation, String trainId, JSONCallback callback);
}