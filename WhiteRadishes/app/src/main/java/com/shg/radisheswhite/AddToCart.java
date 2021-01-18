package com.shg.radisheswhite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.HashMap;

import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;

public class AddToCart extends AppCompatActivity {

    ImageView addbtn, close;
    EditText quantity;
    String uid,pid;
    FirebaseAuth auth;
    DocumentReference freference;
    CollectionReference freference2;
    String quantity2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        addbtn = findViewById(R.id.add_button);
        close = findViewById(R.id.closebtn);
        quantity = findViewById(R.id.quantity);
        Bundle bundle = getIntent().getExtras();
        pid= bundle.getString("stuff");
        uid = bundle.getString("stuff2");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(AddToCart.this, MainActivity.class);
                startActivity(i);
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();
                freference = FirebaseFirestore.getInstance().document("Farmer/"+uid + "/product/" + pid);
                freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                        if(documentSnapshot.exists()){
                            quantity2 = documentSnapshot.get("quantity").toString();
                            if(parseInt(quantity2) < parseInt(quantity.getText().toString())){
                                Toast.makeText(AddToCart.this, "Please enter valid Quantity.", Toast.LENGTH_SHORT).show();
                            }
                            else if(parseInt(quantity2) >= parseInt(quantity.getText().toString())){
                                freference2 = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("name", documentSnapshot.get("name").toString());
                                hashMap.put("quantity", quantity.getText().toString());
                                hashMap.put("price", documentSnapshot.get("price").toString());
                                hashMap.put("farmer_id", uid);
                                hashMap.put("product_id", pid);
                                freference2.add(hashMap).addOnCompleteListener(task ->{
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddToCart.this, "Product added to cart Successfully!", Toast.LENGTH_LONG).show();
                                        close.performClick();
                                    }
                                    else{
                                        Toast.makeText(AddToCart.this, "Error adding to Cart. Please try again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        }
                        else{
                            Toast.makeText(AddToCart.this, "Product is currently unavailable.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}