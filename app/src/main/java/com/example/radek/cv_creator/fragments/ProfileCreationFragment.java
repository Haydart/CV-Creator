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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
    TextInputLayout dateOfBirth;
    TextInputLayout emailAddress;
    TextInputLayout phoneNumber;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String NAME_PATTERN = "^[A-Z][a-zA-Z]*( )*$";
    private static final String DOB_PATTERN = "^\\d{4}[-/\\s]?((((0[13578])|(1[02]))[-/\\s]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[-/\\s]?(([0-2][0-9])|(30)))|(02[-/\\s]?[0-2][0-9]))$";
    private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private Pattern namePattern = Pattern.compile(NAME_PATTERN);
    private Pattern dobPattern = Pattern.compile(DOB_PATTERN);
    private Matcher matcher;

    static final int PHOTO_REQUEST_CODE = 1;

    boolean photoAdded = false;

    public ProfileCreationFragment() {
        // Required empty public constructor
    }

    public Profile getNewProfile() {
        return newProfile;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newProfile = new Profile();
        dateOfBirthTIL = (EditText) getView().findViewById(R.id.textDialog3);

        firstName = (TextInputLayout)getView().findViewById(R.id.til);
        lastName = (TextInputLayout)getView().findViewById(R.id.til2);
        dateOfBirth = (TextInputLayout)getView().findViewById(R.id.til3);
        emailAddress = (TextInputLayout)getView().findViewById(R.id.til4);
        phoneNumber = (TextInputLayout)getView().findViewById(R.id.til5);

        dateOfBirthTIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"DOB clicked!!!",Toast.LENGTH_SHORT).show();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.setTargetFragment(ProfileCreationFragment.this, 1);
                newFragment.show(activity.getSupportFragmentManager(), "DatePicker");
            }
        });

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
                photoBitmap = (Bitmap)bundle.get("data");
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

        if(!namePattern.matcher(firstName.getEditText().getText().toString()).matches()){
            errorCount++;
            firstName.setErrorEnabled(true);
            firstName.setError("Invalid first name");
        }

        if(!namePattern.matcher(lastName.getEditText().getText().toString()).matches()){
            errorCount++;
            lastName.setErrorEnabled(true);
            lastName.setError("Invalid last name");
        }

        if(!dobPattern.matcher(dateOfBirth.getEditText().getText().toString()).matches()){
            errorCount++;
            dateOfBirth.setErrorEnabled(true);
            dateOfBirth.setError("Invalid date of birth");
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

    public void setProfileTraits(){
        if(photoAdded)
            newProfile.setPhoto(photoBitmap);
        newProfile.setName(firstName.getEditText().getText().toString() + " " + lastName.getEditText().getText().toString());
        newProfile.setDOB(dateOfBirth.getEditText().getText().toString());
        newProfile.setPhoneNumber(phoneNumber.getEditText().getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFinishEditDatePickerDialog(String dateText) {
        dateOfBirthTIL.setText(dateText);
    }

    public interface OnProfileCreateFragmentClickListener
    {
        void onCreateProfileFragmentInteraction(View view);
    }
}