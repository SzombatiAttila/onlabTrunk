package com.example.firebasetmit.addPages;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firebasetmit.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class AddContact extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNum;
    private EditText locationAddrLong;
    private EditText locationAddrLat;
    private EditText email;
    private EditText mpriority;
    public GeoPoint p;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Activities/Users/People");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phoneNum = findViewById(R.id.phone);
        locationAddrLong = findViewById(R.id.locationLong);
        locationAddrLat = findViewById(R.id.locationLat);
        email = findViewById(R.id.email);
        mpriority = findViewById(R.id.prio);
    }

    public void saveNote(View v) {
        String mFirst = firstName.getText().toString();
        String mLast = lastName.getText().toString();
        String mPhone = phoneNum.getText().toString();
        double mLat = Double.parseDouble(locationAddrLat.getText().toString());
        double mLong = Double.parseDouble(locationAddrLong.getText().toString());
        p = new GeoPoint(mLat,mLong);
        String mEmail = email.getText().toString();

        if (mpriority.length() == 0) {
            mpriority.setText("0");
        }
        int priority = Integer.parseInt(mpriority.getText().toString());
        AddContactNote note = new AddContactNote(mFirst, mLast, mEmail, mPhone, p, priority);
        notebookRef.add(note);
        Toast.makeText(AddContact.this, "Note saved", Toast.LENGTH_LONG).show();
    }
}
