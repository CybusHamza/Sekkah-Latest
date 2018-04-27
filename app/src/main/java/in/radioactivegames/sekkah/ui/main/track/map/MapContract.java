package in.radioactivegames.sekkah.ui.main.track.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class MapContract
{
    public interface View extends BaseMvpView
    {
        public void setTrainStaiton(ArrayList<LatLng> latLngs);
    }

    public interface Presenter extends BaseMvpPresenter<MapContract.View>
    {
        public void getTrainStaiton(String stationId, Realm realm);
    }
}
