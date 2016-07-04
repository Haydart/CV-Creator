package com.example.radek.cv_creator;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radek on 2016-05-28.
 */
public class ContentStorageManager
{
    private SharedPreferences sharedPreferences;
    SQLiteDatabaseHelper dbHelper;
    SQLiteDatabase database;
    private Context activityContext;
    public static volatile ContentStorageManager instance;
    private static Object lock = new Object();

    private ContentStorageManager(Context activityContext)
    {
        dbHelper = new SQLiteDatabaseHelper(activityContext);
        database = dbHelper.getWritableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityContext);
    }

    public static ContentStorageManager getInstance() {
        ContentStorageManager r = instance;
        if (r == null) {
            synchronized (lock) {    // While we were waiting for the lock, another
                r = instance;        // thread may have instantiated the object.
                if (r == null) {
                    r = new ContentStorageManager(MainActivity.getInstance().getApplicationContext());
                    instance = r;
                }
            }
        }
        return r;
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

    public void addProfileToDatabase(Profile profile) throws SQLiteException {
        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseConstants.COLUMN_NAME, profile.getName());
        cv.put(DatabaseConstants.COLUMN_GENDER, profile.getGender());
        cv.put(DatabaseConstants.COLUMN_EMAIL, profile.getEmail());
        cv.put(DatabaseConstants.COLUMN_PHONE_NUMBER, profile.getPhoneNumber());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_1, profile.getAddressLine1());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_2, profile.getAddressLine2());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_3, profile.getAddressLine3());
        cv.put(DatabaseConstants.COLUMN_PHOTO , DbBitmapUtility.getBytes(profile.getPhoto()));
        cv.put(DatabaseConstants.COLUMN_DOB, profile.getDOB());

        database.insert(DatabaseConstants.PROFILE_TABLE, null, cv );
        database.close();
    }

    public ArrayList getProfilesFromDatabase(){
        ArrayList<Profile> profiles = new ArrayList<>();
        database = dbHelper.getReadableDatabase();

        return profiles;
    }
}
