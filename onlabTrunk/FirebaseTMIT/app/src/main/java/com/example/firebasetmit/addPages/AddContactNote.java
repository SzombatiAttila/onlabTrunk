package com.example.firebasetmit.addPages;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

public class AddContactNote {
    private String documentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private GeoPoint location;
    private int priority;

    public AddContactNote() {
        //public no-arg constructor needed
    }

    public AddContactNote(String firstName, String lastName, String email, String phone, GeoPoint location, int priority) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPriority() {
        return priority;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}