package in.radioactivegames.sekkah.ui.main.track.map;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.report.ReportFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackContract;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackPresenter;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;

import static in.radioactivegames.sekkah.ui.main.report.ReportFragment.isFirst;

public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback, TrackFragment.OnFragmentInteractionListener
{
    private View mFragment;
    private GoogleMap mMap;
    private MapView mMapView;
    private Marker mTrainMarker;
    private OnFragmentInteractionListener mListener;

    @Inject MapPresenter mPresenter;
    @BindView(R.id.btnReport) ImageView mBtnReport;

    private static final String TAG = MapFragment.class.getSimpleName();
    public static LatLng latLng=new LatLng(0,0);
    public static MapFragment newInstance()
    {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragment = inflater.inflate(R.layout.fragment_map, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));

        mMapView = mFragment.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mPresenter.onAttach(this);
        return mFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMapView.onResume();
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent)
    {
        fragmentComponent.inject(this);
    }

    @OnClick(R.id.btnReport)
    public void report()
    {
        /*getFragmentManager().beginTransaction()
                .add(R.id.frameMain, ReportFragment.newInstance(), "ReportFragment")
                .addToBackStack("ReportFragment")
                .commit();*/
        mListener.openReportFragment();
    }

    @Override
    public void setTrainLocation(final LatLng location)
    {
        try
        {
            latLng =location;
            if(isFirst){
               isFirst = false;
            }

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mTrainMarker != null)
                        mTrainMarker.remove();
                    mTrainMarker = mMap.addMarker(new MarkerOptions()
                            .position(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
                    Log.d(TAG, "Location: " + location.toString());
                }
            });
        }
        catch(NullPointerException e)
        {
            if (mTrainMarker != null)
                mTrainMarker.remove();
            mTrainMarker = mMap.addMarker(new MarkerOptions()
                    .position(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
            Log.d(TAG, "Location: " + location.toString());
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
        if(isFirst){
            setTrainLocation(latLng);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        public void openReportFragment();
    }

}
