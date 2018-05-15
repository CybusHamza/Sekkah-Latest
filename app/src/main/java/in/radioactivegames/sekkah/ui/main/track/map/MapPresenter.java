package in.radioactivegames.sekkah.ui.main.track.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.User;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class MapPresenter extends BasePresenter<MapContract.View> implements MapContract.Presenter
{

    private DataManager mDataManager;

    @Inject
    public MapPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }

    @Override
    public void getTrainStaiton(String trainId, Realm realm) {

        ArrayList<StationPOJO> stationPOJOS = new ArrayList<>();
        try {

            stationPOJOS = RealmDB.getinstance().getTrainStations(realm,trainId);

        }finally {

            getMvpView().setTrainStaiton(stationPOJOS);
        }

    }

    @Override
    public void setuserRoute(String source, String destination, String currentLocation, String selectedLocation, String trainId, JSONCallback callback) {

        User user = mDataManager.getCurrentUser();
        if(user != null){
            mDataManager.userroute(user.mAccessToken,source,destination,currentLocation,selectedLocation,trainId,callback);
        }
    }
}
