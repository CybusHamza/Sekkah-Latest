package in.app.sekkah.ui.loginregister.forgotpassword;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseActivity;
import in.app.sekkah.di.component.ActivityComponent;
import in.app.sekkah.ui.loginregister.LoginRegisterActivity;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordContract.View,Validator.ValidationListener {

    @BindView(R.id.etEmail) @Email
    @NotEmpty
    EditText mEtEmail;
    @BindView(R.id.etPassword) @Password(min = 6, scheme = Password.Scheme.ANY, message = "Length 6 - 30, Alphabets, Numbers, Symbols Allowed") EditText mEtPassword;
    @BindView(R.id.etConfirmPassword) @ConfirmPassword
    EditText mEtConfirmPassword;
    private Validator mValidator;
    @Inject
    ForgotPasswordPresenter forgotPasswordPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        forgotPasswordPresenter.onAttach(this);
        setUnbinder(ButterKnife.bind(this));

    }

    @Override
    public void onInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.btnSubmit)
    public void register()
    {
        mValidator.validate();
    }

    @OnClick(R.id.btnLogin)
    public void login()
    {
      startActivity(new Intent(this, LoginRegisterActivity.class));
    }

    @Override
    public void onValidationSucceeded() {

        showProgressDialog("Please wait...");
        String mEmail = mEtEmail.getText().toString().trim();
        String mPassword = mEtPassword.getText().toString().trim();
        String mConfirmPassword = mEtConfirmPassword.getText().toString().trim();

        forgotPasswordPresenter.changePassword(mEmail, mPassword, mConfirmPassword);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(ForgotPasswordActivity.this);
            if (view instanceof EditText)
                ((EditText) view).setError(message);
            else
                Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void changePasswordSuccessful() {
        hideProgressBar();
        Toast.makeText(ForgotPasswordActivity.this, "Password change Succesfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void changePasswordFailed(String message) {
        hideProgressBar();
        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
