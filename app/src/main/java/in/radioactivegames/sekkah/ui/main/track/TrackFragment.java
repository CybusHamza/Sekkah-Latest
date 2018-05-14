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
import static in.radioactivegames.sekkah.utility.Constants.KEY_TRAINID;

public class TrackFragment extends BaseFragment implements TrackContract.View {
    private View mFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private OnFragmentInteractionListener mMapFragmentListener;


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

        Bundle bundle = getArguments();

        String trainId = "";
        if(bundle != null){
            if(bundle.containsKey(KEY_TRAINID)){
                trainId = bundle.getString(KEY_TRAINID);
            }
        }

        setupViewPager();
        mPresenter.trackTrain(trainId);
        mPresenter.startTrackUser();
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
        viewPagerAdapter.addFragment(mapFragment, getString(R.string.map));
        viewPagerAdapter.addFragment(stationFragment, getString(R.string.station));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setTrainLocation(LatLng location,String nextStation) {
        mMapFragmentListener.setTrainLocation(location,nextStation);

    }

    @Override
    public void onResume()
    {
        super.onResume();
//        setupViewPager();
//        mPresenter.trackTrain();
//        mPresenter.startTrackUser();
//        mRequestingLocationUpdates = true;
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
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mPresenter.stopTrackTrain();
    }

    public interface OnFragmentInteractionListener
    {
        void setTrainLocation(LatLng location ,final LatLng nextlocation , String ts ,String stationName,int color);

        void setTrainLocation(LatLng location,String nextStation);
    }
}
