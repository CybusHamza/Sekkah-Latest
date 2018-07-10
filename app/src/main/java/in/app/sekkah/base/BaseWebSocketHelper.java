package in.app.sekkah.base;

import com.google.android.gms.maps.model.LatLng;

import in.app.sekkah.data.callbacks.JSONCallback;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

public interface BaseWebSocketHelper
{
    void trackTrain(String trainId, String userAccessToken, JSONCallback callbac);
    void stopTrackTrain(String trainId);
    void startTrackUser(String trainId, String userAccessToken);
    void trainLocationReport(String stationId, String ts,String userAccessToken, final JSONCallback callback);
    void updateUser(LatLng location);
    void startScheduleTracking(String userAccessToken,JSONCallback jsonCallback);
    void stopTrackUser();
}
