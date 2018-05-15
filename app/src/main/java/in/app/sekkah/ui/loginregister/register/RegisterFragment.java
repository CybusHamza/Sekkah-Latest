package in.app.sekkah.ui.loginregister.register;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import in.app.sekkah.R;
import in.app.sekkah.base.BaseFragment;
import in.app.sekkah.di.component.FragmentComponent;

public class RegisterFragment extends BaseFragment implements RegisterContract.View, Validator.ValidationListener
{
    private View mFragment;
    private String mFirstName, mLastName, mEmail, mPassword, mConfirmPassword, mUsername;
    private long mDateOfBirth;
    private Context mContext;
    private Validator mValidator;
    private OnFragmentInteractionListener mListener = null;

    @BindView(R.id.etUsername) @NotEmpty @Length(min = 3, max = 30, message = "Length must be 3-30") @Pattern(regex = "^[a-zA-Z0-9_]+$", message = "Alphabets, Numbers, _ allowed") EditText mEtUsername;
    @BindView(R.id.etFirstName) @NotEmpty @Length(max = 30, min = 3, message = "Length must be 3-30") @Pattern(regex = "^[a-zA-Z]+$", message = "Only Alphabets allowed") EditText mEtFirstName;
    @BindView(R.id.etLastName) @NotEmpty @Length(max = 30, min = 3, message = "Length must be 3-30") @Pattern(regex = "^[a-zA-Z]+$", message = "Only Alphabets allowed") EditText mEtLastName;
    @BindView(R.id.etEmail) @Email @NotEmpty EditText mEtEmail;
    @BindView(R.id.etPassword) @Password(min = 6, scheme = Password.Scheme.ANY, message = "Length 6 - 30, Alphabets, Numbers, Symbols Allowed") EditText mEtPassword;
    @BindView(R.id.etConfirmPassword) @ConfirmPassword EditText mEtConfirmPassword;
    @BindView(R.id.etDOB) EditText mEtDateOfBirth;

    @Inject RegisterPresenter mPresenter;

    private static final String TAG = RegisterFragment.class.getSimpleName();

    public static RegisterFragment newInstance()
    {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mPresenter.onAttach(this);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    @Override
    public void onInject(FragmentComponent fragmentComponent)
    {
        fragmentComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragment = inflater.inflate(R.layout.fragment_register, container, false);
        setUnbinder(ButterKnife.bind(this, mFragment));
        return mFragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onValidationSucceeded()
    {
        showProgressDialog("Please wait...");
        mUsername = mEtUsername.getText().toString().trim();
        mFirstName = mEtFirstName.getText().toString().trim();
        mLastName = mEtLastName.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        mConfirmPassword = mEtConfirmPassword.getText().toString().trim();
        mPresenter.register(mUsername, mFirstName, mLastName, mEmail, mDateOfBirth, mPassword, mConfirmPassword);
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

    //region EVENTS

    /**
     * Registration method called when the
     * registration button is clicked.
     */
    @OnClick(R.id.btnSubmit)
    public void register()
    {
        mValidator.validate();
    }

    @OnClick(R.id.etDOB)
    public void onClickDob()
    {
        chooseDob();
    }

    @OnFocusChange(R.id.etDOB)
    public void onFocusDob(View view, boolean isFocused)
    {
        if(!isFocused)
            return;
        chooseDob();
    }

    //endregion

    //region CONTRACT METHODS

    @Override
    public void registrationSuccessful()
    {
        hideProgressBar();
        Toast.makeText(getContext(), "Registration Successful! Check your Email to confirm the account.", Toast.LENGTH_LONG).show();
        mEtUsername.setText(""); mEtConfirmPassword.setText(""); mEtPassword.setText(""); mEtEmail.setText("");
        mEtDateOfBirth.setText(""); mEtFirstName.setText(""); mEtLastName.setText("");
        mDateOfBirth = 0;
        mFirstName = null; mLastName = null; mUsername = null; mEmail = null;
        mPassword = null; mConfirmPassword = null;
        mListener.changeViewPagerFragment(0);
    }

    @Override
    public void registrationFailed(String message)
    {
        hideProgressBar();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    //endregion

    private void chooseDob()
    {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        final Calendar mCurrentDate = Calendar.getInstance();
        int startDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int starthMonth = mCurrentDate.get(Calendar.MONTH) + 1;
        int startYear = mCurrentDate.get(Calendar.YEAR);
        final String currentDate = starthMonth + "/" + startDay + "/" + startYear;
        final String minimumDate = "01/01/2000";
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                long dateInMillis = 0;
                long currentDateInMillis = 0;
                long minimumDateInMillis = 0;
                String parsedDateString = null;
                Date parsedDate = null;
                try
                {
                    parsedDate = simpleDateFormat.parse(date);
                    parsedDateString = simpleDateFormat.format(parsedDate);
                    dateInMillis = parsedDate.getTime();
                    currentDateInMillis = simpleDateFormat.parse(currentDate).getTime();
                    minimumDateInMillis = simpleDateFormat.parse(minimumDate).getTime();
                }
                catch(ParseException e)
                {
                    Log.e(TAG, "Error Parsing Dates");
                }

                if(dateInMillis > minimumDateInMillis)
                    Toast.makeText(mContext, "Date should be less than or equal to 1 Jan 2000", Toast.LENGTH_SHORT).show();
                else if(dateInMillis > currentDateInMillis)
                    Toast.makeText(mContext, "Selected Date can't be greater than the current date.", Toast.LENGTH_SHORT).show();
                else
                {
                    mDateOfBirth = dateInMillis;
                    mEtDateOfBirth.setText(parsedDateString);
                }
            }
        }, 1995, 0, 1);
        datePickerDialog.setTitle("Choose Date Of Birth");
        datePickerDialog.show();
    }

    public interface OnFragmentInteractionListener
    {
        void changeViewPagerFragment(int index);
    }
}
