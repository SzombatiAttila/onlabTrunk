package com.example.firebasetmit.addPages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasetmit.FirebaseObject.ActivityObject;
import com.example.firebasetmit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPlace extends AppCompatActivity {

    private static final String TAG = "AddPlace";
    private EditText mName;
    private EditText mFree;
    private EditText locationAddrLong;
    private EditText locationAddrLat;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance()
            .getReference("Activities/Places/Place");

    private ActivityObject mActivityObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        mName = findViewById(R.id.placeName);
        locationAddrLong = findViewById(R.id.locationLong);
        locationAddrLat = findViewById(R.id.locationLat);
        mFree = findViewById(R.id.free);

    }

    public void saveNote(View v) {
        String placeName = mName.getText().toString();
        String mFinalLocationLat = locationAddrLat.getText().toString();
        String mFinalLocationLng = locationAddrLong.getText().toString();
        String free = String.valueOf(mFree);
        Log.d(TAG, "saveNote: location is " + mFinalLocationLat + mFinalLocationLng);


        mActivityObj = new ActivityObject(placeName, mFinalLocationLat, mFinalLocationLng, free);

        mRootRef.push().setValue(mActivityObj);

        Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show();
    }
}
