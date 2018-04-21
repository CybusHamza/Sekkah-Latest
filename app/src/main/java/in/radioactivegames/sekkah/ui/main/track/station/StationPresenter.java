package in.radioactivegames.sekkah.ui.main.track.station;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.data.other.MockData;
import in.radioactivegames.sekkah.ui.main.track.map.MapContract;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsPresenter;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class StationPresenter extends BasePresenter<StationContract.View> implements StationContract.Presenter
{
    private List<Station> stations;
    private static final String TAG = StationPresenter.class.getSimpleName();

    @Inject
    public StationPresenter()
    {
        stations = new ArrayList<>();
    }

    @Override
    public void getStationData()
    {
        try
        {
            for (int i = 0; i < MockData.nestedStations.length(); i++)
            {
                JSONObject jsonStation = MockData.nestedStations.getJSONObject(i);
                Station station = new Station();
                station.id = jsonStation.getInt("id");
                station.name = jsonStation.getString("name");
                station.arrivalTime = jsonStation.getString("arrivalTime");
                station.departureTime = jsonStation.getString("departureTime");
                stations.add(station);
            }
        }
        catch(JSONException ex)
        {
            Log.e(TAG, "JSONException");
        }
        finally
        {
            getMvpView().setStationData(stations);
        }
    }
}
