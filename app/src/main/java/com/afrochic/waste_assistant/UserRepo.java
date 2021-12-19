package com.afrochic.waste_assistant;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseMessaging messaging;
    private Context context;

    private final static String TAG = "UserRepo";

    public UserRepo(Context context) {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        messaging = FirebaseMessaging.getInstance();
        this.context = context;
        initCloudMessaging();
    }

    private void initCloudMessaging() {
        //Calling this function silently
        getCloudMessagingDeviceToken(new OnComplete() {
            @Override
            public void onComplete(Object o) {
                String token = (String) o;

                Log.d(TAG, "Device Token: " + token);

                //Update device token id
                DatabaseReference ref = database.getReference("users").child(auth.getCurrentUser().getUid());
                Map<String, Object> map = new HashMap<>();
                map.put("message_token", token);
                ref.updateChildren(map);

            }

            @Override
            public void onFailure(String message) {

            }
        });
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

    public void getCloudMessagingDeviceToken(OnComplete onComplete) {
        messaging.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            onComplete.onFailure(task.getException().getMessage());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        onComplete.onComplete(token);
                    }
                });
    }
}
