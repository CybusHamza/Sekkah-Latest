package in.app.sekkah.ui.main.contact;

import in.app.sekkah.base.BaseMvpPresenter;
import in.app.sekkah.base.BaseMvpView;
import in.app.sekkah.data.callbacks.JSONCallback;

/**
 * Created by AntiSaby on 1/3/2018.
 * www.radioactivegames.in
 */

public class ContactContract
{


    public interface View extends BaseMvpView
    {

    }

    public interface Presenter extends BaseMvpPresenter<ContactContract.View>
    {
        public void contactUs( String subject, String type, String message, JSONCallback jsonCallback);
         }
}
