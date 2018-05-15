package in.app.sekkah.base;

import java.lang.ref.WeakReference;

/**
 * Created by AntiSaby on 11/6/2017.
 * www.radioactivegames.in
 */

public class BasePresenter<V extends BaseMvpView> implements BaseMvpPresenter<V>
{
    private WeakReference<V> mvpView;

    @Override
    public void onAttach(V mvpView)
    {
        this.mvpView = new WeakReference<V>(mvpView);
    }

    @Override
    public void onDetach()
    {
        this.mvpView = null;
    }

    protected V getMvpView()
    {
        return this.mvpView.get();
    }
}
