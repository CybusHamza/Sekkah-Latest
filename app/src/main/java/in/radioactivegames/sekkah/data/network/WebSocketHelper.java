package in.radioactivegames.sekkah.data.network;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseWebSocketHelper;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import io.realm.Realm;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

public class WebSocketHelper implements BaseWebSocketHelper {

    private Socket mSocketTrackTrain, mSocketTrackUser, mSocketScheduleTracking;
    private Emitter.Listener mListenerTrackTrain, mListnerSchdule;

    private static final String URL_TRACK_TRAIN = "http://sekkah-ws-tut-proto.herokuapp.com/traintracking?token=";
    private static final String URL_TRACK_USER = "http://sekkah-ws-tut-proto.herokuapp.com/usertracking?token=";
    private static final String URL_SHECDULE_TRACKKING = "http://sekkah-ws-uc-proto.herokuapp.com/updates?token=";

    private static final String EVENT_TRACK_TRAIN = "track-train";
    private static final String EVENT_TRAIN_LOCATION_UPDATE = "train-location-update";
    private static final String EVENT_UNTRACK_TRAIN = "untrack-train";

    private static final String EVENT_SCHEDULE_UPDATE = "schedule-update";
    private static final String EVENT_GET_CURRENT_SCHEDULE = "get-current-schedule";

    private static final String EVENT_TRACK_USER = "track-user";
    private static final String EVENT_USER_LOCATION_UPDATE = "user-location-update";
    private static final String EVENT_UNTRACK_USER = "untrack-user";
    private static final String EVENT_TRAIN_REPORRT = "train-location-report";


    private static final String KEY_TRAIN_ID = "trainId";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_STATION_ID = "stationId";
    private static final String KEY_TS = "ts";

    private static final String TAG = WebSocketHelper.class.getSimpleName();

    @Inject
    public WebSocketHelper() {

    }

