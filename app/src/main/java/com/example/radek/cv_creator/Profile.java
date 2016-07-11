package com.example.radek.cv_creator;

import android.app.LauncherActivity;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Radek on 2016-06-29.
 */
public class Profile implements Serializable, Parcelable{

    private static int instancesCount;
    private int ID;
    private String name;
    private String gender;
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private transient Bitmap photo; // bitmaps go to internal storage, we don`t want to have THAT much binary JSON data, do we?
    private String DOB;

    public Profile(int ID, String name, String gender, String email, String phoneNumber, String addressLine1, String addressLine2, String addressLine3, Bitmap photo, String DOB) {
        this.ID = ID;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.photo = photo;
        this.DOB = DOB;
    }

    public Profile(){

    }

    protected Profile(Parcel in) {
        name = in.readString();
        gender = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getCVFileName(){
        StringBuilder result = new StringBuilder();
        String[] stringComp = name.split(" ");
        for(String el : stringComp){
            result.append(el);
            result.append("_");
        }
        result.append("resume.pdf");
        return result.toString();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws WrongDataFormatException{
        if(true)
            this.name = name;
        else throw new WrongDataFormatException("Wrong name");
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setDOB(int day, int month, int year) {
        this.DOB = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws WrongDataFormatException{
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws WrongDataFormatException{
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            this.email = email;
        else throw new WrongDataFormatException("Wrong email address");
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeParcelable(photo, i);
    }
}