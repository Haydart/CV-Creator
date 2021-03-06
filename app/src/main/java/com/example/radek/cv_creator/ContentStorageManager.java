package com.example.radek.cv_creator;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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
        this.activityContext = activityContext;
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

    public ContentValues getContentValuesForProfile(Profile profile){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseConstants.COLUMN_NAME, profile.getName());
        cv.put(DatabaseConstants.COLUMN_GENDER, profile.getGender());
        cv.put(DatabaseConstants.COLUMN_EMAIL, profile.getEmail());
        cv.put(DatabaseConstants.COLUMN_PHONE_NUMBER, profile.getPhoneNumber());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_1, profile.getAddressLine1());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_2, profile.getAddressLine2());
        cv.put(DatabaseConstants.COLUMN_ADDRESS_3, profile.getAddressLine3());
        cv.put(DatabaseConstants.COLUMN_DOB, profile.getDOB());

        if(profile.getPhoto()!=null)
            cv.put(DatabaseConstants.COLUMN_PHOTO , DbBitmapUtility.getBytes(profile.getPhoto()));

        return cv;
    }

    public void addProfileToDatabase(Profile profile) throws SQLiteException {
        database = dbHelper.getWritableDatabase();
        Toast.makeText(activityContext, "Adding " + profile.getName(), Toast.LENGTH_SHORT).show();
        database.insert(DatabaseConstants.PROFILE_TABLE, null, getContentValuesForProfile(profile)); // profile ID is ignored - it was set by program, not by DB
        database.close();
    }

    public void deleteAllProfileRecords(){
        database = dbHelper.getWritableDatabase();
        database.delete(DatabaseConstants.PROFILE_TABLE,null,null);
        database.close();
    }

    public void updateProfile(Profile profile){ //we distinguish them by ID held in the database
        database = dbHelper.getWritableDatabase();
        database.update(DatabaseConstants.PROFILE_TABLE, getContentValuesForProfile(profile), DatabaseConstants.COLUMN_ID +"="+profile.getID(), null);
        database.close();
    }

    public void deleteProfile(int databaseProfileID){ //sql injection-proof
        database = dbHelper.getWritableDatabase();

        String whereClause = DatabaseConstants.COLUMN_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(databaseProfileID) };
        database.delete(DatabaseConstants.PROFILE_TABLE, whereClause, whereArgs);
        database.close();
    }


    public ArrayList getProfilesFromDatabase(){
        ArrayList<Profile> profiles = new ArrayList<>();
        database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseConstants.COLUMN_ID,
                DatabaseConstants.COLUMN_NAME,
                DatabaseConstants.COLUMN_GENDER,
                DatabaseConstants.COLUMN_EMAIL,
                DatabaseConstants.COLUMN_PHONE_NUMBER,
                DatabaseConstants.COLUMN_ADDRESS_1,
                DatabaseConstants.COLUMN_ADDRESS_2,
                DatabaseConstants.COLUMN_ADDRESS_3,
                DatabaseConstants.COLUMN_PHOTO,
                DatabaseConstants.COLUMN_DOB
        };

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseConstants.PROFILE_TABLE, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            profiles.add(new Profile(
                    cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ADDRESS_1)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ADDRESS_2)),
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_ADDRESS_3)),

                    cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.COLUMN_PHOTO))==null?null:
                    DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.COLUMN_PHOTO))),

                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_DOB))
            ));
            cursor.moveToNext();
        }
        database.close();

        return profiles;
    }

    public int getHighestDatabaseID(){
        int result = 0;
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT " + DatabaseConstants.COLUMN_ID + " FROM " + DatabaseConstants.PROFILE_TABLE +
                                          " ORDER BY " + DatabaseConstants.COLUMN_ID + " DESC LIMIT 1",null);

        cursor.moveToFirst();
        if(cursor.getCount()>0)
            result = cursor.getInt(0);
        database.close();
        return result;
    }
}
