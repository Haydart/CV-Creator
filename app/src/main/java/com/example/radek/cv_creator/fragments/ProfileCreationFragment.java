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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.R;

/**
 * Created by Radek on 2016-06-30.
 */
public class ProfileCreationFragment extends Fragment implements SelectDateFragment.EditDatePickerListener{

    FragmentActivity activity;
    ImageView photoImageView;
    EditText dateOfBirthTIL;
    Profile newProfile; // the profile being created

    static final int PHOTO_REQUEST_CODE = 1;
    static final int DATE_PICKER_FEEDBACK_REQUEST_CODE = 1;


    public ProfileCreationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newProfile = new Profile();
        dateOfBirthTIL = (EditText) getView().findViewById(R.id.textDialog3);
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
                photoImageView.setImageBitmap((Bitmap)bundle.get("data"));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFinishEditDatePickerDialog(String inputText) {
        Toast.makeText(getContext(), "DATE PICKER FEEDBACK RECEIVED " + inputText, Toast.LENGTH_SHORT).show();
    }

    public interface OnProfileCreateFragmentClickListener
    {
        void onCreateProfileFragmentInteraction(View view);
    }
}