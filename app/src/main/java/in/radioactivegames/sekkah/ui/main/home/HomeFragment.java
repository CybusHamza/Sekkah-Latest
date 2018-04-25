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
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.network.WebSocketHelper;
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsUtils;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;
import io.realm.Realm;
import io.realm.RealmResults;

import static in.radioactivegames.sekkah.utility.Constants.KEY_FROM;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TO;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private View mFragment;
    @Inject
    HomePresenter mPresenter;
    @BindView(R.id.spnDeparture)
    Spinner spnDeparture;
    @BindView(R.id.spnDestination)
    Spinner spnDestination;

    RealmDB realmDB;

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
        realmDB = RealmDB.getinstance();
        mPresenter.parsonJson(loadJSONFromAsset());
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
       // webSocketHelper.startScheduleTracking(accessToken, jsonCallback);
        super.onResume();

    }

    JSONCallback jsonCallback = new JSONCallback() {
        @Override
        public void onSuccess(final JSONObject jsonObject) {
            mPresenter.parsonJson(jsonObject);

        }

        @Override
        public void onFail(String errorMessage) {

        }
    };

    @Override
    public void setStationsData(List<String> data) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        spnDeparture.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        spnDestination.setAdapter(arrayAdapter1);

        int spinnerPosition = arrayAdapter.getPosition(SharedPrefsUtils.getStringPreference(getContext(),KEY_FROM));
        spnDeparture.setSelection(spinnerPosition);

        int spinner2Position = arrayAdapter.getPosition(SharedPrefsUtils.getStringPreference(getContext(),KEY_FROM));
        spnDestination.setSelection(spinner2Position);
    }

    @OnClick(R.id.btnTrains)
    public void viewTrains() {

        Bundle bundle = new Bundle();

        bundle.putString(KEY_FROM, spnDeparture.getSelectedItem().toString());
        bundle.putString(KEY_TO, spnDestination.getSelectedItem().toString());

        SharedPrefsUtils.setStringPreference(getContext(),KEY_FROM,spnDeparture.getSelectedItem().toString());

        SharedPrefsUtils.setStringPreference(getContext(),KEY_TO,spnDestination.getSelectedItem().toString());

        TrainsFragment trainsFragment = TrainsFragment.newInstance();
        trainsFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .add(R.id.frameMain, trainsFragment, "TrainsFragment")
                .addToBackStack("TrainsFragment")
                .commit();
    }


    public JSONObject loadJSONFromAsset() {
        String json = null;
        JSONObject jsonObject = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.schedule);
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

}
