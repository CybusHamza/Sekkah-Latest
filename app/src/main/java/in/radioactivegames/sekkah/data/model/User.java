package in.radioactivegames.sekkah.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntiSaby on 12/15/2017.
 * www.radioactivegames.in
 */

public class User
{
    @SerializedName("user_id")
    @Expose
    public String mUserId;

    @SerializedName("access_token")
    public String mAccessToken;
}
