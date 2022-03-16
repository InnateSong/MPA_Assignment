package com.example.mpa_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;


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

    public void Registration(View view){

        // AGE RANGE Values
        ageRange = getRange();

        // OCCUPANCY Values
        occupancy = getOccupancy();

        // DAILY DEVICE USAGE Values
        usage = getUsage();

        // EMAIL
        checkValidEmail();
        EditText editEmail = findViewById(R.id.emailAddress);
        emailAd = editEmail.getText().toString();

        // PASSWORD + CONFIRM PASSWORD
        checkValidPassword();
        EditText passEdit = findViewById(R.id.password);
        pass = passEdit.getText().toString().trim();

        // Connect to the DB and populate it
        mAuth.createUserWithEmailAndPassword(emailAd, pass);
        // CHECK IF USER ALREADY EXISTS

        // Call next activity
        Intent intent = new Intent(this, Register_phase_2.class);
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
        EditText editEmail = findViewById(R.id.emailAddress);
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
        EditText passEdit = findViewById(R.id.password);
        String password = passEdit.getText().toString().trim();
        EditText passCEdit = findViewById(R.id.passwordC);
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