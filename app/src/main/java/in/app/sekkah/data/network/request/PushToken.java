package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hamza on 01/05/2018.
 */

public class PushToken {

    @Expose
    @SerializedName("pnToken")
    public String pnToken;
}
