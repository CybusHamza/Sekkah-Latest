package in.app.sekkah.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseActivity;
import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.model.User;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.ActivityComponent;
import in.app.sekkah.fcm.RegistrationIntentService;
import in.app.sekkah.ui.loginregister.LoginRegisterActivity;
import in.app.sekkah.ui.main.contact.ContactFragment;
import in.app.sekkah.ui.main.home.HomeFragment;
import in.app.sekkah.ui.main.report.ReportFragment;
import in.app.sekkah.ui.main.track.map.MapFragment;

import static in.app.sekkah.ui.main.report.ReportFragment.latLngTrain;
import static in.app.sekkah.utility.Constants.FCM_TOEKN_ID;

public class MainActivity extends BaseActivity implements MainContract.View,MapFragment.OnFragmentInteractionListener
{
    @BindView(R.id.toolbarMain) Toolbar mToolbar;

    @BindView(R.id.navView) NavigationView mNavigationView;
    @BindView(R.id.drawer) DrawerLayout mDrawer;

    private ImageView mIvProfile;
    private TextView mTvName;
    private View mNavHeader;

    @Inject MainPresenter mainPresenter;

    private static int mNavItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));

        mNavHeader = mNavigationView.getHeaderView(0);
        mIvProfile = mNavHeader.findViewById(R.id.ivProfile);
        mTvName = mNavHeader.findViewById(R.id.tvName);

        mainPresenter.onAttach(this);

        mainPresenter.getUser();

        setSupportActionBar(mToolbar);

        loadNavHeader();
        setUpNavigationView();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameMain, HomeFragment.newInstance(), "HomeFragment")
                .addToBackStack("HomeFragment")
                .commit();

        Intent intent = new Intent(MainActivity.this,RegistrationIntentService.class);
        startService(intent);

    }

    @Override
    public void onInject(ActivityComponent activityComponent)
    {
        activityComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    private void loadNavHeader()
    {


        mTvName.setText("Sekkah");

        Glide.with(this).load(R.drawable.generic_profile_picture)
                .thumbnail(0.5f)
                .apply(RequestOptions.circleCropTransform())
                .into(mIvProfile);

        //View viewPromotion = getLayoutInflater().inflate(R.layout.nav_main_item_promotion_code, mNavigationView);
        mNavigationView.getMenu().getItem(2).setActionView(R.layout.nav_main_item_promotion_code);
        View viewPromotion = mNavigationView.getMenu().getItem(2).getActionView();
        TextView tvPromotionCode = viewPromotion.findViewById(R.id.tvPromotionCode);
        TextView btnShare = viewPromotion.findViewById(R.id.btnShare);
        tvPromotionCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharePromotionCodeTwitter();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharePromotionCodeTwitter();

            }
        });

    }

    private void setUpNavigationView()
    {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_main_points:
                        mNavItemIndex = 0;
                        break;
                    case R.id.nav_main_settings:
                        mNavItemIndex = 1;
                        break;
                    case R.id.nav_main_promotion_code:
                        mNavItemIndex = 2;
                        break;
                    case R.id.nav_main_reminder:
                        mNavItemIndex = 3;
                        break;
                    case R.id.nav_main_contact_us:
                        mNavItemIndex = 4;
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.frameMain, ContactFragment.newInstance(), "ContactFragment")
                                .addToBackStack("ContactFragment")
                                .commit();
                        break;
                    case R.id.nav_main_logout:
                        mNavItemIndex = 5;
                        startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                        finish();
                        break;
                    default:
                        mNavItemIndex = 0;
                }

                mDrawer.closeDrawers();
                /*if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);
                menuItem.setChecked(true);*/

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void sharePromotionCodeTwitter()
    {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("My Sekkah Promotion Code! #9348120947");
        builder.show();
    }

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
            latLngTrain = null;
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            finish();
        }
    }

    @Override
    public void openReportFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameMain, new ReportFragment(), "ReportFragment")
                .addToBackStack("ReportFragment")
                .commit();
    }

    @Override
    public void setUser(User user) {

        mTvName.setText(String.format("%s %s", user.firstName, user.lastName));

        String token = SharedPrefsUtils.getStringPreference(MainActivity.this,FCM_TOEKN_ID);

        mainPresenter.sendPushToserver(token, new JSONCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onFail(String errorMessage) {

            }
        });


    }
}

