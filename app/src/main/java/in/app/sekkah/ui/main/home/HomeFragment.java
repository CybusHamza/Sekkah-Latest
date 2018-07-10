package in.app.sekkah.ui.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.data.LocationSelect;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.network.WebSocketHelper;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.FragmentComponent;
import in.app.sekkah.ui.main.trainlist.TrainsFragment;

import static in.app.sekkah.utility.Constants.KEY_FROM;
import static in.app.sekkah.utility.Constants.KEY_TO;
import static in.app.sekkah.utility.Constants.LOCATION;
import static in.app.sekkah.utility.Constants.RELOAD_DATASOURCE;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    @Inject
    HomePresenter mPresenter;
    @BindView(R.id.spnDeparture)
    Spinner spnDeparture;
    @BindView(R.id.spnDestination)
    Spinner spnDestination;
    @BindView (R.id.rgLocation)
    RadioGroup rgType;
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
        View mFragment = inflater.inflate(R.layout.fragment_home, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        realmDB = RealmDB.getinstance();
        mPresenter.parsonJson(loadJSONFromAsset());
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getStationsData(getActivity());
    }

    @Override
    public void onResume() {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";
        WebSocketHelper webSocketHelper = new WebSocketHelper();
        if(!SharedPrefsUtils.getBooleanPreference(getActivity(),RELOAD_DATASOURCE,false)){
            webSocketHelper.startScheduleTracking(accessToken, new JSONCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    mPresenter.parsonTrainJson(jsonObject);
                    SharedPrefsUtils.setBooleanPreference(getActivity(),RELOAD_DATASOURCE,true);
                }

                @Override
                public void onFail(String errorMessage) {

                }
            });
        }
        super.onResume();

    }




    @Override
    public void setStationsData(List<String> data) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_dropdown_item_1line, data){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.WHITE);


                return tv;
            }
        };

        spnDeparture.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_dropdown_item_1line, data){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.WHITE);


                return tv;
            }
        };
        spnDestination.setAdapter(arrayAdapter1);

        int spinnerPosition = arrayAdapter.getPosition(SharedPrefsUtils.getStringPreference(getContext(),KEY_FROM));
        spnDeparture.setSelection(spinnerPosition);

        int spinner2Position = arrayAdapter1.getPosition(SharedPrefsUtils.getStringPreference(getContext(),KEY_TO));
        spnDestination.setSelection(spinner2Position);
    }

    @OnClick(R.id.btnTrains)
    public void viewTrains() {

        Bundle bundle = new Bundle();

        bundle.putString(KEY_FROM, spnDeparture.getSelectedItem().toString());
        bundle.putString(KEY_TO, spnDestination.getSelectedItem().toString());

        int radioButtonID = rgType.getCheckedRadioButtonId();
        View radioButton = rgType.findViewById(radioButtonID);
        int idx = rgType.indexOfChild(radioButton);

        RadioButton r = (RadioButton)  rgType.getChildAt(idx);
        if(r == null){
            Toast.makeText(getActivity(), "Pease Select Location", Toast.LENGTH_SHORT).show();
            return;
        }
        String strType = r.getText().toString();

        int location ;
        if (strType.equals(getString(R.string.text_atstation))){
            location = LocationSelect.ATSTATION.ordinal();
        }else if(strType.equals(getString(R.string.text_ontrain))){
            location = LocationSelect.ONTRAIN.ordinal();
        }else {
            location = LocationSelect.JUSTTRACKING.ordinal();
        }

        if(spnDeparture.getSelectedItem().toString().equals(spnDestination.getSelectedItem().toString())) {
            Toast.makeText(getActivity(),"Please Enter Valid Source and Destination", Toast.LENGTH_SHORT).show();
            return;
          }

        SharedPrefsUtils.setStringPreference(getContext(),KEY_FROM,spnDeparture.getSelectedItem().toString());
        SharedPrefsUtils.setStringPreference(getContext(),LOCATION,location+"");
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
