package in.radioactivegames.sekkah.data.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.radioactivegames.sekkah.base.BaseApiHelper;
import in.radioactivegames.sekkah.data.callbacks.JSONCallback;
import in.radioactivegames.sekkah.data.network.api.ProfileApi;
import in.radioactivegames.sekkah.data.network.request.LoginRequest;
import in.radioactivegames.sekkah.data.network.request.RegisterRequest;
import in.radioactivegames.sekkah.data.network.response.RegisterResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public class ApiHelper implements BaseApiHelper
{
    private static final String TAG = ApiHelper.class.getSimpleName();

    private ProfileApi mProfileApi;

    @Inject
    public ApiHelper(ProfileApi profileApi)
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
}
