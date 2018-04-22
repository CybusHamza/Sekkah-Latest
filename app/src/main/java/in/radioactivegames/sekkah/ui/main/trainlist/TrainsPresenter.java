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
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.other.MockData;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by AntiSaby on 12/19/2017.
 * www.radioactivegames.in
 */

public class TrainsPresenter extends BasePresenter<TrainsContract.View> implements TrainsContract.Presenter
{
    private List<Train> trains;
    private List<TrainPOJO> trainsPojo;
    private static final String TAG = TrainsPresenter.class.getSimpleName();

    @Inject
    public TrainsPresenter()
    {
        trains = new ArrayList<>();
        trainsPojo = new ArrayList<>();
    }


    public void   getTrainData(Realm realm) {

        String data ="";
        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class);
        RealmResults<TrainPOJO> result1 = query.findAll();

        try{

            for (TrainPOJO user : result1) {

                TrainPOJO trainPOJO = new TrainPOJO();

                trainPOJO.setId(user.getId());
                trainPOJO.setNameen(user.getNameen());
                trainPOJO.setNamear(user.getNamear());
                trainPOJO.setLat(user.getLat());
                trainPOJO.setLng(user.getLng());
                trainPOJO.setUpdatedAt(user.getUpdatedAt());
                trainPOJO.setDelay(user.getDelay());
                trainPOJO.setDepStation(user.getDepStation());
                trainPOJO.setGetDepStationtime(user.getGetDepStationtime());
                trainPOJO.setFinalStation(user.getFinalStation());
                trainPOJO.setFinalStationDepStationtime(user.getFinalStationDepStationtime());

                trainsPojo.add(trainPOJO);


            }
        }catch (Exception e){
            Log.e(TAG, "JSONException");
        }
        finally {
            getMvpView().setTrainPojoData(trainsPojo);
        }

    }
}
