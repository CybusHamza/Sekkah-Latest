package in.radioactivegames.sekkah.ui.loginregister;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BasePresenter;
import in.radioactivegames.sekkah.data.callbacks.SuccessFailCallback;

/**
 * Created by AntiSaby on 11/28/2017.
 * www.radioactivegames.in
 */

public class LoginRegisterPresenter extends BasePresenter<LoginRegisterContract.View> implements LoginRegisterContract.Presenter
{

    @Inject
    public LoginRegisterPresenter()
    {

    }
}
