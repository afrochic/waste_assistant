package com.afrochic.waste_assistant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class login1 extends AppCompatActivity {
    EditText et_email;
    EditText et_password;
    Button btn_login;
    Button btn_swipeRight;
    FirebaseAuth mAuth;
    TextView forgetpass;
    AuthRepo authRepo;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        authRepo = new AuthRepo(this);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_swipeRight = findViewById(R.id.btn_swipeRight);
        mAuth = FirebaseAuth.getInstance();
        forgetpass=findViewById(R.id.forgetpass);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });


        btn_login.setOnClickListener(v -> {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (email.isEmpty()) {
                    et_email.setError("Email is empty");
                    et_password.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Enter the valid email");
                    et_email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    et_password.setError("Password is empty");
                    et_password.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    et_password.setError("Length of password is more than 6");
                    et_password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(login1.this, Homepage.class));
                    } else {
                        Toast.makeText(login1.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                });
            });
            btn_swipeRight.setOnClickListener(v -> startActivity(new Intent(login1.this, MainActivity.class)));
        }
    ProgressDialog loadingBar;

    public void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(login1.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(login1.this,"Error Occured",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(login1.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


}