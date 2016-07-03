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

        moveProfileCreationButtonToLastPosition();

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rowView = inflater.inflate(R.layout.profile_choice_spinner, parent, false);
        TextView name = (TextView)rowView.findViewById(R.id.nameTextView);
        CircleImageView profileImageView = (CircleImageView) rowView.findViewById(R.id.profilePhotoImageViewMiniature);

        name.setText(profilesResource.get(position).getName());
        profileImageView.setImageBitmap(profilesResource.get(position).getPhoto());

        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        final View dropDownView = inflater.inflate(R.layout.profile_choice_spinner_dropdown, parent, false);

        TextView dropdownName = (TextView)dropDownView.findViewById(R.id.dropdownNameTextView);
        CircleImageView dropdownProfileImageView = (CircleImageView) dropDownView.findViewById(R.id.dropdownProfilePhotoImageViewMiniature);

        if(position == profilesResource.size()-1){
            dropdownName.setText("Create new profile");
            dropDownView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)activity).onNavigationItemSelected(new ActionMenuItem(activity.getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
                }
            });
            dropdownProfileImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_add_black_24dp));
        }else{
            dropdownName.setText(profilesResource.get(position).getName());
            dropdownProfileImageView.setImageBitmap(profilesResource.get(position).getPhoto());
        }

        return dropDownView;
    }
    private void moveProfileCreationButtonToLastPosition(){
        boolean foundCreationButton = false;
        for(int i=0;i<profilesResource.size()&&!foundCreationButton;i++){
            if(profilesResource.get(i).equals(new Profile())){
                foundCreationButton = true;
                Log.d("EEEEEEEEEEEEEEE", "PROFILE EQUALS NEW EMPTY PROFILE");
                profilesResource.remove(i);
            }
        }
        profilesResource.add(new Profile());
    }
}
