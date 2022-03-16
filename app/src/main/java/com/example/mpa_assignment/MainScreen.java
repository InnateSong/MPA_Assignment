package com.example.mpa_assignment;

import android.content.Intent;
import android.os.Bundle;
// Imports: Android
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
import com.google.android.material.navigation.NavigationView;
// Imports: Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Variables
    private DrawerLayout drawer;
    private FirebaseUser mAuth;
    String EmailString;
    TextView emailHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Instantiating the Tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Grabbing the email from the Firebase  Auth db
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        EmailString = mAuth.getEmail();

        // Drawer View instantiated
        drawer = findViewById(R.id.drawer_layout);

        // Navigation View instantiated (Allows for the menu button to open the drawer)
        NavigationView navigationView = findViewById(R.id.nav_view);
        //      Setting the navigation text view to the current logged in users details
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.UserEmailFirebase);
        navUsername.setText(EmailString);
        navigationView.setNavigationItemSelectedListener(this);


        //
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
        // For every option selected on the navigation, a fragment replaces the current fragment in place
        switch (menuItem.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
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