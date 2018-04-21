package in.radioactivegames.sekkah.ui.main.track.station;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.adapter.ViewPagerAdapter;
import in.radioactivegames.sekkah.ui.main.track.TrackContract;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.ui.main.track.TrackPresenter;
import in.radioactivegames.sekkah.ui.main.track.map.MapFragment;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends BaseFragment implements StationContract.View
{
    private View mFragment;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.rvStations) RecyclerView mRecyclerView;

    @Inject StationPresenter mPresenter;

    private static final String TAG = StationFragment.class.getSimpleName();

    public static StationFragment newInstance()
    {
        return new StationFragment();
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
        mFragment = inflater.inflate(R.layout.fragment_station, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPresenter.getStationData();
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        return mFragment;
    }

    @Override
    public void setStationData(List<Station> data)
    {
        mAdapter = new StationFragment.StationAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class StationAdapter extends RecyclerView.Adapter<StationFragment.StationAdapter.ViewHolder>
    {
        private List<Station> mDataset;

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

        public StationAdapter(List<Station> myDataset)
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
            holder.mTvStation.setText(mDataset.get(position).name);
            holder.mTvArrivalTime.setText(mDataset.get(position).arrivalTime);
            holder.mTvDepartureTime.setText(mDataset.get(position).departureTime);
        }

        @Override
        public int getItemCount()
        {
            return mDataset.size();
        }
    }
}
