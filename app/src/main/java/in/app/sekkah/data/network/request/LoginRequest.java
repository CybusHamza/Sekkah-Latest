package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntiSaby on 1/8/2018.
 * www.radioactivegames.in
 */

public class LoginRequest
{
    @Expose
    @SerializedName("fbId")
    public String fbId;

    @Expose
    @SerializedName("phoneNum")
    public String phoneNum;

    @Expose
    @SerializedName("attestation")
    public String attestation;
}
