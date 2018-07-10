package in.app.sekkah.ui.main.track.station;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.model.Station;
import in.app.sekkah.data.model.StationPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class StationPresenter extends BasePresenter<StationContract.View> implements StationContract.Presenter
{
    private static final String TAG = StationPresenter.class.getSimpleName();

    @Inject
    public StationPresenter()
    {
        List<Station> stations = new ArrayList<>();
    }


    @Override
    public void getStationData(Realm realm, String trainId) {


            ArrayList<StationPOJO> stationPOJOS = RealmDB.getinstance().getTrainStations(realm,trainId);


            getMvpView().setStationData(stationPOJOS);

    }
}
