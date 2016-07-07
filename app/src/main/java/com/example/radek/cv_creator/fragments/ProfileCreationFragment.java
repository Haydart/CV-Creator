package com.example.radek.cv_creator.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.R;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Radek on 2016-06-30.
 */
public class ProfileCreationFragment extends Fragment implements SelectDateFragment.EditDatePickerListener{

    FragmentActivity activity;
    ImageView photoImageView;
    Bitmap photoBitmap;

    EditText dateOfBirthTIL;
    Profile newProfile;

    TextInputLayout firstName;
    TextInputLayout lastName;
    RadioGroup genderRadioGroup;
    RadioButton checkedGender;
    TextInputLayout emailAddress;
    TextInputLayout phoneNumber;
    TextInputLayout addressLine1;
    TextInputLayout addressLine2;
    TextInputLayout addressLine3;
    TextInputLayout dateOfBirth;

    ImageButton calendarButton;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String DOB_PATTERN = "^\\d{4}[-/\\s]?((((0[13578])|(1[02]))[-/\\s]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[-/\\s]?(([0-2][0-9])|(30)))|(02[-/\\s]?[0-2][0-9]))$";
    private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private Pattern dobPattern = Pattern.compile(DOB_PATTERN);

    static final int PHOTO_REQUEST_CODE = 1;

    boolean photoAdded = false;

    public ProfileCreationFragment() {
        // Required empty public constructor
    }

    public Profile getNewProfile() {
        return new Profile( firstName.getEditText().getText().toString().trim() + " " + lastName.getEditText().getText().toString().trim(),
                            checkedGender.getText().toString(),
                            emailAddress.getEditText().getText().toString(),
                            phoneNumber.getEditText().getText().toString(),
                            addressLine1.getEditText().getText().toString(),
                            addressLine2.getEditText().getText().toString(),
                            addressLine3.getEditText().getText().toString(),
                            photoBitmap,
                            dateOfBirth.getEditText().getText().toString()
                            );
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newProfile = new Profile();
        calendarButton = (ImageButton)getView().findViewById(R.id.DOBDialogButton) ;

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.setTargetFragment(ProfileCreationFragment.this, 1);
                newFragment.show(activity.getSupportFragmentManager(), "DatePicker");
            }
        });

        firstName = (TextInputLayout)getView().findViewById(R.id.til);
        lastName = (TextInputLayout)getView().findViewById(R.id.til2);

        genderRadioGroup = (RadioGroup)getView().findViewById(R.id.genderRadioGroup);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkedGender = (RadioButton) getView().findViewById(genderRadioGroup.getCheckedRadioButtonId());
            }
        });

        checkedGender = (RadioButton)getView().findViewById(R.id.genderRadioButton1);
        checkedGender.setChecked(true);

        dateOfBirth = (TextInputLayout)getView().findViewById(R.id.til3);
        emailAddress = (TextInputLayout)getView().findViewById(R.id.til4);
        phoneNumber = (TextInputLayout)getView().findViewById(R.id.til5);
        addressLine1 = (TextInputLayout)getView().findViewById(R.id.til6);
        addressLine2 = (TextInputLayout)getView().findViewById(R.id.til7);
        addressLine3 = (TextInputLayout)getView().findViewById(R.id.til8);

        photoImageView = (ImageView)getView().findViewById(R.id.profilePhotoImageView);
        photoImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(photoIntent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(photoIntent, PHOTO_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            activity = (FragmentActivity)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " musi implementowac OnWyborOpcjiListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_creation, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = data.getExtras();
                photoBitmap = Bitmap.createScaledBitmap((Bitmap)bundle.get("data"),300,400,false);
                photoImageView.setImageBitmap(photoBitmap);
                photoAdded = true;
            }
        }
    }

    public boolean isProfileDataValid(){
        return invalidDataErrorsCount()==0;
    }

    private int invalidDataErrorsCount(){
        return setInvalidDataErrors();
    }

    private int setInvalidDataErrors(){
        int errorCount = 0;

        if(firstName.getEditText().getText().toString().trim().equals("")){
            errorCount++;
            firstName.setErrorEnabled(true);
            firstName.setError("Cannot be left empty");
        }

        if(lastName.getEditText().getText().toString().trim().equals("")){
            errorCount++;
            lastName.setErrorEnabled(true);
            lastName.setError("Cannot be left empty");
        }

        if(!dobPattern.matcher(dateOfBirth.getEditText().getText().toString()).matches()){
            errorCount++;
            dateOfBirth.setErrorEnabled(true);
            dateOfBirth.setError("Date not in yyyy-mm-dd format");
        }

        if(!emailPattern.matcher(emailAddress.getEditText().getText().toString()).matches()){
            errorCount++;
            emailAddress.setErrorEnabled(true);
            emailAddress.setError("Invalid e-mail address");
        }

        return errorCount;
    }

    public void setErrorsDisabled(){
        firstName.setErrorEnabled(false);
        lastName.setErrorEnabled(false);
        dateOfBirth.setErrorEnabled(false);
        emailAddress.setErrorEnabled(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFinishEditDatePickerDialog(String dateText) {
        dateOfBirth.getEditText().setText(dateText);
    }

    public interface OnProfileCreateFragmentClickListener
    {
        void onCreateProfileFragmentInteraction(View view);
    }
}