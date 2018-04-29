package in.radioactivegames.sekkah.ui.main.track.map;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsUtils;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.report.ReportFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackContract;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackPresenter;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;
import io.realm.Realm;

import static in.radioactivegames.sekkah.utility.Constants.KEY_FROM;
import static in.radioactivegames.sekkah.utility.Constants.KEY_STATIONID;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TO;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TRAINID;


public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback, TrackFragment.OnFragmentInteractionListener {
    private View mFragment;
    private GoogleMap mMap;
    private MapView mMapView;
    @BindView(R.id.tvDepartureStation)
    TextView tvDepartureStation;
    @BindView(R.id.tvDestinationStation)
    TextView tvDestinationStation;
    @BindView(R.id.tvTrainNumber)
    TextView tvTrainNumber;
    @BindView(R.id.tvTrainClass)
    TextView tvTrainClass;
    private Marker mTrainMarker;
    private OnFragmentInteractionListener mListener;
    String trainId = "";
    @Inject
    MapPresenter mPresenter;
    @BindView(R.id.btnReport)
    ImageView mBtnReport;
    String lan;
    private static final String TAG = MapFragment.class.getSimpleName();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_map, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));

        mMapView = mFragment.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mPresenter.onAttach(this);

        setTrainData();

        return mFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapView.onResume();

        mPresenter.getTrainStaiton(trainId, Realm.getDefaultInstance());

        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @OnClick(R.id.btnReport)
    public void report() {
        /*getFragmentManager().beginTransaction()
                .add(R.id.frameMain, ReportFragment.newInstance(), "ReportFragment")
                .addToBackStack("ReportFragment")
                .commit();*/
        mListener.openReportFragment();
    }


    public void setTrainData() {
        tvDepartureStation.setText(SharedPrefsUtils.getStringPreference(getContext(), KEY_FROM));
        tvDestinationStation.setText(SharedPrefsUtils.getStringPreference(getContext(), KEY_TO));

        Locale currents = getResources().getConfiguration().locale;
        lan = currents.getLanguage();

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_TRAINID)) {
                trainId = bundle.getString(KEY_TRAINID);
            }
        }

        TrainPOJO trainPOJO = RealmDB.getinstance().getTrainbyId(Realm.getDefaultInstance(), trainId);

        tvTrainNumber.setText(trainPOJO.getNumber());


        if (lan.equals("ar")) {
            tvTrainClass.setText(trainPOJO.getNamear());
        } else {
            tvTrainClass.setText(trainPOJO.getNameen());
        }

        tvTrainClass.setText(trainPOJO.getNameen());
    }

    @Override
    public void setTrainLocation(final LatLng location, final LatLng nextlocation, String ts, final String stationName) {

        final String title = stationName + " : " + ts;
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());
                }
            });
        } catch (NullPointerException e) {
            mTrainMarker = mMap.addMarker(new MarkerOptions()
                    .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
            Log.d(TAG, "Location: " + location.toString());
        }

        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();
        points.add(location);
        points.add(nextlocation);
        polyLineOptions.width(7 * 1);
        polyLineOptions.geodesic(true);
        polyLineOptions.color(getContext().getResources().getColor(R.color.colorAccen2));
        polyLineOptions.addAll(points);
        Polyline polyline = mMap.addPolyline(polyLineOptions);
        polyline.setGeodesic(true);

    }

    @Override
    public void setTrainLocation(final LatLng location) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location).title("Current Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());
                }
            });
        } catch (NullPointerException e) {
            mTrainMarker = mMap.addMarker(new MarkerOptions()
                    .position(location).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
            Log.d(TAG, "Location: " + location.toString());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setTrainStaiton(ArrayList<StationPOJO> stationPOJOS) {

        for (int i = 0; i < stationPOJOS.size(); i++) {

            LatLng latlng = new LatLng(stationPOJOS.get(i).getLat(), stationPOJOS.get(i).getLng());
            LatLng nextLatLng;
            String name;
            int current = i;
            if (current < stationPOJOS.size() - 1) {
                current++;
                nextLatLng = new LatLng(stationPOJOS.get(current).getLat(), stationPOJOS.get(current).getLng());
            } else {
                nextLatLng = new LatLng(stationPOJOS.get(i).getLat(), stationPOJOS.get(i).getLng());
            }

            if (lan.equals("ar")) {
                name = stationPOJOS.get(i).getNamear();
            } else {
                name = stationPOJOS.get(i).getNameen();
            }

            String ts = stationPOJOS.get(i).getTs();
            setTrainLocation(latlng, nextLatLng, ts, name);
        }
    }

    public interface OnFragmentInteractionListener {
        public void openReportFragment();
    }

}
