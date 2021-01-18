package com.shg.radisheswhite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText email, password;
    ImageButton login;
    Button forgot_password, go_to_reg;
    FirebaseAuth auth;
    DocumentReference freference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_id_text);
        password = findViewById(R.id.login_password_text);
        login = findViewById(R.id.login_btn);
        forgot_password = findViewById(R.id.forgot_password);
        go_to_reg = findViewById(R.id.goto_register);

        //Login Button Clicking...
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Login.this, "Enter all Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    String userid = firebaseUser.getUid();

                                    freference = FirebaseFirestore.getInstance().collection("Customer").document(userid);
                                    freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot){
                                            if(documentSnapshot.exists()){
                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                    freference = FirebaseFirestore.getInstance().collection("Farmer").document(userid);
                                    freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot){
                                            if(documentSnapshot.exists()){
                                                Intent intent = new Intent(Login.this, FarmerProfile.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                    freference = FirebaseFirestore.getInstance().collection("Transporter").document(userid);
                                    freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot){
                                            if(documentSnapshot.exists()){
                                                Intent intent = new Intent(Login.this, NewOrders.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                }
                            });
                }
            }
        });

        //Moving from Login Page to Reset Password Page
        forgot_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });

        //Moving from Login Page to Register Page
        go_to_reg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}