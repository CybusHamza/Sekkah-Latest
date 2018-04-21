package in.radioactivegames.sekkah.ui.main.home;

import java.util.List;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 11/29/2017.
 * www.radioactivegames.in
 */

public class HomeContract
{
    public interface View extends BaseMvpView
    {
        void setStationsData(List<String> data);
    }

    public interface Presenter extends BaseMvpPresenter<HomeContract.View>
    {
        void getStationsData();
    }
}
