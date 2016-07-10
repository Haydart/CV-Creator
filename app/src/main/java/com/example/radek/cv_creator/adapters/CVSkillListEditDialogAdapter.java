package com.example.radek.cv_creator.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radek.cv_creator.Profile;
import com.example.radek.cv_creator.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by Radek on 2016-07-09.
 */
public class CVSkillListEditDialogAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private Context context;
    private ArrayList<String> skillsResource;
    private ArrayList<Integer> checkedSkillsIndexes;
    private LayoutInflater inflater;

    public CVSkillListEditDialogAdapter(Activity activity, ArrayList<String> skillsResource) {
        super(activity, R.layout.profile_choice_spinner, skillsResource);
        this.activity = activity;
        this.context = (Context)activity;
        this.skillsResource = skillsResource;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedSkillsIndexes = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView;

            rowView = inflater.inflate(R.layout.cv_skills_dialog, parent, false);
            final TextView skillDescription = (TextView) rowView.findViewById(R.id.cvSkillDescriptionTextView);
            skillDescription.setText(skillsResource.get(position));

            skillDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skillDescription.requestFocus();

                    final EditText edittext = new EditText(getContext());
                    edittext.setText(skillDescription.getText().toString());
                    AlertDialog myDialogBox = new AlertDialog.Builder(getContext())
                            //set message, title, and icon
                            .setTitle("Update Skill description")
                            .setView(edittext)

                            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    skillDescription.setText(edittext.getText().toString());
                                    skillsResource.set(position, skillDescription.getText().toString());
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
            });
            final CheckBox skillDeleteCheckbox = (CheckBox) rowView.findViewById(R.id.cvSkillDeleteCheckBox);
        skillDeleteCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(skillDeleteCheckbox.isChecked()){
                    checkedSkillsIndexes.add(position);
                }else{
                    checkedSkillsIndexes.remove(new Integer(position)); //had to perform packing manually
                }
            }
        });

        return rowView;
    }

    public void setSkillsResource(ArrayList<String> skills) {this.skillsResource = skills;}
    public ArrayList<String> getSkillsResource(){return skillsResource;}
    public ArrayList<Integer> getCheckedSkillsIndexes() {
        return checkedSkillsIndexes;
    }
}
