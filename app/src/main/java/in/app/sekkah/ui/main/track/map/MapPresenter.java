package in.app.sekkah.ui.main.track.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.model.User;
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

    @Override
    public void trackTrain(String trainId) {
        //String accessToken = mDataManager.getCurrentUser().mAccessToken;
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        mDataManager.trackTrain(trainId, accessToken, new JSONCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LatLng latLng;
                try {
                    latLng = new LatLng(jsonObject.getDouble("lat"),
                            jsonObject.getDouble("lng"));
                    String nextStation = jsonObject.getString("nextStation");
                    try {
                        getMvpView().setTrainLocation(latLng,nextStation);

                    }catch (NullPointerException e){
                        Log.e("NPE" , e.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String errorMessage) {

            }
        });
    }
}
