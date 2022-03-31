package com.example.mpa_assignment;

import android.content.Intent;
import android.os.Bundle;
// Imports: Android
import android.provider.AlarmClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
// Imports: androidx
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
// Imports: Firebase
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Variables
    private DrawerLayout drawer;
    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;
    private DatePicker simpleDatePicker;
    private TimePicker timePicker1;
    String EmailString;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Instantiating the Tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Grabbing the email from the Firebase Auth db
        mAuth = FirebaseAuth.getInstance().getCurrentUser();

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

        // Create the Table (data taken from the database)
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        // For every option selected on the navigation, a fragment replaces the current fragment in place
        switch (menuItem.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home(), "homeTag").commit();
                toolbar.setTitle("Home");
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_profile(), "profileTag").commit();
                toolbar.setTitle("Profile");
                break;
            case R.id.setAlarm:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Alarm(), "alarmTag").commit();
                toolbar.setTitle("Set Alarm");
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Settings(), "settingsTag").commit();
                toolbar.setTitle("Settings");
                break;
            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // --ONCLICK ACTIONS--------
    public void SetAlarmButton(View view){
        TimePicker timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES,min);
        Toast.makeText(this, "Alarm has been set!", Toast.LENGTH_SHORT).show();
        //startActivity(intent);
    }

    // Delete User Feature
    public void DeleteButton(View view){
    // Grabbing the email from the Firebase Auth db
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.delete();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


    // Password Reset Feature
    public void ForgotPasswordButtonAuth(View view) {
        // References
        EditText editEmail = findViewById(R.id.reAuthEmail);

        if (checkEmptyEmail(editEmail) == false) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(editEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Yeah:", "Email sent.");
                            }
                        }
                    });
        }
    }

    public void ProfileApplyChanges(View view) {
        // Changed email
        EditText changedEmailE = (EditText) findViewById(R.id.ProfileChangeEmail);
        String changedEmailV = changedEmailE.getText().toString().trim();
        // Changed user
        EditText changedUsernameE = (EditText) findViewById(R.id.ProfileChangeUsername);
        String changedUsernameV = changedUsernameE.getText().toString().trim();
        // Changed password
        EditText changedPasswordE = (EditText) findViewById(R.id.ProfileChangePass);
        String changedPasswordV = changedPasswordE.getText().toString().trim();

        if (!changedEmailV.isEmpty() || !changedPasswordV.isEmpty() || !changedUsernameV.isEmpty()) {
            // RE-AUTHENTICATE
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.first);
            layout.setVisibility(View.GONE);

            LinearLayout layout2 = (LinearLayout) findViewById(R.id.second);
            layout2.setVisibility(View.VISIBLE);
        } else {

        }
    }

    public void AuthenticateButton(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Changed email
        EditText changedEmailE = (EditText) findViewById(R.id.ProfileChangeEmail);
        String changedEmailV = changedEmailE.getText().toString().trim();
        // Changed user
        EditText changedUsernameE = (EditText) findViewById(R.id.ProfileChangeUsername);
        String changedUsernameV = changedUsernameE.getText().toString().trim();
        // Changed password
        EditText changedPasswordE = (EditText) findViewById(R.id.ProfileChangePass);
        String changedPasswordV = changedPasswordE.getText().toString().trim();

        // reference to the db
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (!changedEmailV.isEmpty())
            user.updateEmail(changedEmailV)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainScreen.this, "Updated Email", Toast.LENGTH_SHORT).show();
                                // reference to the children
                                DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("email");
                                mPostReference.setValue(changedEmailV);
                            }
                        }
                    });
        if (!changedPasswordV.isEmpty()) {
            user.updatePassword(changedPasswordV)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainScreen.this, "Updated Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (!changedUsernameV.isEmpty()) {
            // reference to the children
            DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("username");
            mPostReference.setValue(changedUsernameV);
        } else {
            return;
        }

        // Reference elements
        EditText AuthEmailE = (EditText) findViewById(R.id.reAuthEmail);
        EditText AuthPassE = (EditText) findViewById(R.id.reAuthPass);

        if ((checkEmptyEmail(AuthEmailE) || checkEmptyPassword(AuthPassE)) == false) {
            // Get string from elements
            String AuthEmailV = AuthEmailE.getText().toString().trim();
            String AuthPassV = AuthPassE.getText().toString().trim();

            // Begin authenticating
            AuthCredential credential = EmailAuthProvider
                    .getCredential(AuthEmailV, AuthPassV);
            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(MainScreen.this, "Authentication Successful!", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
                            } else {
                                Toast.makeText(MainScreen.this, "Authentication not successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            // Restart activity
            finish();
            startActivity(getIntent());
        } else return;
    }

    public void AddProductButton(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_PopulateTable()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void PopulateButton(View view) {
        // Publish all data to the firebase db

        // Product name
        EditText product = (EditText) findViewById(R.id.ProductName);
        String productStr = product.getText().toString().trim();
        // Product calories
        EditText calories = (EditText) findViewById(R.id.CaloricValue);
        String caloricStr = calories.getText().toString().trim();
        // Product date
        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        String day = "" + simpleDatePicker.getDayOfMonth();
        String month = "" + (simpleDatePicker.getMonth() + 1);
        String year = "" + simpleDatePicker.getYear();
        // call format date function
        String FormattedDate = FormatDate(day, month, year);

        // Insert to db
        mDatabase.child("UserAccounts").child(mAuth.getUid()).child("Tracker").child(FormattedDate).child(productStr);
        mDatabase.child("UserAccounts").child(mAuth.getUid()).child("Tracker").child(FormattedDate).child(productStr).child("Caloric_value").setValue(caloricStr);

        // Return to previous fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
    }
    // --ONCLICK ACTIONS--------


    // --CONVENIENCE FUNCTIONS--
    public String FormatDate(String day, String month, String year) {
        String date = day + ":" + month + ":" + year;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:M:yyyy", Locale.ENGLISH);
        LocalDate date2 = LocalDate.parse(date, formatter);
        System.out.println(date); // 2010-01-02
        return date2.toString();
    }

    public void checkCurrentUser() {
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null) {
            Toast.makeText(getApplicationContext(), "User already signed in!", Toast.LENGTH_SHORT).show();
            /*
            Sign current user out
            Go to the login page again (Update UI)
             */
        }
    }

    public boolean checkEmptyEmail(EditText editEmail) {
        String email = editEmail.getText().toString();

        if (email.isEmpty()) {
            editEmail.setError("Email is empty");
            editEmail.requestFocus();
            return true;
        } else return false;
    }

    public boolean checkEmptyPassword(EditText passEdit) {
        String password = passEdit.getText().toString().trim();

        if (password.isEmpty()) {
            passEdit.setError("Password is Empty!");
            passEdit.requestFocus();
            return true;
        } else return false;
    }
    // --CONVENIENCE FUNCTIONS--
}