package in.app.sekkah.ui.loginregister.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseActivity;
import in.app.sekkah.data.sharedpref.SharedPrefsUtils;
import in.app.sekkah.di.component.ActivityComponent;
import in.app.sekkah.ui.loginregister.forgotpassword.ForgotPasswordActivity;
import in.app.sekkah.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements LoginContract.View, Validator.ValidationListener {
    private static final String SAFETER_NET_API_KEY = "AIzaSyBYJtYNTUhDNUcz6pncA_I6XSzW6F0EJus";
    private GoogleSignInClient mGoogleSignInClient;
    private Context mContext;
    private Validator mValidator;
    public static final int REQUEST_CODE = 999;
    @BindView(R.id.etUsername)
    @NotEmpty
    EditText mEtUsername;
    public final int REQUEST_PERMISSIONS_REQUEST = 123;
    @Inject
    LoginPresenter mPresenter;
    @Inject
    TwitterAuthClient mTwitterAuthClient;
    // method create.
    @BindView(R.id.login_button)
    @NotEmpty
    LoginButton loginButton;
    @BindView(R.id.TwitterLogin)
    @NotEmpty
    TwitterLoginButton TwitterLogin;
    CallbackManager callbackManager;
    private final Random mRandom = new SecureRandom();
    private static String decodedPayload;
    private static final int GOOGLE_SIGN_IN = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();
    String accountKitId = "";
    String phoneNumberString ="";
    String fName, lName, mEmail;

    public static LoginActivity newInstance() {
        return new LoginActivity();
    }

    String fbID, phoneNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        setUnbinder(ButterKnife.bind(this));
        mContext = LoginActivity.this;
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mPresenter.onAttach(this);


        FacebookSdk.sdkInitialize(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        getSafteynet();

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);

    }

    @Override
    public void onInject(ActivityComponent activityComponent)
    {
        activityComponent.inject(LoginActivity.this);
    }


    public void getSafteynet() {

        SafetyNet.getClient(mContext).attest(getRequestNonce(), SAFETER_NET_API_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.AttestationResponse response) {

                                //Toast.makeText(mContext, response.getJwsResult(), Toast.LENGTH_SHORT).show();


                                decodeJws(response.getJwsResult());
                            }

                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while communicating with the service.
                        if (e instanceof ApiException) {
                            // An error with the Google Play services API contains some
                            // additional details.
                            ApiException apiException = (ApiException) e;
                            // You can retrieve the status code using the
                            // apiException.getStatusCode() method.
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }

    private byte[] getRequestNonce() {

        String nonceData = "Sikkah_app: " + System.currentTimeMillis();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];
        mRandom.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(nonceData.getBytes());
        } catch (IOException e) {
            return null;
        }

        return byteStream.toByteArray();
    }


    public String decodeJws(String jwsResult) {
        if (jwsResult == null) {
            return null;
        }
        final String[] jwtParts = jwsResult.split("\\.");
        if (jwtParts.length == 3) {
            decodedPayload = new String(Base64.decode(jwtParts[1], Base64.DEFAULT));
            return decodedPayload;
        } else {
            return null;
        }
    }


/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }

    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        // Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            assert acct != null;
            String personName = acct.getDisplayName();
            String[] names = personName.split(" ");
            fName = names[0];
            lName = names[1];
            mEmail = acct.getEmail();
            String googleId = acct.getId();

            mPresenter.loginSocialMedia(googleId,"1",fName,lName,"",mEmail,decodedPayload);


        }
    }

    //---------------Events---------------------------

    @OnClick(R.id.btnFacebook)
    public void attemptFBLogin() {
        loginButton.performClick();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                JSONObject obj = response.getJSONObject();
                                try {

                                    String name = obj.optString("name", "");

                                    String[] names = name.split(" ");
                                    fName = names[0];
                                    lName = names[1];
                                    mEmail = obj.optString("email", "");
                                    String picture = obj.optString("picture", "");
                                    JSONObject image = new JSONObject(picture);
                                    String data = image.optString("data", "");
                                    String fb_id = obj.optString("id", "");

                                    LoginManager.getInstance().logOut();

                                    mPresenter.loginSocialMedia(fb_id,"0",fName,lName,"",mEmail,decodedPayload);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();

            }
        });

       /* List<String> perms = Arrays.asList("email", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(this, perms);*/
    }

    @OnClick(R.id.btnTwitter)
    public void attemptTwitterLogin() {

        mTwitterAuthClient.authorize(LoginActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>()
        {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult)
            {
                loginSuccessful();
            }

            @Override
            public void failure(TwitterException e) {
                loginFailed("Twitter Login Failed!");
            }


        });

    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                REQUEST_PERMISSIONS_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSMSVerification();
            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startSMSVerification() {
        final Intent intent = new Intent(mContext, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...

        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    fbID = loginResult.getAccessToken().getAccountId();

                } else {
                    fbID = loginResult.getAuthorizationCode().substring(0, 10);
                }

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get Account Kit ID
                        accountKitId = account.getId();

                        // Get phone number
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        if (phoneNumber != null) {
                             phoneNumberString = phoneNumber.toString();
                        }

                        // Get email
                        String email = account.getEmail();

                     mValidator.validate();
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                    }
                });


            }

        } else if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            if (callbackManager != null) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }

            TwitterLogin.onActivityResult(requestCode, resultCode, data);


        }
    }

    @OnClick(R.id.btnGoogle)
    public void attemptGoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin() {
        requestPermissions();
    }

    @OnClick(R.id.tvForgotPassword)
    public void onClickForgot() {
        launchFogotPassScreen();
    }


    private void launchFogotPassScreen() {

        startActivity(new Intent(mContext, ForgotPasswordActivity.class));
    }

    @Override
    public void onValidationSucceeded() {
        showProgressDialog("Please Wait...");

        mPresenter.login(accountKitId,phoneNumberString,decodedPayload);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(mContext);
            if (view instanceof EditText)
                ((EditText) view).setError(message);
            else
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccessful() {
        hideProgressBar();
        SharedPrefsUtils.setBooleanPreference(mContext, "isUserLoggedin", true);
        Intent toHome = new Intent(mContext, MainActivity.class);
        startActivity(toHome);
        finish();
    }

    @Override
    public void loginFailed(String message) {
        hideProgressBar();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
