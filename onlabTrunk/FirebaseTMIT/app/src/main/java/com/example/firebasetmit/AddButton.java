package com.example.firebasetmit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasetmit.addPages.AddActivity;
import com.example.firebasetmit.addPages.AddContact;
import com.example.firebasetmit.addPages.AddPlace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddButton extends AppCompatActivity {


    androidx.cardview.widget.CardView mContact;
    androidx.cardview.widget.CardView mPlace;
    androidx.cardview.widget.CardView mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);


        mContact = (CardView)findViewById(R.id.AddContacts);
        mPlace = (CardView)findViewById(R.id.AddPlaces);
        mActivity = (CardView)findViewById(R.id.AddActivity);

        //Get User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Show email on toast
        Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();

        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddButton.this, AddContact.class);
                AddButton.this.startActivity(myIntent);
            }
        });

        mPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddButton.this, AddPlace.class);
                AddButton.this.startActivity(myIntent);
            }
        });

        mActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddButton.this, AddActivity.class);
                AddButton.this.startActivity(myIntent);
            }
        });
    }
}
