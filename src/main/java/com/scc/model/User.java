package com.scc.model;

public record User(
        int userId,
        String username,
        String password,
        String role,
        String accountStatus
) {
    public User(String username, String password, String role, String accountStatus) {
        this(0, username, password, role, accountStatus);
    }
}