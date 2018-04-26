package in.radioactivegames.sekkah.ui.main.track;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.adapter.ViewPagerAdapter;
import in.radioactivegames.sekkah.ui.main.track.map.MapFragment;
import in.radioactivegames.sekkah.ui.main.track.station.StationFragment;

import static in.radioactivegames.sekkah.data.other.Constants.KEY_UPDATE;
import static in.radioactivegames.sekkah.utility.Constants.KEY_STATIONID;

public class TrackFragment extends BaseFragment implements TrackContract.View {
    private View mFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private OnFragmentInteractionListener mMapFragmentListener;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

    @Inject
    TrackPresenter mPresenter;
    @BindView(R.id.vpViewPager)
    ViewPager viewPager;
    @BindView(R.id.tlTabLayout)
    TabLayout tabLayout;

    private static final String TAG = TrackFragment.class.getSimpleName();

    public TrackFragment() {
    }

    public static TrackFragment newInstance() {
        return new TrackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());


        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_track, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        setupViewPager();

        mPresenter.onAttach(this);
        return mFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager();
        mPresenter.trackTrain();
        mPresenter.startTrackUser();
        mRequestingLocationUpdates = true;
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    private void setupViewPager() {

        Bundle bundle = getArguments();

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        MapFragment mapFragment = MapFragment.newInstance();
        mapFragment.setArguments(bundle);
        StationFragment stationFragment = StationFragment.newInstance();
        stationFragment.setArguments(bundle);
        mMapFragmentListener = mapFragment;
        viewPagerAdapter.addFragment(mapFragment, "Map");
        viewPagerAdapter.addFragment(stationFragment, "Station");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setTrainLocation(LatLng location) {
        mMapFragmentListener.setTrainLocation(location);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                requestPermissions();
            }

        /*mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>()
            {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse)
                {
                    Log.i(TAG, "All location settings are satisfied.");
                    try
                    {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }
                    catch (SecurityException e)
                    {
                        requestPermissions();
                    }
                    updateUI();
                }
            })
            .addOnFailureListener(getActivity(), new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode)
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ");
                            try
                            {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            }
                            catch (IntentSender.SendIntentException sie)
                            {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }
                    updateUI();
                }
            });*/
    }

    private void updateUI()
    {
        updateLocationUI();
    }

    private void updateLocationUI()
    {
        if (mCurrentLocation != null)
        {
            mPresenter.updateUserLocation(new LatLng
            (
                mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude()
            ));

            Log.d(TAG, mCurrentLocation.getLatitude() + "");
            Log.d(TAG, mCurrentLocation.getLongitude() + "");
            Log.d(TAG, mLastUpdateTime);
        }
    }

    private void stopLocationUpdates()
    {
        if (!mRequestingLocationUpdates)
        {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                mRequestingLocationUpdates = false;
                //setButtonsEnabledState();
            }
        });
    }

    private boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions()
    {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE)
        {
            if (grantResults.length <= 0)
            {
                Log.i(TAG, "User interaction was cancelled.");
            }
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (mRequestingLocationUpdates)
                {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        setupViewPager();
//        mPresenter.trackTrain();
//        mPresenter.startTrackUser();
//        mRequestingLocationUpdates = true;
        startLocationUpdates();
        /*if (mRequestingLocationUpdates && checkPermissions())
        {
            startLocationUpdates();
        }
        else if (!checkPermissions())
        {
            requestPermissions();
        }

        updateUI();*/
    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mPresenter.stopTrackTrain();
    }

    public interface OnFragmentInteractionListener
    {
        void setTrainLocation(LatLng location);
    }
}
