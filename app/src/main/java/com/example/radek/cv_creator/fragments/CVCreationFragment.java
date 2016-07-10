package com.example.radek.cv_creator.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.R;
import com.example.radek.cv_creator.adapters.CVSkillListEditDialogAdapter;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.ChromaUtil;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import java.util.ArrayList;

public class CVCreationFragment extends Fragment {
    OnCVCreationListener onCVCreationListener;
    Activity activity;
    Spinner profileChoiceSpinner;
    ArrayList<Profile> profilesResource;
    static Bundle args;
    LayoutInflater layoutInflater;
    int userPhotoBorderColorRed = 255;
    int userPhotoBorderColorGreen = 255;
    int userPhotoBorderColorBlue = 255;
    int userPhotoBorderWidth = 4;

    MenuItem saveCV;
    MenuItem editCvModule;
    MenuItem deleteCvModule;
    int activeProfileIndex = 0;
    boolean userWantsToOverwrite = false;
    boolean hasActiveProfileChanged = false;
    private static final int ADD_OBJECTIVE = 0;
    private static final int ADD_INTEREST_FIELDS = 1;
    private static final int ADD_PERSONAL_QUALITIES = 2;
    private static final int ADD_PHOTO = 3;

    LayoutInflater inflater;
    ArrayList<String> skills;
    String objectives;

    TextView nameTextView;
    TextView occupationTextView;
    TextView emailTextView;
    TextView phoneNUmberTextView;
    TextView dobTextView;
    TextView address1TextView;
    TextView address2TextView;
    TextView address3TextView;

    CircularImageView userPhoto;

    LinearLayout objectivesLinearLayout;
    LinearLayout skillsLinearLayout;
    LinearLayout personalTraitsLinearLayout;
    LinearLayout experienceLinearLayout;
    LinearLayout interestsLinearLayout;

    TableRow objectivesTableRow;
    TableRow skillsTableRow;
    TableRow personalTraitsTableRow;
    TableRow experienceTableRow;
    TableRow interestsTableRow;

    public enum FocusedType{
        NONE (""),
        PHOTO ("photo"),
        OBJECTIVES_ITEM ("objectives section"),
        SKILLS_ITEM ("skills section"),
        EXPERIENCE_ITEM ("experience section"),
        INTERESTS_ITEM ("interests section"),
        PERSONAL_TRAITS_ITEM ("personal traits section");


        String name;

        private FocusedType(String componentDisplayedName){
            name = componentDisplayedName;
        }

        public String getNsme(){
            return name;
        }
    }

    public enum PhotoType{
        CIRCULAR,
        RECTANGULAR;
    }

