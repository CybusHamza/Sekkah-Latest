package in.radioactivegames.sekkah.base;

import com.google.android.gms.maps.model.LatLng;

import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

public interface BaseWebSocketHelper
{
    void trackTrain(String trainId, String userAccessToken, TrainLocationCallback callback);
    void stopTrackTrain(String trainId);
    void startTrackUser(String trainId, String userAccessToken);
    void trainLocationReport(String stationId, String ts,String userAccessToken, final TrainLocationCallback callback);
    void updateUser(LatLng location);
    void startScheduleTracking(String userAccessToken);
    void stopTrackUser();
}
