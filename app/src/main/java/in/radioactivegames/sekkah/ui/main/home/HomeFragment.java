package in.radioactivegames.sekkah.ui.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.network.WebSocketHelper;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;
import io.realm.Realm;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private View mFragment;
    @Inject
    HomePresenter mPresenter;
    Realm realm;
    @BindView(R.id.spnDeparture)
    Spinner spnDeparture;
    @BindView(R.id.spnDestination)
    Spinner spnDestination;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_home, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        parsonJson(loadJSONFromAsset());
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getStationsData();
    }

    @Override
    public void onResume() {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        WebSocketHelper webSocketHelper = new WebSocketHelper();
        webSocketHelper.startScheduleTracking(accessToken, jsonCallback);
        super.onResume();

    }

    JSONCallback jsonCallback = new JSONCallback() {
        @Override
        public void onSuccess(final JSONObject jsonObject) {

            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();

            parsonJson(jsonObject);

        }

        @Override
        public void onFail(String errorMessage) {

        }
    };

    @Override
    public void setStationsData(List<String> data) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        spnDeparture.setAdapter(arrayAdapter);
        spnDestination.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btnTrains)
    public void viewTrains() {
        getFragmentManager().beginTransaction()
                .add(R.id.frameMain, TrainsFragment.newInstance(), "TrainsFragment")
                .addToBackStack("TrainsFragment")
                .commit();
    }


    public JSONObject loadJSONFromAsset() {
        String json = null;
        JSONObject jsonObject = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.shcedule);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void parsonJson(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("schedule");

            for (int i = 0; i < jsonArray.length(); i++) {

                final JSONObject jObject = jsonArray.getJSONObject(i);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        TrainPOJO trainPOJO = realm.createObject(TrainPOJO.class);

                        trainPOJO.setId(jObject.optString("_id", ""));
                        trainPOJO.setNameen(jObject.optString("nameen", ""));
                        trainPOJO.setNamear(jObject.optString("namear", ""));
                        trainPOJO.setLat(jObject.optDouble("lat", 0));
                        trainPOJO.setLng(jObject.optDouble("lng", 0));
                        trainPOJO.setUpdatedAt(jObject.optString("updatedAt", ""));
                        trainPOJO.setDelay(jObject.optString("delay", ""));

                        try {
                            JSONArray array = jObject.getJSONArray("stations");
                            JSONObject jsonObject1 = array.getJSONObject(0);
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("stationId");
                            trainPOJO.setDepStation(jsonObject2.optString("nameen"));
                            trainPOJO.setGetDepStationtime(jsonObject1.optString("ts"));

                            JSONObject jsonObject3 = array.getJSONObject(array.length() - 1);
                            JSONObject jsonObject4 = jsonObject3.getJSONObject("stationId");
                            trainPOJO.setFinalStation(jsonObject4.optString("nameen"));
                            trainPOJO.setFinalStationDepStationtime(jsonObject3.optString("ts"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


                JSONArray array = jObject.getJSONArray("stations");

                for (int j = 0; j < array.length(); j++) {
                    final JSONObject staitionObj = array.getJSONObject(j);

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {

                            StationPOJO stationPOJO = new StationPOJO();

                            stationPOJO.setId(staitionObj.optString("_id", ""));
                            stationPOJO.setNameen(staitionObj.optString("namear", ""));
                            stationPOJO.setNamear(staitionObj.optString("nameen", ""));
                            stationPOJO.setLat(staitionObj.optDouble("lat", 0));
                            stationPOJO.setLng(staitionObj.optDouble("lng", 0));
                            stationPOJO.setDistance(staitionObj.optInt("distance", 0));
                        }
                    });

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
