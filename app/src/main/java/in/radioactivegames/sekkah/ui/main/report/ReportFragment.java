package in.radioactivegames.sekkah.ui.main.report;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.di.component.FragmentComponent;


public class ReportFragment extends BaseFragment implements ReportContract.View
{
    private View mFragment;
    @Inject ReportPresenter mPresenter;

    @BindView(R.id.spnStation)
    Spinner spnStation;
    @BindView(R.id.tvHour)
    TextView tvHour;
    @BindView(R.id.tvMins)
    TextView tvMins;
    List<String> data;
    String ts;
    public  ReportFragment () {

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
        mFragment = inflater.inflate(R.layout.fragment_report, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);

        return mFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getStationsData(getActivity());
    }

    @Override
    public void setStationsData(List<String> data)
    {
        this.data=data;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, data);
        spnStation.setAdapter(arrayAdapter);
    }

    @Override
    public void setTrainLocation(LatLng location) {


    }


    @OnClick(R.id.llTime)
    public void chooseTime()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                tvHour.setText(String.format("%02d", selectedHour));
                tvMins.setText(String.format("%02d", selectedMinute));
            }
        }, hour, minute, false);//Yes 24 hour time
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    @OnClick(R.id.btnReport)
    public void getReport()
    {
        String stationId = data.get(spnStation.getSelectedItemPosition());
        ts = tvHour.getText().toString()+":"+tvMins.getText().toString();
        mPresenter.trainLocationReport("5a6475ec457d1b10b4bb38fa",ts);
    }

}
