package in.radioactivegames.sekkah.ui.main.track.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
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
    public void getTrainStaiton(String stationId, Realm realm) {

        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        try {

            latLngArrayList = RealmDB.getinstance().getTrainStations(realm,stationId);

        }finally {

            getMvpView().setTrainStaiton(latLngArrayList);
        }

    }
}
