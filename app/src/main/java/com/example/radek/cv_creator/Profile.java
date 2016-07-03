package com.example.radek.cv_creator;

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
public class Profile implements Serializable, Parcelable {

    private String name;
    private String DOB;
    private String gender = "male";
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private transient Bitmap photo; // bitmaps go to internal storage, we don`t want to have THAT much binary JSON data, do we?

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

    public String getName() {
        return name;
    }

    public void setName(String name) throws WrongDataFormatException{
        if(true)
            this.name = name;
        else throw new WrongDataFormatException("Wrong name");
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