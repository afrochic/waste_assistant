package com.afrochic.waste_assistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Homepage extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private UserRepo userRepo;

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        userRepo = new UserRepo(this);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        TextView balutTitle = findViewById(R.id.store_view_name);
        TextView balutDescription = findViewById(R.id.store_news);
        ImageView balutImage = findViewById(R.id.user_avatar);

        balutTitle.setText(getIntent().getStringExtra("dTitle"));
        balutDescription.setText(getIntent().getStringExtra("dDescription"));

        Glide.with(this).load(getIntent().getIntExtra("dImage", 0)).into(balutImage);

        setSupportActionBar(toolbar);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_foreground);

        //Set username:
        userRepo.getUser(new OnComplete() {
            @Override
            public void onComplete(Object o) {
                User user = (User) o;
                balutTitle.setText(user.username);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        final PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };


        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {
            String appUrl = "https://github.com/afrochic/waste_assistant.git";
            switch (item.getItemId()) {

                case R.id.client:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    startActivity(new Intent(Homepage.this, Client.class));
                    return true;

                case R.id.contact_us:
                    item.setChecked(true);
                    Intent callUs = new Intent(Intent.ACTION_DIAL);
                    callUs.setData(Uri.parse("tel:0113741711"));
                    drawerLayout.closeDrawers();
                    startActivity(callUs);
                    return true;

                case R.id.about_us:
                    item.setChecked(true);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(appUrl));
                    drawerLayout.closeDrawers();
                    startActivity(i);
                    return true;

                case R.id.signout:
                    item.setChecked(true);
                    FirebaseAuth.getInstance().signOut();
                    drawerLayout.closeDrawers();
                    startActivity(new Intent(Homepage.this, login1.class));
                    return true;

                case R.id.share_app:
                    item.setChecked(true);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Take a look at this app, " + appUrl);

                    Intent chooser = Intent.createChooser(shareIntent, "Share via");

                    drawerLayout.closeDrawers();

                    if (shareIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    }
                    return true;

                default:
                    return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
