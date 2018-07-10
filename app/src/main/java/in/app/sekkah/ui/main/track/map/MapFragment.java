package in.app.sekkah.ui.main.track.map;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.model.TrainPOJO;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.FragmentComponent;
import in.app.sekkah.ui.main.MainActivity;
import in.app.sekkah.ui.main.track.TrackFragment;
import in.app.sekkah.utility.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

import static in.app.sekkah.ui.main.report.ReportFragment.latLngTrain;
import static in.app.sekkah.utility.Constants.DELAY;
import static in.app.sekkah.utility.Constants.KEY_FROM;
import static in.app.sekkah.utility.Constants.KEY_TO;
import static in.app.sekkah.utility.Constants.KEY_TRAINID;
import static in.app.sekkah.utility.Constants.LATITUDE;
import static in.app.sekkah.utility.Constants.LOCATION;
import static in.app.sekkah.utility.Constants.LOCATION_REPORT;
import static in.app.sekkah.utility.Constants.LONGITUDE;
import static in.app.sekkah.utility.Constants.NEXT_STATION;


public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback, TrackFragment.OnFragmentInteractionListener {
    private GoogleMap mMap;
    private MapView mMapView;
    @BindView(R.id.tvDepartureStation)
    TextView tvDepartureStation;
    @BindView(R.id.tvDestinationStation)
    TextView tvDestinationStation;
    @BindView(R.id.tvTrainNumber)
    TextView tvTrainNumber;
    @BindView(R.id.tvNextStation)
    TextView tvNextStation;
    @BindView(R.id.tvDestinationTime)
    TextView tvDestinationTime;
    @BindView(R.id.tvDepartureTime)
    TextView tvDepartureTime;
    @BindView(R.id.tvLateTimeWorded)
    TextView tvLateTimeWorded;
    @BindView(R.id.tvLateTime)
    TextView tvLateTime;
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
    LatLng current, next;
    int count;

    public static String nextStationName;
    public static String nextStationdelay;
    public static String traindepartureTime;
    public static String traindestinationTime;
    public static String delayofTrain;


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
        View mFragment = inflater.inflate(R.layout.fragment_map, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));

        mMapView = mFragment.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mPresenter.onAttach(this);
        setTrainData();

        if(!Utils.isNetworkAvailable(getActivity())){
            networkDialog();
        }

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

        mPresenter.setuserRoute(source, destination, location, selectedLocation, trainId, new JSONCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                mPresenter.trackTrain(trainId);
            }

            @Override
            public void onFail(String errorMessage) {
              //  Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMapView.onResume();

        if (latLngTrain != null) {
            setTrainLocation(latLngTrain, "", "");
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


    public void checkLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Location is not enabled");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                   /* Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);*/
                    //get gps
                    displayLocationSettingsRequest(getActivity());
                }
            });
            dialog.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    getActivity().onBackPressed();
                }
            });
            dialog.show();
        } else {
            createUserRoute();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public void networkDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Internet is not enabled");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                getFragmentManager().popBackStack();
                //get gps
            }
        });

        dialog.show();
    }
    @Override
    public void setTrainLocation(final LatLng location, final LatLng nextlocation, String ts, final String stationName, final int color) {

        final String title = stationName + " : " + ts;

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (color == 0) {
                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.gray)));
                    } else {
                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());
                }
            });
        } catch (NullPointerException e) {
            if (color == 0) {
                mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.gray)));
            } else {
                mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(location).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
            Log.d(TAG, "Location: " + location.toString());
        }

        if (count == nextpos) {
            drwaRoute(next, location);
            count++;
        } else {
            count++;
        }

        drwaRoute(location, nextlocation);

    }


    BroadcastReceiver reportFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                mMap.clear();
                double lat = intent.getDoubleExtra(LATITUDE, 0);
                double lag = intent.getDoubleExtra(LONGITUDE, 0);

                String nextStation = intent.getStringExtra(NEXT_STATION);
                String delay = intent.getStringExtra(DELAY);
                tvNextStation.setText(nextStation);
                tvLateTimeWorded.setText(delay);
                setTrainLocation(new LatLng(lat, lag), nextStation, delay);
            }
        }
    };

    public void drwaRoute(LatLng location, LatLng nextlocation) {
        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();
        points.add(location);
        points.add(nextlocation);
        polyLineOptions.width(7);
        polyLineOptions.geodesic(true);
        polyLineOptions.color(getContext().getResources().getColor(R.color.colorAccen2));
        polyLineOptions.addAll(points);
        Polyline polyline = mMap.addPolyline(polyLineOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 8f));
        polyline.setGeodesic(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkLocation();
        mMapView.onResume();
        count = 0;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(reportFragment, new IntentFilter(LOCATION_REPORT));

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
        mMap.clear();
        mMapView.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(reportFragment);


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
                setMapDialogData(trainId);
            }

        }
    }

    int pos;
    int nextpos;

    @Override
    public void setTrainStaiton(ArrayList<StationPOJO> stationPOJOS) {

        ArrayList<LatLng> latlng = new ArrayList<>();

        for (int i = 0; i < stationPOJOS.size(); i++) {

            latlng.add(new LatLng(stationPOJOS.get(i).getLat(), stationPOJOS.get(i).getLng()));
        }

        for (int j = 0; j < latlng.size(); j++) {

            LatLng nextLatLng;
            String name;

            pos = latlng.indexOf(current);
            nextpos = latlng.indexOf(next);

            int current = j;
            if ( j <= pos && j != nextpos) {
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
                setTrainLocation(latlng.get(j), nextLatLng, ts, name, 0);
            } else if ( j != nextpos) {

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
                setTrainLocation(latlng.get(j), nextLatLng, ts, name, 1);
            }


        }
    }



    @Override
    public void setTrainLocation(final LatLng location, final String nextStation, final String delay) {


        Locale currentLan = getActivity().getResources().getConfiguration().locale;

        nextStationdelay = delay;

        if (currentLan.getLanguage().equals("ar")) {
            nextStationName = RealmDB.getinstance().getStationAr(nextStation, Realm.getDefaultInstance());
        } else {
            nextStationName = RealmDB.getinstance().getStation(nextStation, Realm.getDefaultInstance());
        }


        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    String timeOfArival = Utils.addTime(delay,tvDepartureTime.getText().toString());
                    tvLateTime.setText(timeOfArival);

                    delayofTrain = timeOfArival;

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));

                    tvNextStation.setText(nextStationName);
                    tvLateTimeWorded.setText(delay);

                    mTrainMarker.remove();

                /*    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location).title("Trains Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                */    current = location;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());

                    if (!nextStation.equals("")) {

                        RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getStationInfobyId(nextStation, Realm.getDefaultInstance());

                        LatLng latlng;
                        String StationName;
                        if (stationPOJOS.size() > 0) {
                            latlng = new LatLng(stationPOJOS.get(0).getLat(), stationPOJOS.get(0).getLng());
                            StationName = stationPOJOS.get(0).getNameen();
                        } else {
                            latlng = new LatLng(0, 0);
                            StationName = " ";
                        }

                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));


                        next = latlng;
                        mTrainMarker.remove();

                        mTrainMarker = mMap.addMarker(new MarkerOptions()
                                .position(latlng).title("Next Station " + StationName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                        Log.d(TAG, "Location: " + location.toString());
                        drwaRoute(current, latlng);
                    }

                    mPresenter.getTrainStaiton(trainId, Realm.getDefaultInstance());

                }

            });

        } catch (NullPointerException e) {

            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {

                tvNextStation.setText(nextStationName);
                tvLateTimeWorded.setText(delay);

                String timeOfArival = Utils.addTime(delay,tvDepartureTime.getText().toString());
                tvLateTime.setText(timeOfArival);

              /*  mTrainMarker = mMap.addMarker(new MarkerOptions()
                        .position(location).title("Trains Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
*/
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                current = location;
                if (!nextStation.equals("")) {

                    RealmResults<StationPOJO> stationPOJOS = RealmDB.getinstance().getStationInfobyId(nextStation, Realm.getDefaultInstance());

                    LatLng latlng;
                    if (stationPOJOS.size() > 0) {
                        latlng = new LatLng(stationPOJOS.get(0).getLat(), stationPOJOS.get(0).getLng());
                    } else {
                        latlng = new LatLng(0, 0);
                    }

                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(latlng).title("Next Station " + stationPOJOS.get(0).getNameen()).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    next = latlng;
                    Log.d(TAG, "Location: " + location.toString());

                    drwaRoute(current, latlng);
                }

                Log.d(TAG, "Location: " + location.toString());

                mPresenter.getTrainStaiton(trainId, Realm.getDefaultInstance());

            }


        }

    }

    public void setMapDialogData(String trainId) {

        TrainPOJO trainPOJO = RealmDB.getinstance().getTrainbyId(Realm.getDefaultInstance(), trainId);

        tvTrainNumber.setText(trainPOJO.getNumber());
        tvTrainClass.setText(trainPOJO.getNameen());
        tvDepartureTime.setText(trainPOJO.getGetDepStationtime());
        tvDestinationTime.setText(trainPOJO.getFinalStationDepStationtime());

        traindepartureTime = trainPOJO.getGetDepStationtime();
        traindestinationTime = trainPOJO.getFinalStationDepStationtime();
    }

    public interface OnFragmentInteractionListener {
        public void openReportFragment();
    }

}
