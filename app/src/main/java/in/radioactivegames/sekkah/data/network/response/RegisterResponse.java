package in.radioactivegames.sekkah.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntiSaby on 1/8/2018.
 * www.radioactivegames.in
 */

public class RegisterResponse
{
    @Expose
    @SerializedName("success")
    public boolean success;

    @Expose
    @SerializedName("error")
    public String error;
}
