package in.radioactivegames.sekkah.ui.main.track.station;

import java.util.List;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.ui.main.track.map.MapContract;

/**
 * Created by AntiSaby on 12/26/2017.
 * www.radioactivegames.in
 */

public class StationContract
{
    public interface View extends BaseMvpView
    {
        public void setStationData(List<Station> data);
    }

    public interface Presenter extends BaseMvpPresenter<StationContract.View>
    {
        public void getStationData();
    }
}
