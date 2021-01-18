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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class EditProduct2 extends AppCompatActivity {

    EditText quantity;
    ImageButton remove, edit, closebtn;
    FirebaseAuth auth;
    String cid, uid, pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product2);

        quantity = findViewById(R.id.quantity);
        remove = findViewById(R.id.remove_button);
        edit = findViewById(R.id.edit_button);
        closebtn = findViewById(R.id.closebtn);

        //close button
        closebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProduct2.this, ShoppingCart.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        cid= bundle.getString("stuff");
        uid= bundle.getString("stuff3");
        pid = bundle.getString("stuff2");
        quantity.setText(bundle.getString("stuff4"));

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        DocumentReference freference = FirebaseFirestore.getInstance().document("Customer/"+userid + "/cart/" + cid);
        //Remove Button pressed
        remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                freference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProduct2.this, "Removed Item from Cart!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(EditProduct2.this, ShoppingCart.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProduct2.this, "Error Removing Item. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Edit Button pressed
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(quantity.getText().toString())) {
                    Toast.makeText(EditProduct2.this, "Enter all Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference freference2 = FirebaseFirestore.getInstance().document("Farmer/"+uid + "/product/" + pid);
                freference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String quantity2 = documentSnapshot.get("quantity").toString();
                            if (parseInt(quantity2) < parseInt(quantity.getText().toString())) {
                                Toast.makeText(EditProduct2.this, "Please enter valid Quantity.", Toast.LENGTH_SHORT).show();
                            } else if (parseInt(quantity2) >= parseInt(quantity.getText().toString())) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("quantity", quantity.getText().toString());

                                freference.set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditProduct2.this, "Cart Item Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(EditProduct2.this, ShoppingCart.class));
                                        } else {
                                            Toast.makeText(EditProduct2.this, "Error editing Cart Item, Please try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(EditProduct2.this, "Product is currently unavailable.", Toast.LENGTH_SHORT).show();
                            remove.performClick();
                        }
                    }
                });
            }
        });

    }
}