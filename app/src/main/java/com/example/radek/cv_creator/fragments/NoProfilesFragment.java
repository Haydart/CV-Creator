package com.example.radek.cv_creator.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.radek.cv_creator.R;

/**
 * Created by Radek on 2016-06-30.
 */
public class NoProfilesFragment extends Fragment{

    OnProfileCreateButtonClickListener onProfileCreateButtonClickListener;
    FragmentActivity activity;
    Button createProfileButton;

    public NoProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onProfileCreateButtonClickListener = (OnProfileCreateButtonClickListener) context;
            activity = (FragmentActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " musi implementowac OnWyborOpcjiListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onProfileCreateButtonClickListener.onNoProfileButtonClick();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_no_profiles, container, false);
        createProfileButton= (Button)fragmentView.findViewById(R.id.noProfilesCreateButton);
        return fragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        createProfileButton = null;
    }

    public interface OnProfileCreateButtonClickListener
    {
        void onNoProfileButtonClick();
    }
}

