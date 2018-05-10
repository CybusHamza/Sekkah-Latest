package in.radioactivegames.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hamza on 05/05/2018.
 */

public class UserRoute {

    @Expose
    @SerializedName("source")
    public String source;
    @Expose
    @SerializedName("destination")
    public String destination;
    @Expose
    @SerializedName("currentLocation")
    public String currentLocation;
    @Expose
    @SerializedName("selectedLocation")
    public String selectedLocation;
    @Expose
    @SerializedName("trainId")
    public String trainId;

}
