package in.radioactivegames.sekkah.ui.main;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;

/**
 * Created by AntiSaby on 11/27/2017.
 * www.radioactivegames.in
 */

public class MainContract
{
    public interface View extends BaseMvpView
    {

    }

    public interface Presenter extends BaseMvpPresenter<MainContract.View>
    {

    }
}
