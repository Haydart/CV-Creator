package com.example.radek.cv_creator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by Radek on 2016-07-05.
 */
public class ProfileListViewAdapter extends BaseExpandableListAdapter{

    private Activity activity;
    private Context context;
    private ArrayList<Profile> profilesResource;
    private LayoutInflater inflater;

    public ProfileListViewAdapter(Activity activity, ArrayList<Profile> profilesResource) {
        super();
        this.activity = activity;
        this.context = (Context)activity;
        this.profilesResource = profilesResource;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return profilesResource.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1; // only 1 child for every profile
    }

    @Override
    public Object getGroup(int i) {
        return profilesResource.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return getGroup(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final View parentView = inflater.inflate(R.layout.manage_profiles_parent, null);
        CircularImageView profileImageCircularView = (CircularImageView) parentView.findViewById(R.id.manageProfilesPhotoImageViewMiniature);

        if(profilesResource.get(i).getPhoto()!=null)
            profileImageCircularView.setImageBitmap(profilesResource.get(i).getPhoto());

        if(profilesResource.get(i).getGender().equals(context.getResources().getString(R.string.gender_option_1))){ //male
            profileImageCircularView.setBorderColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(profilesResource.get(i).getGender().equals(context.getResources().getString(R.string.gender_option_2))) { //female
            profileImageCircularView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        }

        TextView textView = (TextView)parentView.findViewById(R.id.manageProfilesNameTextView);
        textView.setText(profilesResource.get(i).getName().toString());
        return parentView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View childView = inflater.inflate(R.layout.manage_profiles_child, null);
        TextView email = (TextView)childView.findViewById(R.id.childMailTextView);
        TextView dob = (TextView)childView.findViewById(R.id.childDOBTextView);
        TextView phone = (TextView)childView.findViewById(R.id.childPhoneTextView);
        TextView addr1 = (TextView)childView.findViewById(R.id.childAddr1TextView);
        TextView addr2 = (TextView)childView.findViewById(R.id.childAddr2TextView);
        TextView addr3 = (TextView)childView.findViewById(R.id.childAddr3TextView);

        email.setText(profilesResource.get(i).getEmail());
        dob.setText(profilesResource.get(i).getDOB());
        phone.setText(profilesResource.get(i).getPhoneNumber());
        addr1.setText(profilesResource.get(i).getAddressLine1());
        addr2.setText(profilesResource.get(i).getAddressLine2());
        addr3.setText(profilesResource.get(i).getAddressLine3());

        return childView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
