package com.example.firebasetmit.FirebaseObject;

import com.google.firebase.firestore.GeoPoint;

public class ActivityObject {

    public String placeName;
    public String locationLat;
    public String locationLng;
    public String isAvailable;

    public ActivityObject(){

    }


    public ActivityObject(String placeName, String locationLat, String locationLng, String isAvailable) {
        this.placeName = placeName;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.isAvailable = isAvailable;
        //loc = new GeoPoint(locationLat, locationLng);
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(String locationLng) {
        this.locationLng = locationLng;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
}
