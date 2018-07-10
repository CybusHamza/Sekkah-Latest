package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntiSaby on 1/8/2018.
 * www.radioactivegames.in
 */

public class LoginSocailRequest
{
    @Expose
    @SerializedName("socialId")
    public String socialId;

    @Expose
    @SerializedName("socialType")
    public String socialType;

    @Expose
    @SerializedName("firstName")
    public String firstName;

    @Expose
    @SerializedName("lastName")
    public String lastName;

    @Expose
    @SerializedName("password")
    public String password;

    @Expose
    @SerializedName("email")
    public String email;

    @Expose
    @SerializedName("attestation")
    public String attestation;
}
