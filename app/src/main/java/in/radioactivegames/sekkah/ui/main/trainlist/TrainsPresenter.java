package in.radioactivegames.sekkah.ui.main.trainlist;

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

/**
 * Created by AntiSaby on 12/19/2017.
 * www.radioactivegames.in
 */

public class TrainsPresenter extends BasePresenter<TrainsContract.View> implements TrainsContract.Presenter
{
    private List<Train> trains;
    private static final String TAG = TrainsPresenter.class.getSimpleName();

    @Inject
    public TrainsPresenter()
    {
        trains = new ArrayList<>();
    }

    @Override
    public void getTrainData()
    {
        try
        {
            for (int i = 0; i < MockData.trains.length(); i++)
            {
                JSONObject jsonTrain = MockData.trains.getJSONObject(i);
                Train train = new Train();
                train.id = jsonTrain.getInt("id");
                train.trainNumber = jsonTrain.getInt("trainNumber");
                train.classEn = jsonTrain.getString("classEn");
                train.classAr = jsonTrain.getString("classAr");
                train.departureTime = jsonTrain.getString("departureTime");
                train.departureStation = jsonTrain.getString("departureStation");
                train.destinationStation = jsonTrain.getString("destinationStation");
                train.destinationTime = jsonTrain.getString("destinationTime");
                trains.add(train);
            }
        }
        catch(JSONException ex)
        {
            Log.e(TAG, "JSONException");
        }
        finally
        {
            getMvpView().setTrainData(trains);
        }
    }
}
