package in.app.sekkah.ui.main.track.station;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.FragmentComponent;
import io.realm.Realm;

import static in.app.sekkah.ui.main.track.map.MapFragment.delayofTrain;
import static in.app.sekkah.ui.main.track.map.MapFragment.nextStationName;
import static in.app.sekkah.ui.main.track.map.MapFragment.nextStationdelay;
import static in.app.sekkah.ui.main.track.map.MapFragment.traindepartureTime;
import static in.app.sekkah.ui.main.track.map.MapFragment.traindestinationTime;
import static in.app.sekkah.utility.Constants.KEY_FROM;
import static in.app.sekkah.utility.Constants.KEY_TO;
import static in.app.sekkah.utility.Constants.KEY_TRAINID;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends BaseFragment implements StationContract.View
{
    TextView tvDepartureStation,tvDestinationStation;
    @BindView(R.id.tvNextStation)
    TextView tvNextStation;
    @BindView(R.id.tvLateTimeWorded)
    TextView tvLateTimeWorded;
    @BindView(R.id.tvDepartureTime)
    TextView tvDepartureTime;
    @BindView(R.id.tvLateTime)
    TextView tvLateTime;
    @BindView(R.id.tvDestinationTime)
    TextView tvDestinationTime;
    @BindView(R.id.rvStations) RecyclerView mRecyclerView;

    @Inject StationPresenter mPresenter;

    public static String trainId = "";

    private static final String TAG = StationFragment.class.getSimpleName();

    public static StationFragment newInstance()
    {
        return new StationFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {

                if(nextStationdelay!= null && nextStationName != null)
                {
                    tvNextStation.setText(nextStationName);
                    tvLateTimeWorded.setText(nextStationdelay);
                    tvDepartureTime.setText(traindepartureTime);
                    tvDestinationTime.setText(traindestinationTime);
                    tvLateTime.setText(delayofTrain);
                }
            }
        }
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent)
    {
        fragmentComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View mFragment = inflater.inflate(R.layout.fragment_station, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle = getArguments();

        if(bundle != null){
            if(bundle.containsKey(KEY_TRAINID)){
                trainId = bundle.getString(KEY_TRAINID);
            }
        }

        mPresenter.getStationData( Realm.getDefaultInstance(),trainId);

        tvDepartureStation = mFragment.findViewById(R.id.tvDepartureStation);
        tvDestinationStation = mFragment.findViewById(R.id.tvDestinationStation);


        tvDepartureStation.setText(SharedPrefsUtils.getStringPreference(getContext(),KEY_FROM));

        tvDestinationStation.setText(SharedPrefsUtils.getStringPreference(getContext(),KEY_TO));

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        return mFragment;
    }


    @Override
    public void setStationData(ArrayList<StationPOJO> stationData) {
        RecyclerView.Adapter mAdapter = new StationAdapter(stationData);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class StationAdapter extends RecyclerView.Adapter<StationFragment.StationAdapter.ViewHolder>
    {
        private ArrayList<StationPOJO> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView mTvStation;
            public TextView mTvDepartureTime, mTvArrivalTime;
            public ImageView mBtnReminder;
            public ViewHolder(View v)
            {
                super(v);
                mTvStation = v.findViewById(R.id.tvStation);
                mTvDepartureTime = v.findViewById(R.id.tvDepartureTime);
                mTvArrivalTime = v.findViewById(R.id.tvArrivalTime);
                mBtnReminder = v.findViewById(R.id.btnReminder);
            }
        }

        public StationAdapter(ArrayList<StationPOJO> myDataset)
        {
            mDataset = myDataset;
        }

        @Override
        public StationFragment.StationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_station, parent, false);
            StationFragment.StationAdapter.ViewHolder vh = new StationFragment.StationAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(StationFragment.StationAdapter.ViewHolder holder, int position)
        {

            Locale current = getResources().getConfiguration().locale;
            String lan = current.getLanguage();
            final StationPOJO stationPOJO = mDataset.get(position);
            if (lan.equals("ar")) {
                holder.mTvStation.setText(stationPOJO.getNamear());
            } else {
                holder.mTvStation.setText(stationPOJO.getNameen());
            }

            holder.mTvDepartureTime.setText(stationPOJO.getTs());
        }

        @Override
        public int getItemCount()
        {
            return mDataset.size();
        }
    }



}
