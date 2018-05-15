package in.radioactivegames.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hamza on 29/04/2018.
 */

public class ContactUs {

    @Expose
    @SerializedName("subject")
    public String subject;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("message")
    public String message;
}
