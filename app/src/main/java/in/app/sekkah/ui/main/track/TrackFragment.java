package in.app.sekkah.ui.main.track;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.di.component.FragmentComponent;
import in.app.sekkah.ui.adapter.ViewPagerAdapter;
import in.app.sekkah.ui.main.track.map.MapFragment;
import in.app.sekkah.ui.main.track.station.StationFragment;

import static in.app.sekkah.utility.Constants.KEY_TRAINID;

public class TrackFragment extends BaseFragment implements TrackContract.View {


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
        View mFragment = inflater.inflate(R.layout.fragment_track, container, false);
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
        //mPresenter.trackTrain(trainId);
        mPresenter.startTrackUser();
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    private void setupViewPager() {

        Bundle bundle = getArguments();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        MapFragment mapFragment = MapFragment.newInstance();
        mapFragment.setArguments(bundle);
        StationFragment stationFragment = StationFragment.newInstance();
        stationFragment.setArguments(bundle);
        OnFragmentInteractionListener mMapFragmentListener = mapFragment;
        viewPagerAdapter.addFragment(mapFragment, getString(R.string.map));
        viewPagerAdapter.addFragment(stationFragment, getString(R.string.station));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
    }

    public interface OnFragmentInteractionListener
    {
        void setTrainLocation(LatLng location ,final LatLng nextlocation , String ts ,String stationName,int color);

    }
}
