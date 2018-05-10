package in.radioactivegames.sekkah.data.network;

import android.util.Log;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseApiHelper;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.network.api.ApiInterface;
import in.radioactivegames.sekkah.data.network.request.ContactUs;
import in.radioactivegames.sekkah.data.network.request.ForgotPasswordRequest;
import in.radioactivegames.sekkah.data.network.request.LoginRequest;
import in.radioactivegames.sekkah.data.network.request.Notifsetting;
import in.radioactivegames.sekkah.data.network.request.PushToken;
import in.radioactivegames.sekkah.data.network.request.RegisterRequest;
import in.radioactivegames.sekkah.data.network.request.Reminder;
import in.radioactivegames.sekkah.data.network.request.UserRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public class ApiHelper implements BaseApiHelper
{
    private static final String TAG = ApiHelper.class.getSimpleName();

    private ApiInterface mProfileApi;

    @Inject
    public ApiHelper(ApiInterface profileApi)
    {
        mProfileApi = profileApi;
    }

    @Override
    public void registerUser(String username, String firstName, String lastName, String email, long dateOfBirth, String password, String confirmPassword, final JSONCallback callback)
    {
        RegisterRequest request = new RegisterRequest();
        request.userName = username;
        request.firstName = firstName;
        request.lastName = lastName;
        request.birthDate = dateOfBirth;
        request.email = email;
        request.password = password;
        request.passwordConfirmation = confirmPassword;

        Call<JsonElement> call = mProfileApi.register(request);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    root = new JSONObject(response.body().getAsJsonObject().toString());
                    success = root.getBoolean("success");
                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void loginUser(String username, String password, final JSONCallback callback)
    {
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;

        Call<JsonElement> call = mProfileApi.login(request);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    root = new JSONObject(response.body().getAsJsonObject().toString());
                    success = root.getBoolean("success");
                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void forgotPassword(String email, String password, String passwordConfirmation, final JSONCallback callback) {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.email = email;
        request.password = password;
        request.passwordConfirmation = passwordConfirmation;

        Call<JsonElement> call = mProfileApi.restore(request);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    root = new JSONObject(response.body().getAsJsonObject().toString());
                    success = root.getBoolean("success");
                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });

    }

    @Override
    public void getProfile(String auth, final JSONCallback callbac) {

        Call<JsonElement> call = mProfileApi.profile(auth);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callbac.onSuccess(root);
                else
                    callbac.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callbac.onFail("Error contacting the service! Please try again later.");
            }
        });

    }

    @Override
    public void contactUs(String auth, String subject, String type, String message, final JSONCallback callback) {
        
        ContactUs request = new ContactUs();
        request.subject = subject;
        request.type = type;
        request.message = message;


        Call<JsonElement> call = mProfileApi.contactus(auth,request);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });

    }

    @Override
    public void pntoken(String auth, String pnToken, final JSONCallback callback) {

        PushToken request = new PushToken();
        request.pnToken = pnToken;



        Call<JsonElement> call = mProfileApi.pntoken(auth,request);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });

    }

    @Override
    public void settings(String auth, final JSONCallback callback) {

        Call<JsonElement> call = mProfileApi.getsettings(auth);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void getReminders(String auth, final JSONCallback callback) {

        Call<JsonElement> call = mProfileApi.getReminders(auth);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void setsettings(String auth, boolean getNotifications, final JSONCallback callback) {

        Notifsetting notifsetting = new Notifsetting();
        notifsetting.getNotifications = getNotifications;

        Call<JsonElement> call = mProfileApi.setsettings(auth,notifsetting);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void setreminders(String auth, Reminder Reminder, final JSONCallback callback) {

        Call<JsonElement> call = mProfileApi.setReminders(auth,Reminder);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }

    @Override
    public void userroute(String auth, String source, String destination, String currentLocation, String selectedLocation, String trainId, final JSONCallback callback) {

        UserRoute userRoute = new UserRoute();
        userRoute.source = source;
        userRoute.destination = destination;
        userRoute.currentLocation = currentLocation;
        userRoute.selectedLocation = selectedLocation;
        userRoute.trainId = trainId;

        Call<JsonElement> call = mProfileApi.userRoute(auth,userRoute);
        call.enqueue(new Callback<JsonElement>()
        {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response)
            {
                Log.d(TAG, response.body() + "");
                Log.d(TAG, response + "");

                boolean success = false;
                JSONObject root = null;
                String errorMessage = null;
                try
                {
                    if(response.code() != 401){
                        root = new JSONObject(response.body().getAsJsonObject().toString());
                        success = root.getBoolean("success");

                    }else {
                        return;
                    }

                    if(root.has("error"))
                        errorMessage = root.getString("error");
                }
                catch(JSONException ex)
                {
                    Log.e(TAG, ex.getMessage());
                }

                if(success)
                    callback.onSuccess(root);
                else
                    callback.onFail(errorMessage);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t)
            {
                Log.e(TAG, t.toString());
                callback.onFail("Error contacting the service! Please try again later.");
            }
        });
    }
}
