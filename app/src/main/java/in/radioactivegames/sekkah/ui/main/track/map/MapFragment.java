package in.radioactivegames.sekkah.ui.main.track.map;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

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
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsUtils;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.report.ReportFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackContract;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackPresenter;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;
import in.radioactivegames.sekkah.utility.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

import static in.radioactivegames.sekkah.ui.main.report.ReportFragment.latLngTrain;
import static in.radioactivegames.sekkah.utility.Constants.KEY_FROM;
import static in.radioactivegames.sekkah.utility.Constants.KEY_STATIONID;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TO;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TRAINID;
import static in.radioactivegames.sekkah.utility.Constants.LATITUDE;
import static in.radioactivegames.sekkah.utility.Constants.LOCATION;
import static in.radioactivegames.sekkah.utility.Constants.LONGITUDE;


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
    String from, to;
    LatLng current , next;
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


    public void createUserRoute() {

        String source;
        String destination;

        String location = SharedPrefsUtils.getStringPreference(getActivity(), LATITUDE) + "," + SharedPrefsUtils.getStringPreference(getActivity(), LONGITUDE);
        String selectedLocation = SharedPrefsUtils.getStringPreference(getActivity(), LOCATION);

        if (location.equals(",")) {
            location = "0,0";
        }

        Locale current = getActivity().getResources().getConfiguration().locale;

        if (current.getLanguage().equals("ar")) {
            source = RealmDB.getinstance().getStationbyNameAr(from, Realm.getDefaultInstance());
        } else {
            source = RealmDB.getinstance().getStationbyName(from, Realm.getDefaultInstance());
        }

        if (current.getLanguage().equals("ar")) {
            destination = RealmDB.getinstance().getStationbyNameAr(to, Realm.getDefaultInstance());
        } else {
            destination = RealmDB.getinstance().getStationbyName(to, Realm.getDefaultInstance());
        }

        mPresenter.setuserRoute(source, destination, location, selectedLocation, "5ae707158cecad66917674ad", new JSONCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {


            }

            @Override
            public void onFail(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapView.onResume();

        if (latLngTrain != null) {
            setTrainLocation(latLngTrain,"");
        }

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




    public void checkLocation(){
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("GPS is not enabled");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    getActivity().onBackPressed();
                }
            });
            dialog.show();
        }else {
            createUserRoute();
        }
    }


    @Override
    public void setTrainLocation(final LatLng location, final LatLng nextlocation, String ts, final String stationName, final int color) {

        final String title = stationName + " : " + ts;
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(color ==0){
                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(location).title(title) .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    }else {
                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(location).title(title) .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    }
                     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());
                }
            });
        } catch (NullPointerException e) {
            if(color == 0){
                mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(location).title(title) .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }else {
                mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(location).title(title) .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,8f));
        polyline.setGeodesic(true);

    }

    @Override
    public void setTrainLocation(final LatLng location, final String nextStation) {
        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location));

                    mTrainMarker.remove();

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location).title("Trains Location"));
                    current = location;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());

                    if(!nextStation.equals("")) {

                        RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getStationInfobyId(nextStation,Realm.getDefaultInstance());

                        LatLng latlng;
                        String StationName;
                        if(stationPOJOS.size() >0){
                            latlng=  new LatLng(stationPOJOS.get(0).getLat(),stationPOJOS.get(0).getLng());
                            StationName = stationPOJOS.get(0).getNameen();
                        }
                        else {
                            latlng=  new LatLng(0,0 );
                            StationName = " ";
                        }

                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(latlng));

                        next = latlng;
                        mTrainMarker.remove();

                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(latlng).title("Next Station "+ StationName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                        Log.d(TAG, "Location: " + location.toString());
                    }

                    mPresenter.getTrainStaiton(trainId, Realm.getDefaultInstance());

                }

            });
        } catch (NullPointerException e) {
            mTrainMarker = mMap.addMarker(new MarkerOptions()
                    .position(location).title("Trains Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
            current = location;
            if(!nextStation.equals("")) {

                RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getStationInfobyId(nextStation,Realm.getDefaultInstance());

                LatLng latlng;
                if(stationPOJOS.size() >0){
                    latlng=  new LatLng(stationPOJOS.get(0).getLat(),stationPOJOS.get(0).getLng());
                }
                else {
                    latlng=  new LatLng(0,0 );
                }

                mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(latlng).title("Next Station "+ stationPOJOS.get(0).getNameen())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                next = latlng;
                Log.d(TAG, "Location: " + location.toString());
            }

            Log.d(TAG, "Location: " + location.toString());

            mPresenter.getTrainStaiton(trainId, Realm.getDefaultInstance());
        }




    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocation();
        mMapView.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();
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

    public void setTrainData() {

        from = SharedPrefsUtils.getStringPreference(getContext(), KEY_FROM);
        to = SharedPrefsUtils.getStringPreference(getContext(), KEY_TO);

        tvDepartureStation.setText(from);
        tvDestinationStation.setText(to);

        Locale currents = getResources().getConfiguration().locale;
        lan = currents.getLanguage();

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_TRAINID)) {
                trainId = bundle.getString(KEY_TRAINID);
            }

        }
    }

    @Override
    public void setTrainStaiton(ArrayList<StationPOJO> stationPOJOS) {

        ArrayList<LatLng> latlng = new ArrayList<>() ;

        for (int i = 0; i < stationPOJOS.size(); i++) {

            latlng.add(new LatLng(stationPOJOS.get(i).getLat(), stationPOJOS.get(i).getLng()));
        }

        for(int j=0;j<latlng.size();j++){

            LatLng nextLatLng;
            String name;

            int pos = latlng.indexOf(current);
            int nextpos = latlng.indexOf(next);

            int current = j;
            if(j!= pos && j<pos && j != nextpos){
                if (current < stationPOJOS.size() - 1) {
                    current++;
                    nextLatLng = latlng.get(current);
                } else {
                    nextLatLng = latlng.get(j);
                }

                if (lan.equals("ar")) {
                    name = stationPOJOS.get(j).getNamear();
                } else {
                    name = stationPOJOS.get(j).getNameen();
                }

                String ts = stationPOJOS.get(j).getTs();
                setTrainLocation(latlng.get(j), nextLatLng, ts, name,0);
            }else  if(j!= pos && j!= nextpos) {

                if (current < stationPOJOS.size() - 1) {
                    current++;
                    nextLatLng = latlng.get(current);
                } else {
                    nextLatLng = latlng.get(j);
                }

                if (lan.equals("ar")) {
                    name = stationPOJOS.get(j).getNamear();
                } else {
                    name = stationPOJOS.get(j).getNameen();
                }

                String ts = stationPOJOS.get(j).getTs();
                setTrainLocation(latlng.get(j), nextLatLng, ts, name,1);
            }


        }
    }

    public interface OnFragmentInteractionListener {
        public void openReportFragment();
    }

}
