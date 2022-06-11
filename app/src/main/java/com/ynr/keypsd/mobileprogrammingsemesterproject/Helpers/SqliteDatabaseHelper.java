package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import static android.content.Context.MODE_PRIVATE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.ALREADY_IN_FIREBASE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.SP_NAME;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.DATABASE_TABLE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.DATE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.DB_NAME;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.LOCATION;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.MAIN_TEXT;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.MEDIA_FILE_URI;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.MODE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.PASSWORD;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants.TITLE;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.DbConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SplittableRandom;

public class SqliteDatabaseHelper {

    SQLiteDatabase memoriesDatabase;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;


    public SqliteDatabaseHelper(Context activity) {
        memoriesDatabase = activity.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        mPrefs = activity.getSharedPreferences(SP_NAME, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
    }

    public void createTable() {
        memoriesDatabase.execSQL("CREATE TABLE IF NOT EXISTS Memory(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Date VARCHAR,Mode Integer,Location VARCHAR, Title VARCHAR, MainText VARCHAR," +
                "MediaFileUri VARCHAR, Password VARCHAR);");
    }

    public List<Memory> getMemoriesFromDatabase(){

        Cursor resultSet = memoriesDatabase.rawQuery("Select * from " + DATABASE_TABLE,null);
        List<Memory> memoryList = new ArrayList<>();
        while (resultSet.moveToNext()) {
            int id = resultSet.getInt(0);
            String[] dateValues = resultSet.getString(1).split("-");
            int year = Integer.parseInt(dateValues[0]);
            int month = Integer.parseInt(dateValues[1]);
            int day = Integer.parseInt(dateValues[2]) - 1;

            Date date = new Date(year, month, day);
            int mode = resultSet.getInt(2);

            String latLngStr = resultSet.getString(3);
            LatLng latLng = null;

            if(!latLngStr.equals("")){
                String[] coordinates = latLngStr.split(":");
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                latLng = new LatLng(latitude, longitude);
            }
            String title = resultSet.getString(4);
            String mainText = resultSet.getString(5);
            String mediaFileUri = resultSet.getString(6);
            String password = resultSet.getString(7);

            memoryList.add(new Memory(id, date, mode, title, mainText, mediaFileUri, latLng, password));
        }

        return memoryList;
    }

    public void insertMemory(Memory memory){
        Date date = memory.getDate();

        ContentValues args = new ContentValues();
        args.put(DATE, date.getYear() + "-" + date.getMonth() + "-" + date.getDate());
        args.put(MODE, memory.getMode());

        String locationStr = "";
        if(memory.getLatLng() != null)
            locationStr = memory.getLatLng().latitude + ":" + memory.getLatLng().longitude;

        args.put(LOCATION, locationStr);
        args.put(TITLE, memory.getTitle());
        args.put(MAIN_TEXT, memory.getMainText());
        args.put(MEDIA_FILE_URI, memory.getMediaUri());
        args.put(PASSWORD, memory.getPassword());

        memoriesDatabase.insert(DATABASE_TABLE, null, args);

        prefsEditor.putBoolean(ALREADY_IN_FIREBASE, false);
        prefsEditor.commit();
    }

    public void updateMemory(Memory memory) {

        Date date = memory.getDate();

        ContentValues args = new ContentValues();
        args.put(DATE, date.getYear() + "-" + date.getMonth() + "-" + date.getDate());
        args.put(MODE, memory.getMode());
        if(memory.getLatLng() != null)
            args.put(LOCATION, memory.getLatLng().latitude + ":" + memory.getLatLng().longitude);
        else
            args.put(LOCATION, "");
        args.put(TITLE, memory.getTitle());
        args.put(MAIN_TEXT, memory.getMainText());
        args.put(MEDIA_FILE_URI, memory.getMediaUri());
        args.put(PASSWORD, memory.getPassword());

        memoriesDatabase.update(DATABASE_TABLE, args,"Id = " + memory.getId(), null);

        prefsEditor.putBoolean(ALREADY_IN_FIREBASE, false);
        prefsEditor.commit();
    }

    public void deleteMemory(int id) {
        memoriesDatabase.delete(DATABASE_TABLE, "Id = " + id, null);

        prefsEditor.putBoolean(ALREADY_IN_FIREBASE, false);
        prefsEditor.commit();
    }


}
