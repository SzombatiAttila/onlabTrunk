package com.example.firebasetmit.addPages;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

public class AddPlaceNote {
    private String documentId;
    private String placeName;
    private String time;
    private String type;
    private GeoPoint location;
    private int priority;

    public AddPlaceNote() {
        //public no-arg constructor needed
    }

    public AddPlaceNote(String mName, String time, String type, GeoPoint location, int priority) {
        this.placeName = mName;
        this.time = time;
        this.type = type;
        this.location = location;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public String getTime() {
        return this.time;
    }

    public String getType() {
        return this.type;
    }

    public int getPriority() {
        return this.priority;
    }


    public GeoPoint getLocation() {
        return this.location;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
