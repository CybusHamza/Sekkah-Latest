package in.radioactivegames.sekkah.ui.main.report;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 1/2/2018.
 * www.radioactivegames.in
 */

public class ReportContract
{
    public interface View extends BaseMvpView
    {
        void setStationsData(List<String> data);
        void setTrainLocation(LatLng location);
    }

    public interface Presenter extends BaseMvpPresenter<ReportContract.View>
    {
        void getStationsData();
        void trainLocationReport(String stationId,String ts);
    }
}
