package com.example.mpa_assignment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.logging.type.HttpRequest;


import java.net.URI;

public class Fragment_Home extends Fragment {

    private DatabaseReference mDatabase;

    public static Fragment_Home newInstance() {
        Fragment_Home fragment = new Fragment_Home();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // reference to the db
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // reference to the children
        DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("Tracker");
        // event listener for event on change
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Reference to table
                TableLayout table = view.findViewById(R.id.ProductTable);
                // Loop through first branch of DB
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Date key
                    String key = snapshot.getKey();
                    // Loop through the second branch of DB
                    DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("Tracker").child(key);
                    mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Product
                                String key2 = snapshot.getKey();
                                // Loop through last branch of DB
                                DatabaseReference mPostReference = mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("Tracker").child(key).child(key2);
                                mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            // Calories

                                            // Add row here with all text views
                                            TableRow tr = new TableRow(getActivity());
                                            // Date
                                            TextView DateView = new TextView(getActivity());
                                            DateView.setText(key);
                                            DateView.setTextSize(12);
                                            DateView.setGravity(17);
                                            tr.addView(DateView);
                                            // Product
                                            TextView productView = new TextView(getActivity());
                                            productView.setText(key2);
                                            productView.setTextSize(12);
                                            productView.setGravity(17);
                                            tr.addView(productView);
                                            // Calories
                                            TextView CalorieView = new TextView(getActivity());
                                            CalorieView.setText(snapshot.getValue().toString());
                                            CalorieView.setTextSize(12);
                                            CalorieView.setGravity(17);
                                            tr.addView(CalorieView);
                                            // Add remove Image
                                            ImageView deleteV = new ImageView(getActivity());
                                            String value = String.valueOf(R.drawable.ic_baseline_remove_24);
                                            deleteV.setImageResource(Integer.parseInt(value));
                                            deleteV.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    mDatabase.child("UserAccounts").child(FirebaseAuth.getInstance().getUid()).child("Tracker").child(key).removeValue();
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        getParentFragmentManager().beginTransaction().detach(Fragment_Home.this).commitNow();
                                                        getParentFragmentManager().beginTransaction().attach(Fragment_Home.this).commitNow();
                                                    } else {
                                                        getParentFragmentManager().beginTransaction().detach(Fragment_Home.this).attach(Fragment_Home.this).commit();
                                                    }
                                                }
                                            });
                                            tr.addView(deleteV);
                                            // Add row to table
                                            table.addView(tr);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message
                                        Log.w("Firebase:", "loadPost:onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Log.w("Firebase:", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase:", "loadPost:onCancelled", databaseError.toException());
            }
        });

        return view;
    }

}