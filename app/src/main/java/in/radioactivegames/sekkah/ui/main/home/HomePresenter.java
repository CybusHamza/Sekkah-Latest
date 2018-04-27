package in.radioactivegames.sekkah.ui.main.home;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.other.Constants;
import in.radioactivegames.sekkah.data.model.Station;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by AntiSaby on 11/29/2017.
 * www.radioactivegames.in
 */

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private int mUserLocation, mIDDepartureStation, mIDDestinationStation;
    private List<Station> stations;
    private List<String> stationNames;

    private static final String TAG = HomePresenter.class.getSimpleName();

    @Inject
    public HomePresenter() {
        mUserLocation = Constants.LOCATION_TRAIN; //Default location set to be in Train
        stations = new ArrayList<>();
        stationNames = new ArrayList<>();
    }

    @Override
    public void getStationsData(Context context) {
        Realm  realm = Realm.getDefaultInstance();
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
    public void parsonJson(JSONObject jsonObject) {
        try {

            Realm realm = Realm.getDefaultInstance();

            RealmResults<TrainPOJO> trainPOJORealmResults = RealmDB.getinstance().getTrains(realm);

            if (trainPOJORealmResults.size() > 0) {
                return;
            }

            JSONArray stationsArray = jsonObject.getJSONArray("stations");

            for (int j = 0; j < stationsArray.length(); j++) {
                final JSONObject staitionObj = stationsArray.getJSONObject(j);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        StationPOJO stationPOJO = realm.createObject(StationPOJO.class);

                        stationPOJO.setId(staitionObj.optString("id", ""));
                        stationPOJO.setNameen(staitionObj.optString("nameen", ""));
                        stationPOJO.setNamear(staitionObj.optString("namear", ""));
                        stationPOJO.setLat(staitionObj.optDouble("lat", 0));
                        stationPOJO.setLng(staitionObj.optDouble("lng", 0));
                        stationPOJO.setDistance(staitionObj.optInt("distance", 0));
                    }
                });
            }

            JSONArray trainsArray = jsonObject.getJSONArray("trains");


            for (int i = 0; i < trainsArray.length(); i++) {

                final JSONObject jObject = trainsArray.getJSONObject(i);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        TrainPOJO trainPOJO = realm.createObject(TrainPOJO.class);

                        trainPOJO.setId(jObject.optString("id", ""));
                        trainPOJO.setNumber(jObject.optString("number", ""));
                        trainPOJO.setNamear(jObject.optString("namear", ""));
                        trainPOJO.setNameen(jObject.optString("nameen", ""));

                        try {
                            JSONArray array = jObject.getJSONArray("stations");
                            JSONObject jsonObject1 = array.getJSONObject(0);
                            String stationId = jsonObject1.getString("stationId");
                            trainPOJO.setDepStation(RealmDB.getinstance().getStation(stationId, realm));
                            trainPOJO.setDepStationAr(RealmDB.getinstance().getStationAr(stationId, realm));

                            trainPOJO.setGetDepStationtime(jsonObject1.optString("ts"));

                            JSONObject jsonObject2 = array.getJSONObject(array.length() - 1);
                            String stationIds = jsonObject2.getString("stationId");
                            trainPOJO.setFinalStation(RealmDB.getinstance().getStation(stationIds, realm));
                            trainPOJO.setFinalStationAr(RealmDB.getinstance().getStationAr(stationIds, realm));
                            trainPOJO.setFinalStationDepStationtime(jsonObject2.optString("ts"));

                            RealmList<String> stationRealmList = new RealmList<>();
                            RealmList<String> tsRealmList = new RealmList<>();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject3 = array.getJSONObject(i);

                                Station station = new Station();

                                stationRealmList.add(jsonObject3.getString("stationId"));
                                tsRealmList.add(jsonObject3.optString("ts"));

                            }

                            trainPOJO.setStationPOJOS(stationRealmList);
                            trainPOJO.setTsList(tsRealmList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
