package in.app.sekkah.ui.main.report;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.data.Realm.RealmDB;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.FragmentComponent;
import in.app.sekkah.ui.main.MainActivity;
import io.realm.Realm;

import static in.app.sekkah.ui.main.track.map.MapFragment.delayofTrain;
import static in.app.sekkah.ui.main.track.map.MapFragment.nextStationName;
import static in.app.sekkah.ui.main.track.map.MapFragment.nextStationdelay;
import static in.app.sekkah.ui.main.track.map.MapFragment.traindepartureTime;
import static in.app.sekkah.ui.main.track.map.MapFragment.traindestinationTime;
import static in.app.sekkah.utility.Constants.KEY_FROM;
import static in.app.sekkah.utility.Constants.KEY_TO;
import static in.app.sekkah.utility.Constants.KEY_TRAIN;


public class ReportFragment extends BaseFragment implements ReportContract.View {
    @Inject
    ReportPresenter mPresenter;

    @BindView(R.id.spnStation)
    Spinner spnStation;
    @BindView(R.id.tvHour)
    TextView tvHour;
    @BindView(R.id.tvMins)
    TextView tvMins;
    @BindView(R.id.tvStation)
    TextView tvStation;
    @BindView(R.id.tvArrivalTime)
    TextView tvArrivalTime;
    @BindView(R.id.tvTrain)
    TextView tvTrain;

    @BindView(R.id.tvDestinationTime)
    TextView tvDestinationTime;
    @BindView(R.id.tvDepartureTime)
    TextView tvDepartureTime;
    @BindView(R.id.tvDepartureStation)
    TextView tvDepartureStation;
    @BindView(R.id.tvDestinationStation)
    TextView tvDestinationStation;

    List<String> data;
    String ts;

    public static LatLng latLngTrain;

    public ReportFragment() {

    }

    @Override
    public void onInject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mFragment = inflater.inflate(R.layout.fragment_report, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        mPresenter.onAttach(this);

       tvTrain.setText(SharedPrefsUtils.getStringPreference(getContext(),KEY_TRAIN));

        if(nextStationdelay!= null && nextStationName != null)
        {
            tvDepartureTime.setText(traindepartureTime);
            tvDestinationTime.setText(traindestinationTime);

           String  from = SharedPrefsUtils.getStringPreference(getContext(), KEY_FROM);
           String to = SharedPrefsUtils.getStringPreference(getContext(), KEY_TO);

            tvDepartureStation.setText(from);
            tvDestinationStation.setText(to);

        }

        return mFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getStationsData(getActivity());
    }

    @Override
    public void setStationsData(final List<String> data) {
        this.data = data;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, data){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.WHITE);


                return tv;
            }
        };

        spnStation.setAdapter(arrayAdapter);

        spnStation.setOnItemSelectedListener(new MyOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                super.onItemSelected(parent, view, pos, id);

                tvStation.setText(data.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
                super.onNothingSelected(parent);
            }
        });
    }

    @Override
    public void setTrainLocation(LatLng location) {

        latLngTrain = location;



    }

    @OnClick(R.id.llTime)
    public void chooseTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tvHour.setText(String.format("%02d", selectedHour));
                tvMins.setText(String.format("%02d", selectedMinute));
                tvArrivalTime.setText(String.format("%s:%s", tvHour.getText().toString(), tvMins.getText().toString()));
            }
        }, hour, minute, false);//Yes 24 hour time
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    @OnClick(R.id.btnReport)
    public void getReport() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        String resport = " You will send a report to all users about the train "+tvTrain.getText().toString()+
                " had stopped in "+tvStation.getText().toString()+" station at "+tvArrivalTime.getText().toString()+", Are you sure";

        alertDialogBuilder.setMessage(resport);
                alertDialogBuilder.setPositiveButton("yes , i am sure ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                String stationName = data.get(spnStation.getSelectedItemPosition());

                                Locale current = getActivity().getResources().getConfiguration().locale;
                                String stationId;
                                if (current.getLanguage().equals("ar")) {
                                    stationId = RealmDB.getinstance().getStationbyNameAr(stationName, Realm.getDefaultInstance());
                                } else {
                                    stationId = RealmDB.getinstance().getStationbyName(stationName, Realm.getDefaultInstance());
                                }

                                ts = tvHour.getText().toString() + ":" + tvMins.getText().toString()+":00";

                                mPresenter.trainLocationReport(getActivity(), stationId, time(ts));

                                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }


                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public String time(String ts){

        DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy "+ts+" Z");
        Date date1 = new Date();

        String timeStamp = dateFormat.format(date1);


        return timeStamp;

    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
