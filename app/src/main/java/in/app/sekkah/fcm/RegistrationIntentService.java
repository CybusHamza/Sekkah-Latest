package in.app.sekkah.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import in.app.sekkah.R;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;

import static in.app.sekkah.utility.Constants.FCM_TOEKN_ID;

/**
 * Created by hamza on 01/05/2018.
 */

public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // Make a call to Instance API
            String instanceID = FirebaseInstanceId.getInstance().getToken();
            String senderId = getResources().getString(R.string.gcm_defaultSenderId);

            SharedPrefsUtils.setStringPreference(this,FCM_TOEKN_ID,instanceID);
            Log.d(TAG,instanceID);


        } catch (Exception e) {
            e.getCause();

        }


    }
}