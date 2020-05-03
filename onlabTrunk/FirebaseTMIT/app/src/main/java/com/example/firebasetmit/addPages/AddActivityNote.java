package com.example.firebasetmit.addPages;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

public class AddActivityNote {
    private String documentId;
    private String placeName;
    private String time;
    private String type;
    private String nameList;
    private GeoPoint location;
    private int priority;

    public AddActivityNote() {
        //public no-arg constructor needed
    }

    public AddActivityNote(String name, String time, String type, String nameList, GeoPoint location, int priority) {
        this.placeName = name;
        this.time = time;
        this.type = type;
        this.location = location;
        this.nameList = nameList;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getTime() {
        return time;
    }

    public String getNameList() {
        return nameList;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
