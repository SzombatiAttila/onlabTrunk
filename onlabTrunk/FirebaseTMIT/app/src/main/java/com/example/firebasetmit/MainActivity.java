package com.example.firebasetmit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    ImageView log_out_btn;
    androidx.cardview.widget.CardView mapsID;
    androidx.cardview.widget.CardView activityID;
    androidx.cardview.widget.CardView mAdd;
    androidx.cardview.widget.CardView mContact;
    androidx.cardview.widget.CardView mPlaces;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log_out_btn = (ImageView)findViewById(R.id.log_out_btn);
        mapsID = (CardView) findViewById(R.id.MapsPage);
        mContact = (CardView) findViewById(R.id.ContactsPage);
        activityID = (CardView)findViewById(R.id.activityPage);
        mPlaces = (CardView)findViewById(R.id.PagesPage);
        mAdd = (CardView)findViewById(R.id.AddPage);

        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Contacts.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AddButton.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        mapsID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MapsDirection.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        activityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Activities.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Places.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                log_out_btn.setEnabled(false);
                                showSignInOptions();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //init providers

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builed
                new AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builed
                //new AuthUI.IdpConfig.FacebookBuilder().build(), //FB Builed
                new AuthUI.IdpConfig.GoogleBuilder().build() //Google Builed
        );
        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Show email on toast
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                log_out_btn.setEnabled(true);
                mapsID.setEnabled(true);
            }
            else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
