package in.radioactivegames.sekkah.ui.main.report;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.other.MockData;
import in.radioactivegames.sekkah.ui.main.home.HomePresenter;

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
    public void getStationsData()
    {
        try
        {
            for (int i = 0; i < MockData.stations.length(); i++)
            {
                JSONObject jsonStation = MockData.stations.getJSONObject(i);
                Station station = new Station();
                station.id = jsonStation.getInt("id");
                station. name = jsonStation.getString("name");
                stations.add(station);
                stationNames.add(station.name);
            }
        }
        catch(JSONException ex)
        {
            Log.e(TAG, "JSONException");
        }
        finally
        {
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
