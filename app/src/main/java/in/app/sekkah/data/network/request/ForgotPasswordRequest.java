package in.app.sekkah.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hamza on 28/04/2018.
 */

public class ForgotPasswordRequest {

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
