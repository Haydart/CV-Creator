package com.example.radek.cv_creator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.view.menu.ActionMenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.mikhaellopez.circularimageview.CircularImageView;

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
        Log.d("AAAAAAAAAAAAAAAAAAAAAAA","adapter constructor called");
        Log.d("AAAAAAAAAAAAAAAAAAAAAAA", "profilesCount: " +profilesResource.size());
        this.activity = activity;
        this.context = (Context)activity;
        this.profilesResource = profilesResource;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rowView = inflater.inflate(R.layout.profile_choice_spinner, parent, false);
        TextView name = (TextView)rowView.findViewById(R.id.nameTextView);
        CircularImageView profileImageView = (CircularImageView) rowView.findViewById(R.id.profilePhotoImageViewMiniature);

        name.setText(profilesResource.get(position).getName());
        profileImageView.setImageBitmap(profilesResource.get(position).getPhoto());

        if(profilesResource.get(position).getGender().equals(context.getResources().getString(R.string.gender_option_1))){ //male
            profileImageView.setBorderColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(profilesResource.get(position).getGender().equals(context.getResources().getString(R.string.gender_option_2))) { //female
            profileImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        }
        return rowView;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        final View dropDownView = inflater.inflate(R.layout.profile_choice_spinner_dropdown, parent, false);
        TextView dropdownName = (TextView)dropDownView.findViewById(R.id.dropdownNameTextView);
        CircularImageView dropdownProfileImageView = (CircularImageView) dropDownView.findViewById(R.id.dropdownProfilePhotoImageViewMiniature);

        if(profilesResource.get(position).getGender().equals(context.getResources().getString(R.string.gender_option_1))){ //male
            dropdownProfileImageView.setBorderColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(profilesResource.get(position).getGender().equals(context.getResources().getString(R.string.gender_option_2))) { //female
            dropdownProfileImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
        }

        dropdownName.setText(profilesResource.get(position).getName());

        if(profilesResource.get(position).getPhoto()!=null)
            dropdownProfileImageView.setImageBitmap(profilesResource.get(position).getPhoto());
        else
            dropdownProfileImageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.blank_profile_pic));

        return dropDownView;
    }

    public void setProfilesResource(ArrayList<Profile> profiles) {this.profilesResource = profiles;}
    public ArrayList<Profile> getProfilesResource(){return profilesResource;}
}
