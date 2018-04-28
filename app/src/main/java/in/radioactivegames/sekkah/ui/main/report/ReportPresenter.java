package in.radioactivegames.sekkah.ui.main.report;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.other.MockData;
import in.radioactivegames.sekkah.ui.main.home.HomePresenter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by AntiSaby on 1/2/2018.
 * www.radioactivegames.in
 */

public class ReportPresenter extends BasePresenter<ReportContract.View> implements ReportContract.Presenter
{
    private List<Station> stations;
    private List<String> stationNames;
    private DataManager mDataManager;

    private static final String TAG = ReportPresenter.class.getSimpleName();

    @Inject
    public ReportPresenter(DataManager dataManager)
    {
        stations = new ArrayList<>();
        stationNames = new ArrayList<>();
        mDataManager = dataManager;
    }

    @Override
    public void getStationsData(Context context)
    {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getAllStation(realm);

        try{
            for (StationPOJO user : stationPOJOS) {
                Locale current = context.getResources().getConfiguration().locale;

                if (current.getLanguage().equals("ar")) {
                    stationNames.add(user.getNamear());
                }else {
                    stationNames.add(user.getNameen());
                }

            }
        }catch (Exception e){
            Log.e(TAG, "JSONException");
        }
        finally {
            getMvpView().setStationsData(stationNames);

        }
    }

    @Override
    public void trainLocationReport(String stationId, String ts) {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        mDataManager.trainLocationReport(stationId,ts, accessToken, new TrainLocationCallback()
        {
            @Override
            public void onLocationReceive(LatLng location)
            {
                getMvpView().setTrainLocation(location);
            }
        });
    }



}
