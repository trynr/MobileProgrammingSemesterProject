package com.ynr.keypsd.mobileprogrammingsemesterproject.Models;

import android.location.Location;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

public class Memory implements Serializable {

    private int id;
    private Date date;
    private int mode;
    // private Location location;
    private String title;
    private String mainText;
    private String mediaUri;
    private String latLng;
    private String password;

    public Memory(int id, Date date, int mode, String title, String mainText, String mediaUri, LatLng latLng, String password) {
        this.id = id;
        this.date = date;
        this.mode = mode;
        this.title = title;
        this.mainText = mainText;
        this.mediaUri = mediaUri;
        if(latLng != null)
            this.latLng = latLng.latitude + ":" + latLng.longitude;
        this.password = password;
    }

    public Memory(Date date, int mode, String title, String mainText, String mediaUri, LatLng latLng, String password) {
        this.date = date;
        this.mode = mode;
        this.title = title;
        this.mainText = mainText;
        this.mediaUri = mediaUri;
        if(latLng != null)
            this.latLng = latLng.latitude + ":" + latLng.longitude;
        this.password = password;
    }

    /*
    public Memory(Date date, int mode, Location location, String title, String mainText, Uri mediaUri, String password) {
        this.date = date;
        this.mode = mode;
        this.location = location;
        this.title = title;
        this.mainText = mainText;
        this.mediaUri = mediaUri;
        this.password = password;
    }
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /*
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public LatLng getLatLng() {
        if(latLng == null)
            return null;

        String[] coordinates = latLng.split(":");
        return new LatLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }

    public void setLatLng(LatLng latLngObject) {
        this.latLng = latLngObject.latitude + ":" + latLngObject.longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