    FocusedType currentlyFocusedOnType = FocusedType.NONE;
    PhotoType userPhotoType = PhotoType.CIRCULAR;

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
        editCvModule = (MenuItem) menu.findItem(R.id.cv_creation_edit);
        editCvModule.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_mode_edit_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
        deleteCvModule = (MenuItem) menu.findItem(R.id.cv_creation_delete);
        deleteCvModule.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_delete_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.cv_creation_save){
            Snackbar.make(getActivity().getCurrentFocus(),"CV saved",Snackbar.LENGTH_SHORT).show();
        }else if(id==R.id.cv_creation_edit){
            editCurrentlyFocusedItem();
            currentlyFocusedOnType = FocusedType.NONE;
            setActionBarMenuVisibility(false);
        }else if(id==R.id.cv_creation_delete){
            promptUserBeforeDeletingItem();
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
        setProfileInfo();
    }

    public void setProfileInfo(){
        if(profilesResource!=null && profilesResource.size()>0){
            if(profilesResource.get(activeProfileIndex).getPhoto()!=null)
                userPhoto.setImageBitmap(profilesResource.get(activeProfileIndex).getPhoto());
            nameTextView.setText(profilesResource.get(activeProfileIndex).getName());
            emailTextView.setText(profilesResource.get(activeProfileIndex).getEmail());
            dobTextView.setText(profilesResource.get(activeProfileIndex).getDOB());
            phoneNUmberTextView.setText(profilesResource.get(activeProfileIndex).getPhoneNumber());
            address1TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine1());
            address2TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine2());
            address3TextView.setText(profilesResource.get(activeProfileIndex).getAddressLine3());
        }
    }

    private void setActionBarMenuVisibility(boolean value){
        editCvModule.setVisible(value);
        deleteCvModule.setVisible(value);
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
                                objectivesTableRow.setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting objectives section",Snackbar.LENGTH_SHORT).show();
                                addNewObjectivesItem();
                                break;
                            case ADD_INTEREST_FIELDS:
                                interestsLinearLayout.removeAllViews();
                                interestsTableRow.setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting interests section",Snackbar.LENGTH_SHORT).show();
                                addNewInterestFieldsItem();
                                break;
                            case ADD_PERSONAL_QUALITIES:
                                personalTraitsLinearLayout.removeAllViews();
                                personalTraitsTableRow.setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting personal traits section",Snackbar.LENGTH_SHORT).show();
                                addNewPersonalQualitiesItem();
                                break;
                            case ADD_PHOTO:
                                userPhoto.setVisibility(View.GONE);
                                Snackbar.make(getActivity().getCurrentFocus(),"Overwriting photo item",Snackbar.LENGTH_SHORT).show();
                                addPhoto();
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .create();
                warningDialog.show();
    }

    public void addNewObjectivesItem(){ //parameter used to not copy almost identical code when user wants to edit the section
        if(objectivesLinearLayout.getChildCount() > 0){
            displayDataOverwritingWarning(ADD_OBJECTIVE);
        }else if(objectivesLinearLayout.getChildCount() == 0 || userWantsToOverwrite) {
            userWantsToOverwrite = false;
            displayObjectivesSectionDialog("");
        }
    }

    private void displayObjectivesSectionDialog(String editedText){ // if we are editing, edited text is set to current objectives string
        final EditText edittext = new EditText(getContext());
        edittext.setText(editedText);
        AlertDialog myDialogBox = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Objectives section")
                .setMessage("Tell something about your future aspirations and plans.")
                .setView(edittext)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        objectives = edittext.getText().toString();
                        displayRefreshedObjectivesSection();
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

    private void editObjectivesItem(){
        displayObjectivesSectionDialog(objectives);
    }

    private void displayRefreshedObjectivesSection(){
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(getContext());
            tv.setId(View.generateViewId());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
            tv.setLayoutParams(lparams);
            tv.setText(objectives);

            objectivesLinearLayout.addView(tv);
            objectivesTableRow.setVisibility(View.VISIBLE);
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
        final CVSkillListEditDialogAdapter skillAdapter = new CVSkillListEditDialogAdapter(getActivity(),skills);

        final AlertDialog editSkillsDialog = new AlertDialog.Builder(getActivity())
                .setAdapter(skillAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
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
            skillsTableRow.setVisibility(View.VISIBLE);
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
            skillsTableRow.setVisibility(View.GONE);
        }
    }

    public void addPhoto(){
        if(profilesResource.get(activeProfileIndex).getPhoto()!=null){
            if(userPhoto.getVisibility() == View.VISIBLE){
                displayDataOverwritingWarning(ADD_PHOTO);
            }else if(userPhoto.getVisibility() == View.GONE || userWantsToOverwrite) {
                userWantsToOverwrite = false;
                displayPhotoSectionDialog();
            }
        }else{
            Snackbar.make(getActivity().getCurrentFocus(),"The active profile has no photo. Go under Manage Profiles to change that",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void editPhotoItem(){
        displayPhotoSectionDialog();
    }

    private void displayPhotoSectionDialog(){
        View newView = inflater.inflate(R.layout.cv_photo_options_dialog, null,   false);
        final ImageView borderColorImageView = (ImageView)newView.findViewById(R.id.photoBorderColorImageView);
        borderColorImageView.setBackgroundColor(Color.rgb(userPhotoBorderColorRed,userPhotoBorderColorGreen,userPhotoBorderColorBlue));
        RadioGroup photoTypeRadioGroup = (RadioGroup)newView.findViewById(R.id.photoTypeRadioGroup);
        RadioButton circularPhotoRB = (RadioButton)newView.findViewById(R.id.circularPhotoRadioButton);
        RadioButton rectangularPhotoRB = (RadioButton)newView.findViewById(R.id.rectangularPhotoRadioButton);
        SeekBar userPhotoBorderThicknessSeekBar = (SeekBar)newView.findViewById(R.id.userPhotoBorderThicknessSeekBar);
        final TextView borderWidthTextView = (TextView)newView.findViewById(R.id.borderWidthTextView);
        userPhotoBorderThicknessSeekBar.setMax(10);
        userPhotoBorderThicknessSeekBar.setProgress(userPhotoBorderWidth);

        userPhotoBorderThicknessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userPhotoBorderWidth = seekBar.getProgress();
                borderWidthTextView.setText("Border width = " + userPhotoBorderWidth);
            }
        });

        borderColorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChromaDialog.Builder()
                        .initialColor(Color.rgb(userPhotoBorderColorRed,userPhotoBorderColorGreen,userPhotoBorderColorBlue))
                        .colorMode(ColorMode.RGB) // RGB, ARGB, HVS, CMYK, CMYK255, HSL
                        .indicatorMode(IndicatorMode.DECIMAL) //HEX or DECIMAL; Note that (HSV || HSL || CMYK) && IndicatorMode.HEX is a bad idea
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int color) {
                                borderColorImageView.setBackgroundColor(color);
                                String colorString = ChromaUtil.getFormattedColorString(color, false);
                                Toast.makeText(getContext(), "Color picked is " + colorString, Toast.LENGTH_SHORT).show();
                                userPhotoBorderColorRed = Integer.parseInt(colorString.substring(1,3),16);
                                userPhotoBorderColorGreen = Integer.parseInt(colorString.substring(3,5),16);
                                userPhotoBorderColorBlue = Integer.parseInt(colorString.substring(5,7),16);
                            }
                        })
                        .create()
                        .show(getActivity().getSupportFragmentManager(), "ChromaDialog");
            }
        });

        if(userPhotoType == PhotoType.CIRCULAR){
            circularPhotoRB.setChecked(true);
            rectangularPhotoRB.setChecked(false);
        }
        else{
            circularPhotoRB.setChecked(false);
            rectangularPhotoRB.setChecked(true);
        }

        final AlertDialog editPhotoDialog = new AlertDialog.Builder(getActivity())
                .setView(newView)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        displayRefreshedPhotoSection();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setTitle("Photo Options")
                .setIcon(R.drawable.ic_person_black_24dp)
                .setCancelable(false)
                .create();

        editPhotoDialog.show();
    }

    private void displayRefreshedPhotoSection(){
        if(profilesResource.get(activeProfileIndex).getPhoto() != null){
            userPhoto.setVisibility(View.VISIBLE);
            userPhoto.setBorderColor(Color.rgb(userPhotoBorderColorRed,userPhotoBorderColorGreen,userPhotoBorderColorBlue));
            userPhoto.setBorderWidth(userPhotoBorderWidth);
        }
        else
            userPhoto.setVisibility(View.GONE);
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

        userPhoto = (CircularImageView)getView().findViewById(R.id.cvPhotoImageView);

        objectivesLinearLayout = (LinearLayout) getView().findViewById(R.id.cvObjectivesLinearLayout);
        skillsLinearLayout = (LinearLayout)getView().findViewById(R.id.cvSkillsLinearLayout);
        personalTraitsLinearLayout = (LinearLayout) getView().findViewById(R.id.cvPersonalTraitsLinearLayout);
        experienceLinearLayout = (LinearLayout) getView().findViewById(R.id.cvExperienceLinearLayout);
        interestsLinearLayout = (LinearLayout) getView().findViewById(R.id.cvInterestsLinearLayout);

        objectivesTableRow = (TableRow)getView().findViewById(R.id.cvObjectivesTableRow);
        skillsTableRow = (TableRow)getView().findViewById(R.id.cvSkillsTableRow);
        experienceTableRow = (TableRow)getView().findViewById(R.id.cvExperienceTableRow);
        personalTraitsTableRow = (TableRow)getView().findViewById(R.id.cvPersonalTraitsTableRow);
        interestsTableRow = (TableRow)getView().findViewById(R.id.cvInterestsTableRow);

       layoutInflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private void setLayoutsOnClickListeners(){
        objectivesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked objLayout", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.OBJECTIVES_ITEM){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.OBJECTIVES_ITEM;
                    setActionBarMenuVisibility(true);
                }

                //editObjectivesItem();
            }
        });
        skillsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked skillLay", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.SKILLS_ITEM){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.SKILLS_ITEM;
                    setActionBarMenuVisibility(true);
                }

                //editSkillsItem();
            }
        });
        personalTraitsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked traitsLay", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.PERSONAL_TRAITS_ITEM){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.PERSONAL_TRAITS_ITEM;
                    setActionBarMenuVisibility(true);
                }
            }
        });
        experienceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked expLay", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.EXPERIENCE_ITEM){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.EXPERIENCE_ITEM;
                    setActionBarMenuVisibility(true);
                }
            }
        });
        interestsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked interestsLay", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.INTERESTS_ITEM){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.INTERESTS_ITEM;
                    setActionBarMenuVisibility(true);
                }
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked photo", Toast.LENGTH_SHORT).show();
                if(currentlyFocusedOnType == FocusedType.PHOTO){ //clicked once more to unmatch the iten
                    currentlyFocusedOnType = FocusedType.NONE;
                    setActionBarMenuVisibility(false);
                }
                else{
                    currentlyFocusedOnType = FocusedType.PHOTO;
                    setActionBarMenuVisibility(true);
                }
            }
        });
    }

    private void promptUserBeforeDeletingItem(){
        new  AlertDialog.Builder(getContext())
                .setTitle("Delete item ")
                .setMessage("Are you sure you want to delete the " + currentlyFocusedOnType.getNsme() + "? This will permanently delete the whole paragraph from your CV")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentlyFocusedItem();
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create().show();
    }

    private void deleteCurrentlyFocusedItem() {
        switch (currentlyFocusedOnType) {
            case PHOTO:
                Snackbar.make(getActivity().getCurrentFocus(), "delete image", Snackbar.LENGTH_SHORT).show();
                userPhoto.setVisibility(View.GONE);
                break;
            case PERSONAL_TRAITS_ITEM:
                break;
            case EXPERIENCE_ITEM:
                break;
            case INTERESTS_ITEM:
                break;
            case SKILLS_ITEM:
                skillsLinearLayout.removeAllViews();
                skillsTableRow.setVisibility(View.GONE);
                break;
            case OBJECTIVES_ITEM:
                objectivesLinearLayout.removeAllViews();
                objectivesTableRow.setVisibility(View.GONE);
                break;
            default:
                Snackbar.make(getActivity().getCurrentFocus(), "There was nothing selected to delete", Snackbar.LENGTH_SHORT).show();
        }
        currentlyFocusedOnType = FocusedType.NONE;
        setActionBarMenuVisibility(false);
    }

    private void editCurrentlyFocusedItem(){
        switch(currentlyFocusedOnType){
            case PHOTO:
                Snackbar.make(getActivity().getCurrentFocus(), "edit image", Snackbar.LENGTH_SHORT).show();
                editPhotoItem();
                break;
            case PERSONAL_TRAITS_ITEM:
                break;
            case EXPERIENCE_ITEM:
                break;
            case INTERESTS_ITEM:
                break;
            case SKILLS_ITEM:
                editSkillsItem();
                break;
            case OBJECTIVES_ITEM:
                editObjectivesItem();
                break;
            default:
                Snackbar.make(getActivity().getCurrentFocus(),"There was nothing selected to delete",Snackbar.LENGTH_SHORT).show();
        }
        currentlyFocusedOnType = FocusedType.NONE;
        setActionBarMenuVisibility(false);
    }
}