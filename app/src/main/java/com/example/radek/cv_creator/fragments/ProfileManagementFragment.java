package com.example.radek.cv_creator.fragments;

/**
 * Created by Radek on 2016-07-01.
 */
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
public class ProfileManagementFragment extends Fragment{

    ProfileManagementEventListener profileManagementEventListener;
    FragmentActivity activity;

    public ProfileManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            profileManagementEventListener = (ProfileManagementEventListener) context;
            activity = (FragmentActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //add fragment listeners here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_manage_profiles, container, false);
        //get erferences for fragment items here
        return fragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //make every reference null here to prevent memory leak
    }

    public interface ProfileManagementEventListener
    {
        void onSumfin();
    }
}
