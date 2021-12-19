package com.afrochic.waste_assistant;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepo {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Context context;

    public UserRepo(Context context) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        this.context = context;
    }

    public void getUser(OnComplete complete) {
        final DatabaseReference ref = database.getReference("users");
        ref.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                complete.onComplete(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                complete.onFailure(error.getMessage());
            }
        });
    }
}
