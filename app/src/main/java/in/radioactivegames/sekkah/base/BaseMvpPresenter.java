package in.radioactivegames.sekkah.base;

/**
 * Created by AntiSaby on 11/6/2017.
 * www.radioactivegames.in
 */

public interface BaseMvpPresenter<V extends BaseMvpView>
{
    void onAttach(V mvpView);
    void onDetach();
}
