package in.app.sekkah.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.app.sekkah.utility.Utils;

/**
 * Created by hamza on 10/06/2018.
 */

public class LocalChangeReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.doRestart(context);
    }
}
