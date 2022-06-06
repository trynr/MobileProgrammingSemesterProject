package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private Activity activity;
    private Button datePickerButton;
    private DatePickerDialog datePickerDialog;
    private Date currentSelectedDate;

    public DateHelper(Activity activity, Button datePickerButton) {
        this.activity = activity;
        this.datePickerButton = datePickerButton;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int year = cal.get(Calendar.YEAR);
        return createDateString(day, month, year);
    }

    public void initializeDatePickerForAdd(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        initializeDatePicker(getTodaysDate(), day, month, year);
        currentSelectedDate = new Date(year, month, day);
    }

    public void initializeDatePickerForUpdate(Date date){
        initializeDatePicker(createDateString(date.getDate()+1, date.getMonth()+1, date.getYear()),
                                date.getDate()+1,
                                date.getMonth(),
                                date.getYear()
                            );
        currentSelectedDate = new Date(date.getYear(), date.getMonth(), date.getDate()+1);
    }

    public void initializeDatePicker(String dateStr, int day, int month, int year) {
        DatePickerDialog.OnDateSetListener dataSetListener = (datePicker, y, m, d) -> {
            currentSelectedDate = new Date(y, m, d);
            String date = createDateString(d, m+1, y);
            datePickerButton.setText(date);
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(activity, style, dataSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerButton.setText(dateStr);
    }

    public static String createDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private static String getMonthFormat(int month) {
        switch (month){
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                throw new IllegalArgumentException("Illegal month value: " + month);
        }
    }

    public Date getCurrentSelectedDate() {
        return currentSelectedDate;
    }

    public void openDatePicker(){
        datePickerDialog.show();
    }

}
