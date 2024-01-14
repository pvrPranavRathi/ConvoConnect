package com.example.convoconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.convoconnect.Models.Users;
import com.example.convoconnect.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
//    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if (binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmail.setError("Enter your email");
                    return;
                }

                if (binding.etPassword.getText().toString().isEmpty()) {
                    binding.etPassword.setError("Enter your password");
                    return;
                }

//                progressDialog.show();
                auth.createUserWithEmailAndPassword
                                (binding.etEmail.getText().toString() , binding.etPassword.getText().toString())
                        .addOnCompleteListener (new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Users user = new Users(binding.etUserName.getText().toString(), binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                    database.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}