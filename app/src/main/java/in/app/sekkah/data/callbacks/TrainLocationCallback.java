package in.app.sekkah.data.callbacks;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AntiSaby on 1/17/2018.
 * www.radioactivegames.in
 */

public interface TrainLocationCallback
{
    void onLocationReceive(LatLng location);
}
