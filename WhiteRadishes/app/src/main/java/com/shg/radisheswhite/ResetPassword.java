package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    ImageButton reset;
    EditText re_email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset = findViewById(R.id.btn_reset);
        re_email = findViewById(R.id.re_email);

        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = re_email.getText().toString();
                if(email.equals("")){
                    Toast.makeText(ResetPassword.this, "Email ID Required", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Check your Email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPassword.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}