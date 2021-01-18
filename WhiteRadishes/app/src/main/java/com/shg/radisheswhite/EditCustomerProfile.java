package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class EditCustomerProfile extends AppCompatActivity {

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_profile);

        TextView name = findViewById(R.id.name);
        TextView location = findViewById(R.id.location);
        TextView contactno = findViewById(R.id.contactno);
        TextView address = findViewById(R.id.address);
        ImageButton closeButton = findViewById(R.id.closebtn);
        ImageButton editbtn = findViewById(R.id.editbtn);
        Bundle bundle = getIntent().getExtras();

        name.setText(bundle.getString("stuff"));
        location.setText(bundle.getString("stuff2"));
        contactno.setText(bundle.getString("stuff3"));
        address.setText(bundle.getString("stuff4"));

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();


        //closebutton
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCustomerProfile.this, UserProfile.class));
            }
        });

        //edit profile button
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get product array for specific user
                DocumentReference freference = FirebaseFirestore.getInstance().document("Customer/"+userid );
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("name", name.getText().toString());
                hashMap.put("location", location.getText().toString());
                hashMap.put("contact", contactno.getText().toString());
                hashMap.put("address", address.getText().toString());
                freference.set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditCustomerProfile.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(EditCustomerProfile.this, UserProfile.class));
                        }
                        else{
                            Toast.makeText(EditCustomerProfile.this, "Error updating profile, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }
}