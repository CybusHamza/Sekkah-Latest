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
import in.radioactivegames.sekkah.data.network.request.RegisterRequest;
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
}
