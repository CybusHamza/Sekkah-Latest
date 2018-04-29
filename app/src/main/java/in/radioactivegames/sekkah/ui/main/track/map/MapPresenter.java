package in.radioactivegames.sekkah.ui.main.track.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class MapPresenter extends BasePresenter<MapContract.View> implements MapContract.Presenter
{
    @Inject
    public MapPresenter()
    {


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
}
