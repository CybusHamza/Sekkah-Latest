package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hamza on 03/05/2018.
 */

public class Reminder {

    @Expose
    @SerializedName("trainId")
    String trainId;
    @Expose
    @SerializedName("stationId")
    String stationId;
    @Expose
    @SerializedName("sendAt")
    String sendAt;


}

