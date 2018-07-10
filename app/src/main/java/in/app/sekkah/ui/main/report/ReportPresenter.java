package in.app.sekkah.ui.main.report;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.Station;
import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.model.User;
import io.realm.Realm;

import static in.app.sekkah.ui.main.track.station.StationFragment.trainId;
import static in.app.sekkah.utility.Constants.DELAY;
import static in.app.sekkah.utility.Constants.LATITUDE;
import static in.app.sekkah.utility.Constants.LOCATION_REPORT;
import static in.app.sekkah.utility.Constants.LONGITUDE;
import static in.app.sekkah.utility.Constants.NEXT_STATION;

/**
 * Created by AntiSaby on 1/2/2018.
 * www.radioactivegames.in
 */

public class ReportPresenter extends BasePresenter<ReportContract.View> implements ReportContract.Presenter {
    private List<String> stationNames;
    private DataManager mDataManager;

    private static final String TAG = ReportPresenter.class.getSimpleName();

    @Inject
    public ReportPresenter(DataManager dataManager) {
        List<Station> stations = new ArrayList<>();
        stationNames = new ArrayList<>();
        mDataManager = dataManager;
    }

    @Override
    public void getStationsData(Context context) {



        Realm realm = Realm.getDefaultInstance();

        ArrayList<StationPOJO> stationPOJOS = RealmDB.getinstance().getTrainStations(realm,trainId);

        try {
            for (StationPOJO user : stationPOJOS) {
                Locale current = context.getResources().getConfiguration().locale;

                if (current.getLanguage().equals("ar")) {
                    stationNames.add(user.getNamear());
                } else {
                    stationNames.add(user.getNameen());
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "JSONException");
        } finally {
            getMvpView().setStationsData(stationNames);

        }
    }

    @Override
    public void trainLocationReport(final Context contex, String stationId, String ts) {

        User user = mDataManager.getCurrentUser();
        if (user != null) {
            mDataManager.trainLocationReport(stationId, ts, user.mAccessToken, new JSONCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {


                    try {
                        LatLng latLng = new LatLng(jsonObject.getDouble("lat"),
                                jsonObject.getDouble("lng"));
                        String nextStation = jsonObject.getString("nextStation");
                        String daley = jsonObject.getString("delay");
                        try {

                            Intent intent = new Intent(LOCATION_REPORT);

                            intent.putExtra(LATITUDE, latLng.latitude);
                            intent.putExtra(LONGITUDE, latLng.longitude);
                            intent.putExtra(DELAY, daley);
                            intent.putExtra(NEXT_STATION, nextStation);

                            LocalBroadcastManager.getInstance(contex).sendBroadcast(intent);

                        } catch (NullPointerException e) {
                            Log.e("NPE", e.toString());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String errorMessage) {

                }
            });

        }
    }


}

