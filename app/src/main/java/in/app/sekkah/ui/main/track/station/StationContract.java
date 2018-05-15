package in.app.sekkah.ui.main.track.station;

import java.util.ArrayList;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;
import in.app.sekkah.data.model.StationPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class StationContract
{
    public interface View extends BaseMvpView
    {
        public void setStationData(ArrayList<StationPOJO> stationData);
    }

    public interface Presenter extends BaseMvpPresenter<StationContract.View>
    {
        public void getStationData(Realm realm , String trainId);
    }
}
