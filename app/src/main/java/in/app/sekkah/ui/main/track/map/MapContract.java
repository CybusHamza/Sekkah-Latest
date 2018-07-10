package in.app.sekkah.ui.main.track.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.StationPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class MapContract
{
    public interface View extends BaseMvpView
    {
        public void setTrainStaiton( ArrayList<StationPOJO> stationPOJOS);
        public void setTrainLocation(LatLng location, String nextStation,String delay);
    }

    public interface Presenter extends BaseMvpPresenter<MapContract.View>
    {
        public void getTrainStaiton(String stationId, Realm realm);
        public void setuserRoute(String source,String destination,String currentLocation, String selectedLocation,String trainId,JSONCallback callback);
        public void trackTrain(String trainId);
    }
}
