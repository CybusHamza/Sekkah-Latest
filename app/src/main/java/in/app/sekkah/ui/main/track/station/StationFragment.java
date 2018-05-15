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

import static in.app.sekkah.utility.Constants.KEY_FROM;
import static in.app.sekkah.utility.Constants.KEY_TO;
import static in.app.sekkah.utility.Constants.KEY_TRAINID;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends BaseFragment implements StationContract.View
{
    private View mFragment;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView tvDepartureStation,tvDestinationStation;

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

        Bundle bundle = getArguments();
        String trainId = "";
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
        mAdapter = new StationFragment.StationAdapter(stationData);
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
