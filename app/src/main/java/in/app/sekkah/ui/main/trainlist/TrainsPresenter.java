package in.app.sekkah.ui.main.trainlist;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.model.Train;
import in.app.sekkah.data.model.TrainPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/19/2017.
 * www.radioactivegames.in
 */

public class TrainsPresenter extends BasePresenter<TrainsContract.View> implements TrainsContract.Presenter
{
    private static final String TAG = TrainsPresenter.class.getSimpleName();
    RealmDB realmDB ;

    @Inject
    public TrainsPresenter(DataManager dataManager)
    {
        List<Train> trains = new ArrayList<>();
        realmDB =  RealmDB.getinstance();
        DataManager mDataManager = dataManager;
    }


    @Override
    public void getTrainData(String fromStation, String toStaion, Realm realm, Context context) {

        String data ="";

        ArrayList<TrainPOJO> result1;

        List<TrainPOJO> trainsPojo = new ArrayList<>();

        Locale current = context.getResources().getConfiguration().locale;

        if (current.getLanguage().equals("ar")) {
            result1= realmDB.getTrainListfromStationAr(fromStation,toStaion,realm);
        }else {
            result1= realmDB.getTrainListfromStation(fromStation,toStaion,realm);
        }

        try{

            for (int i=result1.size()-1; i>=0 ;i--){

                TrainPOJO user  = result1.get(i);

                TrainPOJO trainPOJO = new TrainPOJO();

                trainPOJO.setId(user.getId());
                trainPOJO.setNameen(user.getNameen());
                trainPOJO.setNamear(user.getNamear());
                trainPOJO.setNumber(user.getNumber());
                trainPOJO.setFinalStationAr(user.getFinalStationAr());
                trainPOJO.setDepStationAr(user.getDepStationAr());
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
