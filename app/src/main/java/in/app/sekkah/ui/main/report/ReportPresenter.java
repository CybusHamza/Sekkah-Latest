package in.app.sekkah.ui.main.report;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import in.app.sekkah.base.BasePresenter;
import in.app.sekkah.data.DataManager;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.callbacks.TrainLocationCallback;
import in.app.sekkah.data.model.Station;
import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.model.User;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by AntiSaby on 1/2/2018.
 * www.radioactivegames.in
 */

public class ReportPresenter extends BasePresenter<ReportContract.View> implements ReportContract.Presenter
{
    private List<Station> stations;
    private List<String> stationNames;
    private DataManager mDataManager;

    private static final String TAG = ReportPresenter.class.getSimpleName();

    @Inject
    public ReportPresenter(DataManager dataManager)
    {
        stations = new ArrayList<>();
        stationNames = new ArrayList<>();
        mDataManager = dataManager;
    }

    @Override
    public void getStationsData(Context context)
    {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getAllStation(realm);

        try{
            for (StationPOJO user : stationPOJOS) {
                Locale current = context.getResources().getConfiguration().locale;

                if (current.getLanguage().equals("ar")) {
                    stationNames.add(user.getNamear());
                }else {
                    stationNames.add(user.getNameen());
                }

            }
        }catch (Exception e){
            Log.e(TAG, "JSONException");
        }
        finally {
            getMvpView().setStationsData(stationNames);

        }
    }

    @Override
    public void trainLocationReport(String stationId, String ts) {

        User user = mDataManager.getCurrentUser();
        if(user != null){
            mDataManager.trainLocationReport(stationId,ts, user.mAccessToken, new TrainLocationCallback()
            {
                @Override
                public void onLocationReceive(LatLng location)
                {
                    getMvpView().setTrainLocation(location);
                }
            });

        }

    }



}
