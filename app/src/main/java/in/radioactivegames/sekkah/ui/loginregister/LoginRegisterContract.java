package in.radioactivegames.sekkah.ui.loginregister;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import in.radioactivegames.sekkah.data.callbacks.SuccessFailCallback;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class LoginRegisterContract
{
    public interface View extends BaseMvpView
    {

    }

    public interface Presenter extends BaseMvpPresenter<LoginRegisterContract.View>
    {

    }
}
