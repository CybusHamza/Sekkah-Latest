package in.radioactivegames.sekkah.ui.main.track;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.DataManager;
import in.radioactivegames.sekkah.data.callbacks.TrainLocationCallback;
import in.radioactivegames.sekkah.data.model.User;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class TrackPresenter extends BasePresenter<TrackContract.View> implements TrackContract.Presenter
{

    private DataManager mDataManager;

    String accessToken;


    @Inject
    public TrackPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
        if(mDataManager.getCurrentUser() != null){
            accessToken = mDataManager.getCurrentUser().mAccessToken;
        }else {
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        }
    }

    @Override
    public void trackTrain(String trainId)
    {
        //String accessToken = mDataManager.getCurrentUser().mAccessToken;
       String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        mDataManager.trackTrain("5a6475ec457d1b10b4bb38fa", accessToken, new TrainLocationCallback()
        {
            @Override
            public void onLocationReceive(LatLng location)
            {
                getMvpView().setTrainLocation(location);
            }
        });
    }

    @Override
    public void trainLocationReport() {
       // String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        mDataManager.trainLocationReport("5a6475ec457d1b10b4bb38fa",new Timestamp(System.currentTimeMillis()).toString(), accessToken, new TrainLocationCallback()
        {
            @Override
            public void onLocationReceive(LatLng location)
            {
                getMvpView().setTrainLocation(location);
            }
        });
    }

    @Override
    public void scheduleTracking() {
        //String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
    }

    @Override
    public void stopTrackTrain()
    {
        mDataManager.stopTrackTrain("5a6475ec457d1b10b4bb3928");
    }

    //TODO: Take a look at Presenter's and View's responsibilities.

    @Override
    public void startTrackUser()
    {
        User user = mDataManager.getCurrentUser();
        if(user != null){
            mDataManager.startTrackUser("5a6475ec457d1b10b4bb3928", user.mAccessToken);
        }

        //mDataManager.startTrackUser("5a40dab6734d1d5b99260f85", mDataManager.getCurrentUser().mAccessToken);

    }

    @Override
    public void updateUserLocation(LatLng location)
    {
        mDataManager.updateUser(location);
    }

    @Override
    public void stopUpdateUserLocation()
    {
        mDataManager.stopTrackUser();
    }

}
