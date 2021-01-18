package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    Spinner catagory;
    EditText email, password, confirm_password;
    ImageButton register;
    FirebaseAuth auth;
    DocumentReference freference;

    //makes sure selected item is white in spinner
    private AdapterView.OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(18);

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Spinner / Dropdown implementation
        catagory = findViewById(R.id.type_of_account);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        catagory.setOnItemSelectedListener(OnCatSpinnerCL);

        email = findViewById(R.id.account_email_text);
        password = findViewById(R.id.password_text);
        confirm_password = findViewById(R.id.confirm_password_text);
        register = findViewById(R.id.register_btn);
        auth = FirebaseAuth.getInstance();

        //Register Button Clicking...
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_type = catagory.getSelectedItem().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_con_password = confirm_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_con_password)) {
                    Toast.makeText(Register.this, "Enter all Credentials", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_con_password)) {
                    Toast.makeText(Register.this, "Both password fields should be same", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_type, txt_email, txt_password);
                }
            }
        });
    }

    //Add users to database
    public void register(final String usertype, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();
//
                            freference = FirebaseFirestore.getInstance().collection(catagory.getSelectedItem().toString()).document(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("usertype", usertype);
                            hashMap.put("status", "offline");
                            if(usertype.equals("Farmer") ){
                                hashMap.put("name", "");
                                hashMap.put("location", "");
                                hashMap.put("rating", "");
                                hashMap.put("orders", "");
                                hashMap.put("contact", "");
                            }
                            if(usertype.equals("Transporter") ){
                                hashMap.put("name", "");
                                hashMap.put("location", "");
                                hashMap.put("rating", "");
                                hashMap.put("orders", "");
                                hashMap.put("contact", "");
                            }
                            if(usertype.equals("Customer") ){
                                hashMap.put("name", "");
                                hashMap.put("location", "");
                                hashMap.put("address", "");
                                hashMap.put("contact", "");
                            }
                            freference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(usertype.equals("Customer")){
                                            Intent intent = new Intent(Register.this, MainActivity.class);
//
                                            startActivity(intent);
                                        }else if(usertype.equals("Farmer")){
                                            Intent intent = new Intent(Register.this, FarmerProfile.class);
//
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(Register.this, NewOrders.class);
//
                                            startActivity(intent);
                                        }
//
                                    }
                                    else{
                                        Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
//
                        }else{
                            Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}