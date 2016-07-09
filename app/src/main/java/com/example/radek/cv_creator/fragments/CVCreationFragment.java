package com.example.radek.cv_creator.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.MainActivity;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.R;
import com.example.radek.cv_creator.adapters.CVSkillAdapter;

import java.util.ArrayList;

public class CVCreationFragment extends Fragment {
    OnCVCreationListener onCVCreationListener;
    Activity activity;
    Spinner profileChoiceSpinner;
    ArrayList<Profile> profilesResource;
    static Bundle args;
    MenuItem saveCV;
    int activeProfileIndex = 0;
    boolean userWantsToOverwrite = false;
    private static final int ADD_OBJECTIVE = 0;
    private static final int ADD_INTEREST_FIELDS = 1;
    private static final int ADD_PERSONAL_QUALITIES = 2;

    LayoutInflater inflater;
    ArrayList<String> skills;

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
    LinearLayout interestsLinearLayout;

    ArrayList<RelativeLayout> relativeLayouts;

    public CVCreationFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_cvcreation, container, false);
        this.inflater = inflater;
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getReferences();
        setLayoutsOnClickListeners();
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
            Snackbar.make(getActivity().getCurrentFocus(),"CV saved",Snackbar.LENGTH_SHORT).show();
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

    private void displayDataOverwritingWarning(final int whichOption){
        AlertDialog warningDialog = new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("Such component already exists. If you decide to proceed, the data will be overwritten. If you wish to edit the component, cimply click on it in main view. Do you wish to proceed?")

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userWantsToOverwrite = true;

                        switch(whichOption){
                            case ADD_OBJECTIVE:
                                objectivesLinearLayout.removeAllViews();
                                getView().findViewById(R.id.cvObjectivesTableRow).setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting objectives module",Snackbar.LENGTH_SHORT).show();
                                addNewObjectivesItem();
                                break;
                            case ADD_INTEREST_FIELDS:
                                interestsLinearLayout.removeAllViews();
                                getView().findViewById(R.id.cvObjectivesTableRow).setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting interests module",Snackbar.LENGTH_SHORT).show();
                                addNewInterestFieldsItem();
                                break;
                            case ADD_PERSONAL_QUALITIES:
                                personalTraitsLinearLayout.removeAllViews();
                                getView().findViewById(R.id.cvObjectivesTableRow).setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting personal traits module",Snackbar.LENGTH_SHORT).show();
                                addNewPersonalQualitiesItem();
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .create();
                warningDialog.show();
    }

    public void addNewObjectivesItem(){
        if(objectivesLinearLayout.getChildCount() > 0){
            displayDataOverwritingWarning(ADD_OBJECTIVE);
        }else if(objectivesLinearLayout.getChildCount() == 0 || userWantsToOverwrite){
            userWantsToOverwrite = false;
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(getContext());
            tv.setId(View.generateViewId());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
            tv.setLayoutParams(lparams);
            tv.setText("asdyguadscuiygdasciudgascuigasdciudygasciygasdciuygasdcuigasdcuiygasdciugasdcygasdciygasdcuiygadscuigasdcigasdciuygasdciuygasdcuiygasdciuygasdciuygasdciuygasdcuiygadscuiygasdciuygadscygaudscgadscuiygadscygadscygadsygadscyguasdygadsgadscygadsciygadscuiygadsgdscouygasdcouygadscouygadscoyugadscoyugadscoyugadsocyugdasc");

            objectivesLinearLayout.addView(tv);
            getView().findViewById(R.id.cvObjectivesTableRow).setVisibility(View.VISIBLE);
        }
    }

    public void addNewSkillsItem(){
        final EditText edittext = new EditText(getContext());
        AlertDialog myDialogBox = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Add new skill you possess")
                .setView(edittext)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        skills.add(edittext.getText().toString());
                        displayRefreshedSkillsList();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        myDialogBox.show();
    }

    private void editSkillsItem(){
        final CVSkillAdapter skillAdapter = new CVSkillAdapter(getActivity(),skills);

        final AlertDialog editSkillsDialog = new AlertDialog.Builder(getActivity())
                .setAdapter(skillAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
                        Toast.makeText(getContext(), "clicked " + i, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skills = skillAdapter.getSkillsResource();
                        displayRefreshedSkillsList();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Delete Selected", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int deletedCount = skillAdapter.getCheckedSkillsIndexes().size();
                        ArrayList<String> skillsToBeRemoved = new ArrayList<String>();

                        for(int i=0;i<deletedCount;i++){
                            skillsToBeRemoved.add(skills.get(skillAdapter.getCheckedSkillsIndexes().get(i)));
                        }
                        skills.removeAll(skillsToBeRemoved);

                        skillAdapter.setSkillsResource(skills);
                        skillAdapter.notifyDataSetChanged();
                        displayRefreshedSkillsList();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setTitle("Edit your Skills")
                .setIcon(R.drawable.ic_person_black_24dp)
                .setCancelable(false)
                .create();

        editSkillsDialog.show();

    }

    private void displayRefreshedSkillsList(){
        if(skills.size() > 0){
            getView().findViewById(R.id.cvSkillsTableRow).setVisibility(View.VISIBLE);
            skillsLinearLayout.removeAllViews();
            for(String skillItem : skills){
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView tv = new TextView(getContext());
                tv.setId(View.generateViewId());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
                tv.setLayoutParams(lparams);
                tv.setText(skillItem);
                skillsLinearLayout.addView(tv);
            }
        }else{
            getView().findViewById(R.id.cvSkillsTableRow).setVisibility(View.GONE);
        }
    }

    public void addPhoto(){
        Toast.makeText(getContext(), "add photo", Toast.LENGTH_SHORT).show();
    }

    public void addNewExperienceItem(){
        Toast.makeText(getContext(), "new experience item", Toast.LENGTH_SHORT).show();
    }

    public void addNewInterestFieldsItem(){
        Toast.makeText(getContext(), "new interest fields item", Toast.LENGTH_SHORT).show();
    }

    public void addNewPersonalQualitiesItem(){
        Toast.makeText(getContext(), "new personal qualities", Toast.LENGTH_SHORT).show();
    }

    public interface OnCVCreationListener{
        void onCVCreated();
    }

    public void getReferences(){
        skills = new ArrayList<>();
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
        interestsLinearLayout = (LinearLayout) getView().findViewById(R.id.cvInterestsLinearLayout);
    }

    private void setLayoutsOnClickListeners(){
        objectivesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked objLayout", Toast.LENGTH_SHORT).show();
            }
        });
        skillsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked skillLay", Toast.LENGTH_SHORT).show();
                editSkillsItem();
            }
        });
        personalTraitsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked traitsLay", Toast.LENGTH_SHORT).show();
            }
        });
        experienceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked expLay", Toast.LENGTH_SHORT).show();
            }
        });
        interestsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked interestsLay", Toast.LENGTH_SHORT).show();
            }
        });
    }
}