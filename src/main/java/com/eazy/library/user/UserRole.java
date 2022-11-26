package com.eazy.library.user;

public enum UserRole {
    User, Admin;


    public String getRole() {
        return this.name();
    }
}
