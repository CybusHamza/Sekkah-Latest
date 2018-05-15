package in.app.sekkah.ui.main.trainlist;

import android.content.Context;

import java.util.List;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;
import in.app.sekkah.data.model.TrainPOJO;
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
        public void getTrainData(String fromStation,String toStaion,Realm realm,Context context);

    }
}
