package com.example.radek.cv_creator;

import android.graphics.Bitmap;
import android.util.Patterns;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Radek on 2016-06-29.
 */
public class Profile implements Serializable {

    private String name;
    private Date DOB;
    private String gender = "male";
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private Bitmap photo;

    public Profile(){
        DOB = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws WrongDataFormatException{
        if(name.matches("[a-zA-Z]+"))
            this.name = name;
        else throw new WrongDataFormatException("Wrong name");
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws WrongDataFormatException{
        if(phoneNumber.matches("(\\d{3}-){3}"))
            this.phoneNumber = phoneNumber;
        else throw new WrongDataFormatException("Wrong phone number, format must be xxx-xxx-xxx");
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
}