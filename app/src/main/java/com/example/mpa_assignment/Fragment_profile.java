package com.example.mpa_assignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_profile extends Fragment {

    // Reference
    private DatabaseReference mDatabase;

    public static Fragment_profile newInstance() {
        Fragment_profile fragment = new Fragment_profile();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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
                TextView Email = (TextView) view.findViewById(R.id.ProfileEmailView);
                TextView Username = view.findViewById(R.id.ProfileUsernameView);
                // set the values to the text edits
                Username.setText(user.getUsername());
                Email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase:", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Hiding some fragment elements and setting other visible for authentication to happen
        LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.second);
        layout2.setVisibility(View.GONE);

        return view;
    }
}