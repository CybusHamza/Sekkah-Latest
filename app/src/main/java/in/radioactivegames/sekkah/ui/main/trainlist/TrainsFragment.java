package in.radioactivegames.sekkah.ui.main.trainlist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.data.Realm.RealmDB;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsUtils;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.utility.Constants;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static in.radioactivegames.sekkah.utility.Constants.KEY_FROM;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TO;
import static in.radioactivegames.sekkah.utility.Constants.LATITUDE;
import static in.radioactivegames.sekkah.utility.Constants.LOCATION;
import static in.radioactivegames.sekkah.utility.Constants.LONGITUDE;

public class TrainsFragment extends BaseFragment implements TrainsContract.View {
    private View mFragment;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    private SettingsClient mSettingsClient;
    private FusedLocationProviderClient mFusedLocationClient;
    String from, to;
    @BindView(R.id.rvTrns)
    RecyclerView mRecyclerView;

    @Inject
    TrainsPresenter mPresenter;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private static final String TAG = TrainsFragment.class.getSimpleName();

    public static TrainsFragment newInstance() {
        return new TrainsFragment();
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
        mFragment = inflater.inflate(R.layout.fragment_trains, container, false);

        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";

        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //mPresenter.getTrainData();

        Bundle bundle = getArguments();
        getTrainData(bundle);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        createLocationRequest();
        buildLocationSettingsRequest();
        createLocationCallback();

        return mFragment;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRequestingLocationUpdates = true;
    }

    public void getTrainData(Bundle bundle) {


        from = bundle.getString(KEY_FROM);
        to = bundle.getString(KEY_TO);

        Realm realm = Realm.getDefaultInstance();
        mPresenter.getTrainData(from, to, realm, getActivity());

    }


    private void updateLocationUI() {

        if (mCurrentLocation != null) {
            Log.d(TAG, mCurrentLocation.getLatitude() + "");
            Log.d(TAG, mCurrentLocation.getLongitude() + "");
            Log.d(TAG, mLastUpdateTime);

            SharedPrefsUtils.setStringPreference(getActivity(), LATITUDE, mCurrentLocation.getLatitude() + "");
            SharedPrefsUtils.setStringPreference(getActivity(), LONGITUDE, mCurrentLocation.getLongitude() + "");

        }
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
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            requestPermissions();
        }
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mRequestingLocationUpdates = false;
                //setButtonsEnabledState();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void setTrainPojoData(List<TrainPOJO> data) {
        if (data.size() > 0) {
            mAdapter = new TrainAdapter(data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(getActivity(), "No Data Found !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startTracking(final String trainId) {

        showProgressDialog("Please Wait...");

        String source;
        String destination;


        final String location = SharedPrefsUtils.getStringPreference(getActivity(), LATITUDE) + "," + SharedPrefsUtils.getStringPreference(getActivity(), LONGITUDE);
        String selectedLocation = SharedPrefsUtils.getStringPreference(getActivity(), LOCATION);

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

                hideProgressBar();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_TRAINID, trainId);

                TrackFragment trackFragment = TrackFragment.newInstance();
                trackFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .add(R.id.frameMain, trackFragment, "TrackFragment")
                        .addToBackStack("TrackFragment")
                        .commit();
            }

            @Override
            public void onFail(String errorMessage) {

                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.ViewHolder> {
        private List<TrainPOJO> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTvTrainNumber, mTvTrainClass, mTvDepartureStation, mTvDestinationStation;
            public TextView mTvDepartureTime, mTvDestinationTime;
            public ImageView mBtnTrack, mBtnReminder;

            public ViewHolder(View v) {
                super(v);
                mTvTrainNumber = v.findViewById(R.id.tvTrainNumber);
                mTvTrainClass = v.findViewById(R.id.tvTrainClass);
                mTvDepartureStation = v.findViewById(R.id.tvDepartureStation);
                mTvDestinationStation = v.findViewById(R.id.tvDestinationStation);
                mTvDepartureTime = v.findViewById(R.id.tvDepartureTime);
                mTvDestinationTime = v.findViewById(R.id.tvDestinationTime);
                mBtnTrack = v.findViewById(R.id.btnTrack);
                mBtnReminder = v.findViewById(R.id.btnReminder);
            }
        }

        public TrainAdapter(List<TrainPOJO> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public TrainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_train, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Locale current = getResources().getConfiguration().locale;
            String lan = current.getLanguage();
            final TrainPOJO trainPOJO = mDataset.get(position);
            if (lan.equals("ar")) {
                holder.mTvTrainClass.setText(trainPOJO.getNamear());
                holder.mTvDepartureStation.setText(trainPOJO.getDepStationAr());
                holder.mTvDestinationStation.setText(trainPOJO.getFinalStationAr());
            } else {
                holder.mTvTrainClass.setText(trainPOJO.getNameen());
                holder.mTvDepartureStation.setText(trainPOJO.getDepStation());
                holder.mTvDestinationStation.setText(trainPOJO.getFinalStation());
            }

            holder.mTvTrainNumber.setText(trainPOJO.getNumber());
            holder.mTvDepartureTime.setText(trainPOJO.getGetDepStationtime());
            holder.mTvDestinationTime.setText(trainPOJO.getFinalStationDepStationtime());
            holder.mBtnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTracking(trainPOJO.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
