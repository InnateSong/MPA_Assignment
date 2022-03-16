package com.example.mpa_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private String emailAd, pass;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

    }

    public void LoginSubmitButton(View view){

        EditText editEmail = findViewById(R.id.LoginEmailAddress);
        EditText editPass = findViewById(R.id.LoginPassword);

        // EMAIL
        checkEmptyEmail(editEmail);

        // PASSWORD
        checkEmptyPassword(editPass);

        // Connect to the DB and Login
        mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPass.getText().toString())

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Call next activity
                            Intent intent = new Intent(Login.this, MainScreen.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void checkEmptyEmail(EditText editEmail){
        String email = editEmail.getText().toString();

        if(email.isEmpty())
        {
            editEmail.setError("Email is empty");
            editEmail.requestFocus();
            return;
        }
    }

    public void checkEmptyPassword(EditText passEdit){
        String password = passEdit.getText().toString().trim();

        if(password.isEmpty()){
            passEdit.setError("Password is Empty!"); passEdit.requestFocus();
            return;
        }
    }

}