package in.radioactivegames.sekkah.ui.loginregister.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.radioactivegames.sekkah.R;
import in.radioactivegames.sekkah.base.BaseFragment;
import in.radioactivegames.sekkah.di.component.FragmentComponent;
import in.radioactivegames.sekkah.ui.loginregister.LoginRegisterActivity;
import in.radioactivegames.sekkah.ui.loginregister.forgotpassword.ForgotPasswordActivity;
import in.radioactivegames.sekkah.ui.main.MainActivity;

public class LoginFragment extends BaseFragment implements LoginContract.View, Validator.ValidationListener
{
    private View mFragmentView;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Context mContext;
    private Validator mValidator;

    @BindView(R.id.etUsername) @NotEmpty EditText mEtUsername;
    @BindView(R.id.etPassword) @NotEmpty EditText mEtPassword;

    @Inject LoginPresenter mPresenter;
    @Inject TwitterAuthClient mTwitterAuthClient;

    private static final int GOOGLE_SIGN_IN = 0;
    private static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mPresenter.onAttach(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                loginSuccessful();
            }

            @Override
            public void onCancel()
            {
                loginFailed("Login Cancelled!");
            }

            @Override
            public void onError(FacebookException exception)
            {
                loginFailed("Login Error!");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

    }

    @Override
    public void onInject(FragmentComponent fragmentComponent)
    {
        fragmentComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        setUnbinder(ButterKnife.bind(this, mFragmentView));
        return mFragmentView;
    }

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

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try
        {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "Email: " + account.getEmail());
            loginSuccessful();
            //updateUI(account);
        }
        catch (ApiException e)
        {
            loginFailed("Google Sign-In Failed!");
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    //---------------Events---------------------------

    @OnClick(R.id.btnFacebook)
    public void attemptFBLogin()
    {
        List<String> perms = Arrays.asList("email", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(this, perms);
    }

    @OnClick(R.id.btnTwitter)
    public void attemptTwitterLogin()
    {
        mTwitterAuthClient.authorize(getActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>()
        {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult)
            {
                loginSuccessful();
            }

            @Override
            public void failure(TwitterException e)
            {
                loginFailed("Twitter Login Failed!");
            }
        });
    }

    @OnClick(R.id.btnGoogle)
    public void attemptGoogleLogin()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.btnLogin)
    public void attemptLogin()
    {
        mValidator.validate();
    }

    @OnClick(R.id.tvForgotPassword)
    public void onClickForgot()
    {
        launchFogotPassScreen();
    }


    private void launchFogotPassScreen(){

        startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
    }
    @Override
    public void onValidationSucceeded()
    {
        showProgressDialog("Please Wait...");
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        mPresenter.login(username, password);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(mContext);
            if (view instanceof EditText)
                ((EditText) view).setError(message);
            else
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccessful()
    {
        hideProgressBar();
        Intent toHome = new Intent(getActivity(), MainActivity.class);
        startActivity(toHome);
        getActivity().finish();
    }

    @Override
    public void loginFailed(String message)
    {
        hideProgressBar();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
