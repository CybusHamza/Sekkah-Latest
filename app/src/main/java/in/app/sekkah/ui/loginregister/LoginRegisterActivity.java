package in.app.sekkah.ui.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseActivity;
import in.app.sekkah.di.component.ActivityComponent;
import in.app.sekkah.ui.adapter.ViewPagerAdapter;
import in.app.sekkah.ui.loginregister.login.LoginFragment;
import in.app.sekkah.ui.loginregister.register.RegisterFragment;

public class LoginRegisterActivity extends BaseActivity implements LoginRegisterContract.View, RegisterFragment.OnFragmentInteractionListener
{
    private ViewPagerAdapter mViewPagerAdapter;

    @BindView(R.id.vpViewPager) ViewPager mViewPager;
    @BindView(R.id.tlTabLayout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);
        setUnbinder(ButterKnife.bind(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(LoginFragment.newInstance(), "Login");
        mViewPagerAdapter.addFragment(RegisterFragment.newInstance(), "Register");
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onInject(ActivityComponent activityComponent)
    {
        activityComponent.inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = mViewPagerAdapter.getItem(0);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void changeViewPagerFragment(int index)
    {
        mViewPager.setCurrentItem(index, true);
    }
}
