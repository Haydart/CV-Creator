package com.example.radek.cv_creator;

import java.io.Serializable;

/**
 * Created by Radek on 2016-05-28.
 */
public class Settings implements Serializable {

    private static Settings instance = null;
    private static String sharedPrefsName = "sharedPrefs";
    private static String profilesSaveDirName = "profiles";

    private Settings(){}

    public static Settings getInstance()
    {
        if(instance == null)
            instance = new Settings();
        return instance;
    }

    public String getSharedPrefsName() {
        return sharedPrefsName;
    }
    public String getProfilesSaveDirName() {return profilesSaveDirName; }
}
