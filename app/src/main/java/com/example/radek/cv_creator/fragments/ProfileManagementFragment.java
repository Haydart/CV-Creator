package com.example.radek.cv_creator.fragments;

/**
 * Created by Radek on 2016-07-01.
 */
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.radek.cv_creator.DbBitmapUtility;
import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.ProfileListViewAdapter;
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

    private MenuItem editProfile;
    private MenuItem deleteProfile;

    private int lastExpandedPosition = -1;
    private int expandedCount;

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
        expandableListView.setAdapter(new ProfileListViewAdapter(activity, profilesResource));
        expandableListView.setIndicatorBounds(850,950);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                expandedCount++;

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;

                Snackbar asd = Snackbar.make(getView(),"Turn on the editing menu",Snackbar.LENGTH_SHORT);
                asd.show();
                toggleActionBarMenuVisibility();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                expandedCount--;
                if(expandedCount==0){
                    Toast.makeText(getContext(), "Turn off the editing menu", Toast.LENGTH_SHORT).show();
                    toggleActionBarMenuVisibility();
                }
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
        Log.d("MENU ITEM = ","" + editProfile);
        deleteProfile = (MenuItem)menu.findItem(R.id.deleteProfile);
        deleteProfile.setIcon(DbBitmapUtility.resizeImage(
                getContext(),
                R.drawable.ic_delete_white_24dp,DbBitmapUtility.dpToPx(getContext(),64),DbBitmapUtility.dpToPx(getContext(),64)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.editProfile){
            Snackbar.make(getActivity().getCurrentFocus(),"EDIT PROFILE",Snackbar.LENGTH_SHORT).show();
            profileManagementEventListener.onEditProfilePressed(lastExpandedPosition);
        }else if(id == R.id.deleteProfile){
            Snackbar.make(getActivity().getCurrentFocus(),"DELETE PROFILE",Snackbar.LENGTH_SHORT).show();
            profileManagementEventListener.onDeleteProfilePressed(lastExpandedPosition);
        }

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

    private int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void toggleActionBarMenuVisibility(){
        editProfile.setVisible(!editProfile.isVisible());
        deleteProfile.setVisible(!deleteProfile.isVisible());
    }
}
