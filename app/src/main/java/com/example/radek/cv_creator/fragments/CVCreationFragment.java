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
import android.widget.Spinner;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.ProfileChoiceSpinnerAdapter;
import com.example.radek.cv_creator.R;

import java.util.ArrayList;

public class CVCreationFragment extends Fragment {
    OnCVCreationListener onCVCreationListener;
    FragmentActivity activity;
    Spinner profileChoiceSpinner;
    ArrayList<Profile> profilesResource;
    static Bundle args;
    MenuItem saveCV;

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

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
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

    public interface OnCVCreationListener{
        void onCVCreated();
    }
}