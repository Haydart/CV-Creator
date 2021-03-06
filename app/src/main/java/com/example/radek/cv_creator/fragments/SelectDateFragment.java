package com.example.radek.cv_creator.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Radek on 2016-07-02.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);

        EditDatePickerListener callingFragment = (EditDatePickerListener) getTargetFragment();
        callingFragment.onFinishEditDatePickerDialog(String.valueOf(yy) + "-" + ((mm<10)?"0":"") + String.valueOf(mm) + "-" +((dd<10)?"0":"") + String.valueOf(dd));
        this.dismiss();
    }
    public void populateSetDate(int year, int month, int day) {

    }

    public interface EditDatePickerListener {
        void onFinishEditDatePickerDialog(String dateText);
    }
}