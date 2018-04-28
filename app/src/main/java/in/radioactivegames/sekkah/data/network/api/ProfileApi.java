package in.radioactivegames.sekkah.data.network.api;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import in.radioactivegames.sekkah.data.network.request.ForgotPasswordRequest;
import in.radioactivegames.sekkah.data.network.request.LoginRequest;
import in.radioactivegames.sekkah.data.network.request.RegisterRequest;
import in.radioactivegames.sekkah.data.network.response.RegisterResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface ProfileApi
{
    @Headers("Content-type: application/json")
    @POST("auth/register")
    Call<JsonElement> register(@Body RegisterRequest body);

    @Headers("Content-type: application/json")
    @POST("auth/login")
    Call<JsonElement> login(@Body LoginRequest body);

    @Headers("Content-type: application/json")
    @POST("auth/restore")
    Call<JsonElement> restore(@Body ForgotPasswordRequest body);

    @GET("profile")
    Call<JsonElement> profile( @Header("Authorization") String Authorization);


}
