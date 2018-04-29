package in.radioactivegames.sekkah.ui.main.trainlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.track.TrackFragment;
import in.radioactivegames.sekkah.utility.Constants;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static in.radioactivegames.sekkah.utility.Constants.KEY_FROM;
import static in.radioactivegames.sekkah.utility.Constants.KEY_TO;

public class TrainsFragment extends BaseFragment implements TrainsContract.View {
    private View mFragment;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.rvTrns)
    RecyclerView mRecyclerView;

    @Inject
    TrainsPresenter mPresenter;

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
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //mPresenter.getTrainData();

        Bundle bundle = getArguments();
        getTrainData(bundle);

        return mFragment;
    }

    public void getTrainData(Bundle bundle) {

        String from, to;

        from = bundle.getString(KEY_FROM);
        to = bundle.getString(KEY_TO);

        Realm realm = Realm.getDefaultInstance();
        mPresenter.getTrainData(from, to, realm, getActivity());

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


    private void startTracking(String trainId) {

        Bundle bundle = new Bundle();

        bundle.putString(Constants.KEY_TRAINID, trainId);


        TrackFragment trackFragment = TrackFragment.newInstance();
        trackFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .add(R.id.frameMain, trackFragment, "TrackFragment")
                .addToBackStack("TrackFragment")
                .commit();
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
