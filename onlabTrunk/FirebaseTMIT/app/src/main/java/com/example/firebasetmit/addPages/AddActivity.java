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

public class AddActivity extends AppCompatActivity {

    private EditText name;
    private EditText time;
    private EditText type;
    private EditText peopleList;
    private EditText locationAddrLong;
    private EditText locationAddrLat;
    private EditText mpriority;
    public GeoPoint p;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Activities/Activity/Program");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.placeName);
        time = findViewById(R.id.closingTime);
        type = findViewById(R.id.type);
        peopleList = findViewById(R.id.nameList);
        locationAddrLong = findViewById(R.id.locationLong);
        locationAddrLat = findViewById(R.id.locationLat);
        mpriority = findViewById(R.id.prio);
    }

    public void saveNote(View v) {
        String placeName = name.getText().toString();
        String placeType = type.getText().toString();
        String placeTime = time.getText().toString();
        String mPeopleList = peopleList.getText().toString();
        double mLat = Double.parseDouble(locationAddrLat.getText().toString());
        double mLong = Double.parseDouble(locationAddrLong.getText().toString());
        p = new GeoPoint(mLat,mLong);

        if (mpriority.length() == 0) {
            mpriority.setText("0");
        }
        int priority = Integer.parseInt(mpriority.getText().toString());
        AddActivityNote note = new AddActivityNote(placeName, placeTime, placeType, mPeopleList,p, priority);
        notebookRef.add(note);
        Toast.makeText(AddActivity.this, "Note saved", Toast.LENGTH_LONG).show();
    }
}
