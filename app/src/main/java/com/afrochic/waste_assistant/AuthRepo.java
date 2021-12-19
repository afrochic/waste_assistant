package com.afrochic.waste_assistant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AuthRepo {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Context context;

    public AuthRepo(Context context) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        this.context = context;
        if (auth.getCurrentUser() != null)
            context.startActivity(new Intent(context, Homepage.class));
    }

    public void register(String username, String email, String password) {
        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            if (task.isSuccessful()) {
                addUser(FirebaseAuth.getInstance().getUid(), username, email);
            }
        });
    }

    public void addUser(String uid, String username, String email) {
        DatabaseReference ref = database.getReference("users").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    Log.d("Auth", "user is null");
                    ref.setValue(new User(username, email));
                    context.startActivity(new Intent(context, Homepage.class));
                }
                context.startActivity(new Intent(context, Homepage.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
