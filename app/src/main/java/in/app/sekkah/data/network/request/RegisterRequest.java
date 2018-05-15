package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntiSaby on 1/8/2018.
 * www.radioactivegames.in
 */

public class RegisterRequest
{
    @Expose
    @SerializedName("userName")
    public String userName;

    @Expose
    @SerializedName("firstName")
    public String firstName;

    @Expose
    @SerializedName("lastName")
    public String lastName;

    @Expose
    @SerializedName("birthDate")
    public long birthDate;

    @Expose
    @SerializedName("password")
    public String password;

    @Expose
    @SerializedName("passwordConfirmation")
    public String passwordConfirmation;

    @Expose
    @SerializedName("email")
    public String email;
}
