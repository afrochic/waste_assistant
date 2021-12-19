package com.afrochic.waste_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_email;
    EditText et_password;
    EditText et_repasswords;
    Button btn_register;
    Button btn_swipeLeft;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_repasswords = findViewById(R.id.et_repasswords);
        btn_register = findViewById(R.id.btn_register);
        btn_swipeLeft = findViewById(R.id.btn_swipeLeft);

        mAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            if (email.isEmpty()) {
                et_email.setError("Email is empty");
                et_email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.setError("Enter the valid email address");
                et_email.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                et_password.setError("Enter the password");
                et_password.requestFocus();
                return;
            }
            if (password.length() < 6) {
                et_password.setError("Length of the password should be more than 6");
                et_password.requestFocus();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Toast.makeText(MainActivity.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Homepage.class));
                } else {
                    Toast.makeText(MainActivity.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                }
            });

        });
        btn_swipeLeft.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, login1.class)));
    }
}