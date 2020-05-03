package com.example.firebasetmit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firebasetmit.addPages.AddActivityNote;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

public class Activities extends AppCompatActivity {

    private TextView textViewData1;

    static GeoPoint mLocation;
    static GeoPoint locPlace;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Activities/Activity/Program");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        textViewData1 = findViewById(R.id.text_view_data1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AddActivityNote note = documentSnapshot.toObject(AddActivityNote.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String name = note.getPlaceName();
                    String time = note.getTime();
                    String type = note.getType();
                    String mPeople = note.getNameList();
                    locPlace = note.getLocation();
                    System.out.println("Valami: " + locPlace);
                    int priority = note.getPriority();

                    data += "ID: " + documentId
                            + "\nActivity name is : " + name + "\nClosing time is : " + time
                            + "\nPlace type is : " + type + "\nThe people list is : " + mPeople
                            + "\nLocation is : " + locPlace + "\nPriority: " + priority + "\n\n";
                }

                textViewData1.setText(data);
            }
        });
        mLocation = locPlace;
    }
}
