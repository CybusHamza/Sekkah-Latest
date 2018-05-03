package in.radioactivegames.sekkah.ui.main;

import in.radioactivegames.sekkah.base.BaseMvpPresenter;
import in.radioactivegames.sekkah.base.BaseMvpView;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.model.User;

/**
 * Created by AntiSaby on 11/27/2017.
 * www.radioactivegames.in
 */

public class MainContract
{
    public interface View extends BaseMvpView
    {
             void setUser(User user);
    }

    public interface Presenter extends BaseMvpPresenter<MainContract.View>
    {
        void getUser();
        public void sendPushToserver( String pushToken ,JSONCallback jsonCallback);

    }
}
