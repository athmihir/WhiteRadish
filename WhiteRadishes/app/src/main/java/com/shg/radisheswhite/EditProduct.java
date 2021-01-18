package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditProduct extends AppCompatActivity {

    EditText quantity, price;
    ImageButton remove, edit, close;
    FirebaseAuth auth;
    String pid, pname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        remove = findViewById(R.id.remove_button);
        edit = findViewById(R.id.edit_button);
        close = findViewById(R.id.closebtn);

        Bundle bundle = getIntent().getExtras();
        pid= bundle.getString("stuff");
        pname = bundle.getString("stuff2");

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        //Get product array for specific user
        DocumentReference freference = FirebaseFirestore.getInstance().document("Farmer/"+userid + "/product/" + pid);

        //Remove Button pressed
        remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                freference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProduct.this, "Product Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(EditProduct.this, Inventory.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProduct.this, "Error Deleting Product. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Edit Button pressed
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(price.getText().toString()) || TextUtils.isEmpty(quantity.getText().toString())) {
                    Toast.makeText(EditProduct.this, "Enter all Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("name", pname);
                hashMap.put("price", price.getText().toString());
                hashMap.put("quantity", quantity.getText().toString());

                freference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProduct.this, "Product Updated Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(EditProduct.this, Inventory.class));
                        }
                        else{
                            Toast.makeText(EditProduct.this, "Error editing product, Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        //Close button is pressed
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(EditProduct.this, Inventory.class));
            }
        });


    }
}