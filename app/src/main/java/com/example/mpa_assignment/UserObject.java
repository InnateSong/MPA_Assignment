package com.example.mpa_assignment;

public class UserObject {
    private String email, username;

    public UserObject() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObject(String email, String username){
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
