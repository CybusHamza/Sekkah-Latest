package in.radioactivegames.sekkah.ui.main.track;

import com.google.android.gms.maps.model.LatLng;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class TrackContract
{
    public interface View extends BaseMvpView
    {
        void setTrainLocation(LatLng location);
    }

    public interface Presenter extends BaseMvpPresenter<TrackContract.View>
    {
        void trackTrain();
        void trainLocationReport();
        void scheduleTracking();
        void stopTrackTrain();
        void startTrackUser();
        void updateUserLocation(LatLng location);
        void stopUpdateUserLocation();
    }
}
