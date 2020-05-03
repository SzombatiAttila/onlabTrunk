package com.example.firebasetmit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firebasetmit.addPages.AddContactNote;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

public class Contacts extends AppCompatActivity {

    private TextView textViewData1;

    static GeoPoint mLocation;
    static GeoPoint loc;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Activities/Users/People");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

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
                    AddContactNote note = documentSnapshot.toObject(AddContactNote.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String firstN = note.getFirstName();
                    String lastN = note.getLastName();
                    String emailAddr = note.getEmail();
                    String phoneNumb = note.getPhone();
                    loc = note.getLocation();
                    System.out.println("Valami: " + loc);
                    int priority = note.getPriority();

                    data += "ID: " + documentId
                            + "\nFirst name is : " + firstN + "\nLast name is : " + lastN
                            + "\nEmail address is : " + emailAddr + "\nPhone number: " + phoneNumb
                            + "\nLocation is : " + loc + "\nPriority: " + priority + "\n\n";
                }

                textViewData1.setText(data);
            }
        });
        mLocation = loc;
    }
}
