package com.example.radek.cv_creator.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.ProfileChoiceSpinnerAdapter;
import com.example.radek.cv_creator.R;

import java.util.ArrayList;

public class CVCreationFragment extends Fragment {
    OnCVCreationListener onCVCreationListener;
    FragmentActivity activity;
    Spinner profileChoiceSpinner;
    ArrayList<Profile> profilesResource;
    ProfileChoiceSpinnerAdapter profileChoiceSpinnerAdapter;
    static Bundle args;

    public CVCreationFragment() {
        // Required empty public constructor
    }

    public static void setArgumentsBundle(Bundle bundle){
        args = bundle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profilesResource = new ArrayList<>();

        try {
            onCVCreationListener = (OnCVCreationListener) context;
            activity = (FragmentActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCVCreationListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profileChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectedWelcomeChoice = profileChoiceSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_cvcreation, container, false);
        profileChoiceSpinner = (Spinner) fragmentView.findViewById(R.id.profileChoiceSpinner);
        profilesResource = (ArrayList<Profile>) args.get("profilesResource");
        Log.d("AAAAAAAAAAAAAAAAAAAAAAA", profilesResource.toString());

        profileChoiceSpinnerAdapter = new ProfileChoiceSpinnerAdapter(getActivity(), profilesResource);
        profileChoiceSpinnerAdapter.setDropDownViewResource(R.layout.profile_choice_spinner);
        profileChoiceSpinner.setAdapter(profileChoiceSpinnerAdapter);

        return fragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileChoiceSpinner = null;
        profilesResource = null;
        profileChoiceSpinnerAdapter = null;
        args = null;
        activity = null;
    }

    public interface OnCVCreationListener{
        void onCVCreated();
    }

}