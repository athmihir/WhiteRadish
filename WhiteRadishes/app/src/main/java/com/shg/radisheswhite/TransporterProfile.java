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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransporterProfile extends AppCompatActivity {

    private ImageButton transorders;
    private ImageButton transhistory;
    private ImageButton logout, editbtn;

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
        setContentView(R.layout.activity_transporter_profile);

        transorders = findViewById(R.id.transorders);
        transhistory = findViewById(R.id.transhistory);
        logout = findViewById(R.id.logoutbtn);
        editbtn = findViewById(R.id.editbtn);

        profilename = findViewById(R.id.profilename);
        profilelocation = findViewById(R.id.location);
        profilerating = findViewById(R.id.rating);
        profileorders = findViewById(R.id.orders);
        contact = findViewById(R.id.contact);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        //getting profile info and setting it
        freference = FirebaseFirestore.getInstance().document("Transporter/" + userid);
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
                    Toast.makeText(TransporterProfile.this, "Error displaying order.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        transorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransporterProfile.this, TransporterOrders.class));
            }
        });

        transhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransporterProfile.this, TransporterHistory.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //logout person via database connectivity
                startActivity(new Intent(TransporterProfile.this, HomeActivity.class));
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("stuff", profilename.getText().toString());
                b.putString("stuff2", profilelocation.getText().toString());
                b.putString("stuff3", contact.getText().toString());
                Intent i=new Intent(TransporterProfile.this, EditTransporterProfile.class);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

    }
}