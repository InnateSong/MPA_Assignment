package com.example.mpa_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register_phase_1 extends AppCompatActivity {

    // Variables

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String emailAd, pass;
    int selectedId;
    String ageRange, occupancy, usage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Instantiate FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }

    public void RegisterSubmitButton(View view){

        // AGE RANGE Values
        ageRange = getRange();

        // OCCUPANCY Values
        occupancy = getOccupancy();

        // DAILY DEVICE USAGE Values
        usage = getUsage();

        // EMAIL
        checkValidEmail();
        EditText editEmail = findViewById(R.id.RegisterEmailAddress);
        emailAd = editEmail.getText().toString();

        // PASSWORD + CONFIRM PASSWORD
        checkValidPassword();
        EditText passEdit = findViewById(R.id.RegisterPassword);
        pass = passEdit.getText().toString().trim();

        // USERNAME
        EditText userNameEdit = findViewById(R.id.RegisterName);
        String userName = userNameEdit.getText().toString().trim();

        // Connect to the DB and populate it
        mAuth.createUserWithEmailAndPassword(emailAd, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    // Create User Object
                    UserObject user = new UserObject(emailAd, userName);

                    // User from Firebase
                    FirebaseUser Firebaseuser = mAuth.getCurrentUser();

                    // User reference from database for "User Accounts"
                    DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference("UserAccounts");

                    // Adding values to the database under "User Accounts" reference
                    UserReference.child(Firebaseuser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // :C
                            }
                        }

                    });
                }
            }
        });

        // Call next activity
        Intent intent = new Intent(this, Register_phase_2.class);
        startActivity(intent);

    }

    public void RegisterBack(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public String getRange(){
        radioGroup = (RadioGroup) findViewById(R.id.ageRange);
        // get selected radio button from radioGroup
        selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        return radioButton.getText().toString();
    }

    public String getOccupancy(){
        radioGroup = (RadioGroup) findViewById(R.id.occupancy);
        selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        return radioButton.getText().toString();
    }

    public String getUsage(){
        radioGroup = (RadioGroup) findViewById(R.id.deviceUsage);
        selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        return radioButton.getText().toString();
    }

    public void checkValidEmail(){
        EditText editEmail = findViewById(R.id.RegisterEmailAddress);
        String email = editEmail.getText().toString();

        if(email.isEmpty())
        {
            editEmail.setError("Email is empty");
            editEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editEmail.setError("Enter the valid email address");
            editEmail.requestFocus();
            return;
        }
    }

    public void checkValidPassword(){
        EditText passEdit = findViewById(R.id.RegisterPassword);
        String password = passEdit.getText().toString().trim();
        EditText passCEdit = findViewById(R.id.RegisterConfirmPassword);
        String passwordC = passCEdit.getText().toString().trim();

        if(password.isEmpty()){
            passEdit.setError("Password is Empty!"); passEdit.requestFocus();
            return;
        }
        if(passwordC.isEmpty()){
            passCEdit.setError("Password is Empty!"); passCEdit.requestFocus();
            return;
        }
        if(!password.matches(passwordC)){
           passEdit.setError("Does not match!"); passEdit.requestFocus();
           passCEdit.setError("Does not match!");passCEdit.requestFocus();
           return;
        }
       //String passStrength = "^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$";
    }

}