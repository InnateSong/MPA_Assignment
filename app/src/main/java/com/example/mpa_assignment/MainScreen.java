package com.example.mpa_assignment;

import android.content.Intent;
import android.os.Bundle;
// Imports: Android
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
// Imports: androidx
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
// Imports: Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Variables
    private DrawerLayout drawer;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;

    String EmailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Instantiating the Tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Grabbing the email from the Firebase Auth db
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        EmailString = mAuth.getEmail();

        // Drawer View instantiated
        drawer = findViewById(R.id.drawer_layout);

        // 1. START: Navigation View instantiated (Allows for the menu button to open the drawer)
        NavigationView navigationView = findViewById(R.id.nav_view);
        // 2.START Setting the navigation text view to the current logged in users details
        View headerView = navigationView.getHeaderView(0);

        // reference to the db
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // reference to the children
        DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid());
        // event listener for event on change
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // snapchat stored as object
                UserObject user = dataSnapshot.getValue(UserObject.class);
                // find the header text views
                TextView navEmail = (TextView) headerView.findViewById(R.id.UserEmailFirebase);
                TextView navUsername = headerView.findViewById(R.id.UserNameFirebase);
                // set the values to the text edits
                navUsername.setText(user.getUsername());
                navEmail.setText(user.getEmail());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase:", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // 2. END
        navigationView.setNavigationItemSelectedListener(this);
        // 1. END

        // Toggles stay in place
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Makes sure that the selected fragment doesn't change when the orientation changes
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        // For every option selected on the navigation, a fragment replaces the current fragment in place
        switch (menuItem.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
                toolbar.setTitle("Home");
                break;
            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void checkCurrentUser(){
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null) {
            Toast.makeText(getApplicationContext(), "User already signed in!", Toast.LENGTH_SHORT).show();
            /*
            Sign current user out
            Go to the login page again (Update UI)
             */
        }
    }
}