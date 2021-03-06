package com.example.radek.cv_creator.fragments;

/**
 * Created by Radek on 2016-07-01.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.adapters.ProfileListViewAdapter;
import com.example.radek.cv_creator.R;

import java.util.ArrayList;

/**
 * Created by Radek on 2016-06-30.
 */
public class ProfileManagementFragment extends Fragment {

    private ProfileManagementEventListener profileManagementEventListener;
    private FragmentActivity activity;
    private ArrayList<Profile> profilesResource;
    static Bundle args;
    private ExpandableListView expandableListView;
    private TextView noProfilesTextView;
    private ImageView noProfilesImageView;
    private ProfileListViewAdapter profileListViewAdapter;

    private MenuItem editProfile;
    private MenuItem deleteProfile;

    private int lastExpandedPosition = -1;

    public ProfileManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profilesResource = new ArrayList<>();

        try {
            profileManagementEventListener = (ProfileManagementEventListener) context;
            activity = (FragmentActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_manage_profiles, container, false);
        profilesResource = (ArrayList<Profile>) args.get("profilesResource");
        expandableListView = (ExpandableListView) fragmentView.findViewById(R.id.profilesExpandableListView);
        noProfilesImageView = (ImageView)fragmentView.findViewById(R.id.noProfilesToManageImageView);
        noProfilesTextView = (TextView) fragmentView.findViewById(R.id.noProfilesToManageTextView);
        profileListViewAdapter = new ProfileListViewAdapter(activity, profilesResource);
        expandableListView.setAdapter(profileListViewAdapter);

        if(profilesResource.size()==0){
            noProfilesImageView.setVisibility(View.VISIBLE);
            noProfilesTextView.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }

        //TODO make it independent on screen size
        expandableListView.setIndicatorBounds(850,950);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                setActionBarMenuVisibility(true);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setActionBarMenuVisibility(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_creation_ab_menu, menu);

        editProfile = (MenuItem)menu.findItem(R.id.editProfile); // 0 is settings from MainActivity
        editProfile.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_mode_edit_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
        deleteProfile = (MenuItem)menu.findItem(R.id.deleteProfile);
        deleteProfile.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_delete_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.editProfile){
            profileManagementEventListener.onEditProfilePressed(lastExpandedPosition);
            expandableListView.collapseGroup(lastExpandedPosition);
        }else if(id == R.id.deleteProfile){
            profileManagementEventListener.onDeleteProfilePressed(lastExpandedPosition);
            expandableListView.collapseGroup(lastExpandedPosition);
        }
        setActionBarMenuVisibility(false);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //make every reference null here to prevent memory leak
    }

    public interface ProfileManagementEventListener
    {
        void onEditProfilePressed(int userProfilePosition);
        void onDeleteProfilePressed(int userProfilePosition);
    }

    private void setActionBarMenuVisibility(boolean value){
        editProfile.setVisible(value);
        deleteProfile.setVisible(value);
    }

    public void notifyProfilesResourceChanged(ArrayList<Profile> newProfilesResource){
        profileListViewAdapter.setProfiles(newProfilesResource);
        profileListViewAdapter.notifyDataSetChanged();
    }
}