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

public class UserProfile extends AppCompatActivity {

    private ImageButton usercarts;
    private ImageButton userorders;

    private TextView profilename;
    private TextView profilelocation;
    private TextView profileemail;
    private TextView profileaddr;
    private TextView contact;
    private ImageButton editbtn;
    DocumentReference freference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usercarts = findViewById(R.id.usercart);
        userorders = findViewById(R.id.userorders);
        profilename = findViewById(R.id.profilename);
        profilelocation = findViewById(R.id.locval);
        profileemail = findViewById(R.id.emailval);
        profileaddr = findViewById(R.id.addressval);
        contact = findViewById(R.id.contactval);
        editbtn = findViewById(R.id.editbtn);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        usercarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //onclick for cart
                Intent intent2 = new Intent(UserProfile.this, ShoppingCart.class);
                startActivity(intent2);
            }
        });

        //getting profile info and setting it
        freference = FirebaseFirestore.getInstance().document("Customer/" + userid);
        freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                    profilename.setText(documentSnapshot.get("name").toString());
                    profilelocation.setText(documentSnapshot.get("location").toString());
                    profileaddr.setText(documentSnapshot.get("address").toString());
                    profileemail.setText(firebaseUser.getEmail());
                    contact.setText(documentSnapshot.get("contact").toString());
                }
                else{
                    Toast.makeText(UserProfile.this, "Error displaying order.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        userorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onclick for orders
                Intent intent2 = new Intent(UserProfile.this, UserOrders.class);
                startActivity(intent2);
            }
        });


        editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("stuff", profilename.getText().toString());
                b.putString("stuff2", profilelocation.getText().toString());
                b.putString("stuff3", contact.getText().toString());
                b.putString("stuff4", profileaddr.getText().toString());
                Intent i=new Intent(UserProfile.this, EditCustomerProfile.class);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

    }
}