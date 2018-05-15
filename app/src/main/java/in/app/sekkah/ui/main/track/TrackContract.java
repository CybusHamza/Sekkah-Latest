package in.app.sekkah.ui.main.track;

import com.google.android.gms.maps.model.LatLng;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class TrackContract
{
    public interface View extends BaseMvpView
    {

    }

    public interface Presenter extends BaseMvpPresenter<TrackContract.View>
    {
        void trainLocationReport();
        void scheduleTracking();
        void stopTrackTrain();
        void startTrackUser();
        void updateUserLocation(LatLng location);
        void stopUpdateUserLocation();
    }
}
