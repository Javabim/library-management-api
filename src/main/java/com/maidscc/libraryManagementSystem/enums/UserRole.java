package com.maidscc.libraryManagementSystem.enums;

public enum UserRole {
    LIBRARIAN,
    PATRON;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}