    @Override
    public void trackTrain(String trainId, String userAccessToken, final TrainLocationCallback callback) {
        Log.d(TAG, "Starting Socket!");
        stopTrackTrain(trainId);
        try {
            mSocketTrackTrain = IO.socket(URL_TRACK_TRAIN + userAccessToken);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Error URIException Track Train");
            e.printStackTrace();
        }

        JSONObject data = null;
        try {
            data = new JSONObject();
            data.put(KEY_TRAIN_ID, trainId);
        } catch (JSONException e) {
            Log.d(TAG, "JSON Exception");
            e.printStackTrace();
        }

        mListenerTrackTrain = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null)
                    return;
                //Log.d(TAG, args[0].toString());
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    LatLng latLng = new LatLng(jsonObject.getDouble("lat"),
                            jsonObject.getDouble("lng"));
                    callback.onLocationReceive(latLng);
                } catch (JSONException e) {
                    Log.d(TAG, "JSON Exception");
                    e.printStackTrace();
                }
            }
        };

        mSocketTrackTrain.on(EVENT_TRAIN_LOCATION_UPDATE, mListenerTrackTrain);
        mSocketTrackTrain.connect();
        mSocketTrackTrain.emit(EVENT_TRACK_TRAIN, data);
    }

    @Override
    public void stopTrackTrain(String trainId) {
        Log.d(TAG, "Stopping Socket!");
        if (mSocketTrackTrain != null) {
            JSONObject data = null;
            try {
                data = new JSONObject();
                data.put(KEY_TRAIN_ID, trainId);
            } catch (JSONException e) {
                Log.d(TAG, "JSON Exception");
                e.printStackTrace();
            }

            mSocketTrackTrain.emit(EVENT_UNTRACK_TRAIN, data);
            mSocketTrackTrain.off(EVENT_TRAIN_LOCATION_UPDATE, mListenerTrackTrain);
            mSocketTrackTrain.disconnect();
            mListenerTrackTrain = null;
            mSocketTrackTrain = null;
        }
    }

    @Override
    public void startTrackUser(String trainId, String userAccessToken) {
        Log.d(TAG, "Starting User Socket!");
        stopTrackUser();
        try {
            mSocketTrackUser = IO.socket(URL_TRACK_USER + userAccessToken);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Error URIException Track User");
            e.printStackTrace();
        }

        JSONObject data = null;
        try {
            data = new JSONObject();
            data.put(KEY_TRAIN_ID, trainId);
        } catch (JSONException e) {
            Log.d(TAG, "JSON Exception");
            e.printStackTrace();
        }

        mSocketTrackUser.connect();
        mSocketTrackUser.emit(EVENT_TRACK_USER, data);
    }

    @Override
    public void trainLocationReport(String stationId, String ts, String userAccessToken, final TrainLocationCallback callback) {

        Log.d(TAG, "Train Location Report!");

        try {
            mSocketTrackTrain = IO.socket(URL_TRACK_TRAIN + userAccessToken);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Error URIException Track Train");
            e.printStackTrace();
        }

        JSONObject data = null;
        try {
            data = new JSONObject();
            data.put(KEY_STATION_ID, stationId);
            data.put(KEY_TS, ts);
        } catch (JSONException e) {
            Log.d(TAG, "JSON Exception");
            e.printStackTrace();
        }

        mListenerTrackTrain = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null)
                    return;
                //Log.d(TAG, args[0].toString());
                try {
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    LatLng latLng = new LatLng(jsonObject.getDouble("lat"),
                            jsonObject.getDouble("lng"));
                    callback.onLocationReceive(latLng);
                } catch (JSONException e) {
                    Log.d(TAG, "JSON Exception");
                    e.printStackTrace();
                }
            }
        };

        mSocketTrackTrain.on(EVENT_TRAIN_LOCATION_UPDATE, mListenerTrackTrain);
        mSocketTrackTrain.connect();
        mSocketTrackTrain.emit(EVENT_TRAIN_REPORRT, data);
    }


    @Override
    public void updateUser(LatLng location) {
        if (mSocketTrackUser == null) {
            Log.e(TAG, "SOCKET UPDATE USER NULL");
            return;
        }

        JSONObject data = null;
        try {
            data = new JSONObject();
            data.put(KEY_LAT, location.latitude);
            data.put(KEY_LNG, location.longitude);
        } catch (JSONException e) {
            Log.d(TAG, "JSON Exception");
            e.printStackTrace();
        }

        Log.d(TAG, "UPDATING USER");
        mSocketTrackUser.emit(EVENT_USER_LOCATION_UPDATE, data);
    }

    @Override
    public void startScheduleTracking(String userAccessToken) {

        Log.d(TAG, "Stariting Scheduling");
        try {
            mSocketScheduleTracking = IO.socket(URL_SHECDULE_TRACKKING + userAccessToken);
        } catch (URISyntaxException e) {
            Log.d(TAG, "Error URIException Track Train");
            e.printStackTrace();
        }


        mListnerSchdule = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null)
                    return;

                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmDB realmDB = RealmDB.getinstance();
                    realmDB.clearAll(realm);

                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    Log.d(TAG, jsonObject.toString());

                } catch (JSONException e) {
                    Log.d(TAG, "JSON Exception");
                    e.printStackTrace();
                }
            }

        };

        mSocketScheduleTracking.on(EVENT_SCHEDULE_UPDATE, mListnerSchdule);
        mSocketScheduleTracking.connect();
        mSocketScheduleTracking.emit(EVENT_GET_CURRENT_SCHEDULE);


    }


    @Override
    public void stopTrackUser() {
        Log.d(TAG, "Stopping User Socket!");
        if (mSocketTrackUser != null) {
            mSocketTrackUser.emit(EVENT_UNTRACK_TRAIN);
            mSocketTrackUser.disconnect();
            mSocketTrackUser = null;
        }
    }
}
