package in.radioactivegames.sekkah.ui.main.home;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.other.Constants;
import in.radioactivegames.sekkah.data.other.MockData;
import in.radioactivegames.sekkah.data.model.Station;

/**
 * Created by AntiSaby on 11/29/2017.
 * www.radioactivegames.in
 */

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter
{
    private int mUserLocation, mIDDepartureStation, mIDDestinationStation;
    private List<Station> stations;
    private List<String> stationNames;

    private static final String TAG = HomePresenter.class.getSimpleName();

    @Inject
    public HomePresenter()
    {
        mUserLocation = Constants.LOCATION_TRAIN; //Default location set to be in Train
        stations = new ArrayList<>();
        stationNames = new ArrayList<>();
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
}
