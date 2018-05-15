package in.app.sekkah.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;
import in.app.sekkah.MyApplication;
import in.app.sekkah.di.component.ActivityComponent;
import in.app.sekkah.di.component.DaggerActivityComponent;
import in.app.sekkah.di.module.ActivityModule;

/**
 * Created by AntiSaby on 11/5/2017.
 * www.radioactivegames.in
 */

public abstract class BaseActivity extends AppCompatActivity
{
    protected ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .build();

        onInject(activityComponent);
    }

    public abstract void onInject(ActivityComponent activityComponent);

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(unbinder != null)
            unbinder.unbind();
    }

    protected void setUnbinder(Unbinder unbinder)
    {
        this.unbinder = unbinder;
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

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1)
        {
            getSupportFragmentManager().popBackStack();
        }else {
            finish();
        }
    }
}
