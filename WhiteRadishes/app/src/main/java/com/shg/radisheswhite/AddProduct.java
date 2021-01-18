package com.shg.radisheswhite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddProduct extends AppCompatActivity {

    EditText product_name, quantity, price;
    ImageButton add_button;
    ImageButton closeButton;
    FirebaseAuth auth;

    CollectionReference freference;
    long pid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        product_name = findViewById(R.id.product_name);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        add_button = findViewById(R.id.add_button);
        closeButton = findViewById(R.id.closebtn);
        auth = FirebaseAuth.getInstance();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();
                freference = FirebaseFirestore.getInstance().collection("Farmer/"+userid + "/product");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", product_name.getText().toString());
                hashMap.put("quantity", quantity.getText().toString());
                hashMap.put("price", price.getText().toString());

                freference.add(hashMap).addOnCompleteListener(task ->{
                    if (task.isSuccessful()) {
                        Toast.makeText(AddProduct.this, "Product added Successfully!", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(AddProduct.this, "Error adding Product. Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
                product_name.setText("");
                price.setText("");
                quantity.setText("");

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProduct.this, Inventory.class));
            }
        });
    }
}