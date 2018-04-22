package in.radioactivegames.sekkah;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import in.radioactivegames.sekkah.data.network.WebSocketHelper;

public class LocationService extends Service {

    String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJVU0VSMTUxNTQzMzkxMjkyMiIsImd1aWQiOiI1YTUzYWZiOTBmODQyZjAwMTRiOTczMTYiLCJpYXQiOjE1MTU0OTc3NjR9.E1MRwZS3oDHTm0rm5XVD6Sq3Z9y_S1xSWotCOudm10s";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
