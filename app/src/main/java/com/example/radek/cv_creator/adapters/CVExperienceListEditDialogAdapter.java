package com.example.radek.cv_creator.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.radek.cv_creator.R;

import java.util.ArrayList;

/**
 * Created by Radek on 2016-07-09.
 */
public class CVExperienceListEditDialogAdapter extends ArrayAdapter<ArrayList<String>> {

    private Activity activity;
    private Context context;
    private ArrayList<ArrayList<String>> experienceResource;
    private ArrayList<Integer> checkedExperienceItemIndexes;
    private LayoutInflater inflater;

    public CVExperienceListEditDialogAdapter(Activity activity, ArrayList<ArrayList<String>> experienceResource) {
        super(activity, R.layout.profile_choice_spinner, experienceResource);
        this.activity = activity;
        this.context = (Context)activity;
        this.experienceResource = experienceResource;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedExperienceItemIndexes = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView;

            rowView = inflater.inflate(R.layout.cv_experience_edit_dialog, parent, false);
            final TextView experienceDescription = (TextView) rowView.findViewById(R.id.cvExperienceDescriptionTextView);
            experienceDescription.setText(experienceResource.get(position).get(0));

            experienceDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    experienceDescription.requestFocus();

                    View newView = inflater.inflate(R.layout.cv_experience_dialog_add_new,null);
                    final EditText name = (EditText)newView.findViewById(R.id.positionName);
                    final EditText timeSpan = (EditText)newView.findViewById(R.id.positionTimeSpan);
                    final EditText description = (EditText)newView.findViewById(R.id.positionDescription);

                    name.setText(experienceResource.get(position).get(0));
                    timeSpan.setText(experienceResource.get(position).get(1));
                    description.setText(experienceResource.get(position).get(2));

                    final AlertDialog editPhotoDialog = new AlertDialog.Builder(getContext())
                            .setView(newView)
                            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    experienceDescription.setText(name.getText().toString());
                                    ArrayList<String> experienceItem = new ArrayList<String>();
                                    experienceItem.add(name.getText().toString());
                                    experienceItem.add(timeSpan.getText().toString());
                                    experienceItem.add(description.getText().toString());
                                    experienceResource.set(position,experienceItem);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setTitle("Experience item")
                            .setIcon(R.drawable.ic_person_black_24dp)
                            .setCancelable(false)
                            .create();

                    editPhotoDialog.show();
                }
            });
            final CheckBox experienceDeleteCheckbox = (CheckBox) rowView.findViewById(R.id.cvExperienceDeleteCheckBox);
        experienceDeleteCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(experienceDeleteCheckbox.isChecked()){
                    checkedExperienceItemIndexes.add(position);
                }else{
                    checkedExperienceItemIndexes.remove(new Integer(position)); //had to perform packing manually
                }
            }
        });
        return rowView;
    }

    public void setExperienceResource(ArrayList<ArrayList<String>> experience) {this.experienceResource = experience;}
    public ArrayList<ArrayList<String>> getExperienceResource(){return experienceResource;}
    public ArrayList<Integer> getCheckedExperienceItemIndexes() {
        return checkedExperienceItemIndexes;
    }
}
