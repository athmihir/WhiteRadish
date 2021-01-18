package com.shg.radisheswhite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FarmerProfile extends AppCompatActivity {

    private ImageButton btnInvent;
    private ImageButton btnorders;
    private ImageButton logout;
    private ImageButton editbtn;

    private TextView profilename;
    private TextView profilelocation;
    private TextView profilerating;
    private TextView profileorders;
    private TextView contact;
    DocumentReference freference;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);

        btnInvent = (ImageButton) findViewById(R.id.btnInventory);
        btnorders = (ImageButton) findViewById(R.id.btnorders);
        logout = findViewById(R.id.logoutbtn);
        editbtn = findViewById(R.id.editbtn);

        profilename = (TextView) findViewById(R.id.profilename);
        profilelocation = (TextView) findViewById(R.id.locval);
        profilerating = (TextView) findViewById(R.id.ratingval);
        profileorders = (TextView) findViewById(R.id.orderval);
        contact =  findViewById(R.id.contactval);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        //getting profile info and setting it
        freference = FirebaseFirestore.getInstance().document("Farmer/" + userid);
        freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                    profilename.setText(documentSnapshot.get("name").toString());
                    profilelocation.setText(documentSnapshot.get("location").toString());
                    profilerating.setText(documentSnapshot.get("rating").toString());
                    profileorders.setText(documentSnapshot.get("orders").toString());
                    contact.setText(documentSnapshot.get("contact").toString());
                }
                else{
                    Toast.makeText(FarmerProfile.this, "Error displaying order.", Toast.LENGTH_SHORT).show();
                }
            }
        });




        btnInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmerProfile.this, Inventory.class);
                startActivity(intent);
            }
        });

        btnorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmerProfile.this, FarmerOrder.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //logout person via database connectivity
                startActivity(new Intent(FarmerProfile.this, HomeActivity.class));
            }
        });


        editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("stuff", profilename.getText().toString());
                b.putString("stuff2", profilelocation.getText().toString());
                b.putString("stuff3", contact.getText().toString());
                Intent i=new Intent(FarmerProfile.this, EditFarmerProfile.class);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });


    }
}

