package com.example.radek.cv_creator;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Radek on 2016-06-30.
 */
public class ProfileChoiceSpinnerAdapter extends ArrayAdapter<Profile> {

    Activity activity;
    Context context;
    ArrayList<Profile> profilesResource;
    LayoutInflater inflater;

    public ProfileChoiceSpinnerAdapter(Activity activity, ArrayList<Profile> profilesResource) {
        super(activity, R.layout.profile_choice_spinner, profilesResource);
        this.activity = activity;
        this.context = (Context)activity;
        this.profilesResource = profilesResource;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rowView = inflater.inflate(R.layout.profile_choice_spinner, parent, false);
        TextView name = (TextView)rowView.findViewById(R.id.nameTextView);
        CircleImageView profileImageView = (CircleImageView) rowView.findViewById(R.id.profilePhotoImageViewMiniature);

        name.setText(profilesResource.get(position).getName());
        //profileImageView.setImageBitmap(profilesResource.get(position).getPhoto());

        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        final View dropDownView = inflater.inflate(R.layout.profile_choice_spinner_dropdown, parent, false);

        TextView dropdownName = (TextView)dropDownView.findViewById(R.id.dropdownNameTextView);
        CircleImageView dropdownProfileImageView = (CircleImageView) dropDownView.findViewById(R.id.dropdownProfilePhotoImageViewMiniature);

        dropdownName.setText(profilesResource.get(position).getName());
        //dropdownProfileImageView.setImageBitmap(profilesResource.get(position).getPhoto());

        return dropDownView;
    }
}
