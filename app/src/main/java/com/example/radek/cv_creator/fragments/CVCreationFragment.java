package com.example.radek.cv_creator.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.ProfileChoiceSpinnerAdapter;
import com.example.radek.cv_creator.R;

import java.util.ArrayList;

public class CVCreationFragment extends Fragment {
    OnCVCreationListener onCVCreationListener;
    Activity activity;
    Spinner profileChoiceSpinner;
    ArrayList<Profile> profilesResource;
    static Bundle args;
    MenuItem saveCV;
    int activeProfileIndex = 0;

    TextView nameTextView;
    TextView occupationTextView;
    TextView emailTextView;
    TextView phoneNUmberTextView;
    TextView dobTextView;
    TextView address1TextView;
    TextView address2TextView;
    TextView address3TextView;

    LinearLayout objectivesLinearLayout;
    LinearLayout skillsLinearLayout;
    LinearLayout personalTraitsLinearLayout;
    LinearLayout experienceLinearLayout;
    LinearLayout employersLinearLayout;
    LinearLayout interestsLinearLayout;

    ArrayList<RelativeLayout> relativeLayouts;

    public CVCreationFragment() {
        // Required empty public constructor
    }

    public void getReferences(){
        nameTextView = (TextView) getView().findViewById(R.id.cvNameTextView);
        occupationTextView = (TextView)getView().findViewById(R.id.cvOccupationTextView);
        emailTextView = (TextView)getView().findViewById(R.id.cvEmailTextView);
        phoneNUmberTextView = (TextView) getView().findViewById(R.id.cvPhoneNumberTextView);
        dobTextView = (TextView) getView().findViewById(R.id.cvDOBTextView);
        address1TextView = (TextView) getView().findViewById(R.id.cvAddress1TextView);
        address2TextView = (TextView) getView().findViewById(R.id.cvAddress2TextView);
        address3TextView = (TextView) getView().findViewById(R.id.cvAddress3TextView);

        objectivesLinearLayout = (LinearLayout) getView().findViewById(R.id.cvObjectivesLinearLayout);
        skillsLinearLayout = (LinearLayout)getView().findViewById(R.id.cvSkillsLinearLayout);
        personalTraitsLinearLayout = (LinearLayout) getView().findViewById(R.id.cvPersonalTraitsLinearLayout);
        experienceLinearLayout = (LinearLayout) getView().findViewById(R.id.cvExperienceLinearLayout);
        employersLinearLayout = (LinearLayout) getView().findViewById(R.id.cvEmployersLinearLayout);
        interestsLinearLayout = (LinearLayout) getView().findViewById(R.id.cvInterestsLinearLayout);
    }

    public static void setArgumentsBundle(Bundle bundle){
        args = bundle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        args = getArguments();
        profilesResource = (ArrayList<Profile>) args.get("profilesResource");

        try {
            onCVCreationListener = (OnCVCreationListener) context;
            activity = (Activity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCVCreationListener");
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // set listeners here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_cvcreation, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getReferences();
        setProfileInfo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cv_creation_menu, menu);

        saveCV = (MenuItem)menu.findItem(R.id.cv_creation_save);
        saveCV.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_save_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.cv_creation_save){
            Snackbar.make(getActivity().getCurrentFocus(),"SAVED CV",Snackbar.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileChoiceSpinner = null;
        profilesResource = null;
        args = null;
        activity = null;
    }

    public void setActiveProfileIndex(int index){
        this.activeProfileIndex = index;
    }

    public void setProfileInfo(){
        if(profilesResource!=null & profilesResource.size()>0){
            nameTextView.setText(profilesResource.get(activeProfileIndex).getName());
            emailTextView.setText(profilesResource.get(activeProfileIndex).getEmail());
            dobTextView.setText(profilesResource.get(activeProfileIndex).getDOB());
            phoneNUmberTextView.setText(profilesResource.get(activeProfileIndex).getPhoneNumber());
            address1TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine1());
            address2TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine2());
            address3TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine3());
        }else{
            Toast.makeText(getContext(), "no profiles available", Toast.LENGTH_SHORT).show();
        }

    }

    public void addNewObjectivesItem(){
        TextView textView = new TextView(getContext());
        textView.setText("BLABLAB");
        textView.setId(View.generateViewId());
        textView.setTextSize(10);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "view click", Toast.LENGTH_SHORT).show();
            }
        });
        objectivesLinearLayout.addView(textView);
    }

    public interface OnCVCreationListener{
        void onCVCreated();
    }
}