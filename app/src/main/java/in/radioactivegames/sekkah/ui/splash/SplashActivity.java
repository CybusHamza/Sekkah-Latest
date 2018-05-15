package in.radioactivegames.sekkah.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.data.network.WebSocketHelper;
import in.radioactivegames.sekkah.data.sharedpref.SharedPrefsUtils;
import in.radioactivegames.sekkah.ui.loginregister.LoginRegisterActivity;
import in.radioactivegames.sekkah.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(SharedPrefsUtils.getBooleanPreference(SplashActivity.this,"isUserLoggedin",false)){
                    Intent toLoginRegister = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(toLoginRegister);
                    finish();

                }else {

                    Intent toLoginRegister = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                    startActivity(toLoginRegister);
                    SharedPrefsUtils.setBooleanPreference(SplashActivity.this,"isUserLoggedin",true);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
