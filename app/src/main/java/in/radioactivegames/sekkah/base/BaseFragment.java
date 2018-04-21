package in.radioactivegames.sekkah.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;
import in.radioactivegames.sekkah.MyApplication;
import in.radioactivegames.sekkah.di.component.DaggerFragmentComponent;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.di.module.FragmentModule;

/**
 * Created by AntiSaby on 11/6/2017.
 * www.radioactivegames.in
 */

public abstract class BaseFragment extends Fragment
{
    private Unbinder unbinder;
    private BaseActivity baseActivity;
    private FragmentComponent fragmentComponent;
    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule())
                .applicationComponent(((MyApplication) baseActivity.getApplication()).getApplicationComponent())
                .build();
        onInject(fragmentComponent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (unbinder != null)
        {
            unbinder.unbind();
        }
    }

    public abstract void onInject(FragmentComponent fragmentComponent);

    public void setUnbinder(Unbinder unbinder)
    {
        this.unbinder = unbinder;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof BaseActivity)
        {
            BaseActivity baseActivity = (BaseActivity) context;
            this.baseActivity = baseActivity;
        }
    }

    protected void showProgressDialog(String message)
    {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void hideProgressBar()
    {
        progressDialog.hide();
    }
}