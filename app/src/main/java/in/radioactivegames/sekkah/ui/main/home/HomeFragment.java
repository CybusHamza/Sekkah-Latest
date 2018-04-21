package in.radioactivegames.sekkah.ui.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.main.trainlist.TrainsFragment;

public class HomeFragment extends BaseFragment implements HomeContract.View
{
    private View mFragment;
    @Inject HomePresenter mPresenter;

    @BindView(R.id.spnDeparture) Spinner spnDeparture;
    @BindView(R.id.spnDestination) Spinner spnDestination;

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent)
    {
        fragmentComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragment = inflater.inflate(R.layout.fragment_home, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getStationsData();
    }

    @Override
    public void setStationsData(List<String> data)
    {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        spnDeparture.setAdapter(arrayAdapter);
        spnDestination.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btnTrains)
    public void viewTrains()
    {
        getFragmentManager().beginTransaction()
                .add(R.id.frameMain, TrainsFragment.newInstance(), "TrainsFragment")
                .addToBackStack("TrainsFragment")
                .commit();
    }
}
