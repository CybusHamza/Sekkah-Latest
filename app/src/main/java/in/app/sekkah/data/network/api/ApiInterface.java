package in.app.sekkah.data.network.api;

import com.google.gson.JsonElement;

import in.app.sekkah.data.network.request.ContactUs;
import in.app.sekkah.data.network.request.ForgotPasswordRequest;
import in.app.sekkah.data.network.request.LoginRequest;
import in.app.sekkah.data.network.request.LoginSocailRequest;
import in.app.sekkah.data.network.request.Notifsetting;
import in.app.sekkah.data.network.request.PushToken;
import in.app.sekkah.data.network.request.RegisterRequest;
import in.app.sekkah.data.network.request.Reminder;
import in.app.sekkah.data.network.request.UserRoute;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface ApiInterface
{
    @Headers("Content-type: application/json")
    @POST("auth/register")
    Call<JsonElement> register(@Body RegisterRequest body);

    @Headers("Content-type: application/json")
    @POST("auth/loginphone")
    Call<JsonElement> login(@Body LoginRequest body);

    @Headers("Content-type: application/json")
    @POST("auth/loginsocial")
    Call<JsonElement> loginSocialMedia(@Body LoginSocailRequest body);

    @Headers("Content-type: application/json")
    @POST("auth/restore")
    Call<JsonElement> restore(@Body ForgotPasswordRequest body);

    @GET("profile")
    Call<JsonElement> profile( @Header("Authorization") String Authorization);

    @Headers("Content-type: application/json")
    @POST("contactus")
    Call<JsonElement> contactus( @Header("Authorization") String Authorization,@Body ContactUs body);

    @Headers("Content-type: application/json")
    @POST("pntoken")
    Call<JsonElement> pntoken( @Header("Authorization") String Authorization,@Body PushToken body);

    @Headers("Content-type: application/json")
    @POST("settings")
    Call<JsonElement> getsettings( @Header("Authorization") String Authorization);

    @Headers("Content-type: application/json")
    @POST("settings")
    Call<JsonElement> setsettings( @Header("Authorization") String Authorization,@Body Notifsetting body);

    @Headers("Content-type: application/json")
    @POST("reminders")
    Call<JsonElement> getReminders( @Header("Authorization") String Authorization);

    @Headers("Content-type: application/json")
    @POST("reminders")
    Call<JsonElement> setReminders( @Header("Authorization") String Authorization ,@Body Reminder reminder);

    @Headers("Content-type: application/json")
    @POST("userroute")
    Call<JsonElement> userRoute( @Header("Authorization") String Authorization ,@Body UserRoute UserRoute);


}
