package com.example.radek.cv_creator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Radek on 2016-07-03.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cv_creator.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_PROFILES = "CREATE TABLE " + DatabaseConstants.PROFILE_TABLE
            + "("
            + DatabaseConstants.COLUMN_ID + " integer primary key AUTOINCREMENT, "
            + DatabaseConstants.COLUMN_NAME + " text not null, "
            + DatabaseConstants.COLUMN_GENDER + " text not null, "
            + DatabaseConstants.COLUMN_EMAIL + " text not null, "
            + DatabaseConstants.COLUMN_PHONE_NUMBER + " text, "
            + DatabaseConstants.COLUMN_ADDRESS_1 + " text, "
            + DatabaseConstants.COLUMN_ADDRESS_2 + " text, "
            + DatabaseConstants.COLUMN_ADDRESS_3 + " text, "
            + DatabaseConstants.COLUMN_PHOTO + " blob, "
            + DatabaseConstants.COLUMN_DOB + " text not null " + ")";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PROFILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.PROFILE_TABLE);
        onCreate(sqLiteDatabase);    }
}
