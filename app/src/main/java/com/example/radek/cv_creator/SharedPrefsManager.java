package com.example.radek.cv_creator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radek on 2016-05-28.
 */
public class SharedPrefsManager
{
    private SharedPreferences sharedPreferences;
    private Context activityContext;

    public SharedPrefsManager(Context activityContext)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityContext);
    }

    public synchronized void saveProfilesList(ArrayList<Profile> profiles)
    {
        String alarmsListJSON = new Gson().toJson(profiles);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(Settings.getInstance().getProfilesSaveDirName(), alarmsListJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public synchronized ArrayList<Profile> loadProfilesList()
    {
        ArrayList<Profile> alarmsList = new ArrayList<>();
        try {
            alarmsList = new Gson().fromJson(sharedPreferences.getString(Settings.getInstance().getProfilesSaveDirName(),"[]"), new TypeToken<List<Profile>>(){}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return alarmsList;
    }
}
