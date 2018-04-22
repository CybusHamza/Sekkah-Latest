package in.radioactivegames.sekkah.ui.main.trainlist;

import org.json.JSONObject;

import java.util.List;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import in.radioactivegames.sekkah.data.model.Train;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import io.realm.Realm;

/**
 * Created by AntiSaby on 12/19/2017.
 * www.radioactivegames.in
 */

public class TrainsContract
{
    public interface View extends BaseMvpView
    {
        public void setTrainPojoData(List<TrainPOJO> data);
    }

    public interface Presenter extends BaseMvpPresenter<TrainsContract.View> {
        public void getTrainData(Realm realm);
    }
}
