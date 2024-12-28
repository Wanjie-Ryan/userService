package com.eclectics.bank.dto;

import com.eclectics.bank.domain.ConfirmationToken;

public class LoginResponse {
    private ConfirmationToken user;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(ConfirmationToken user, String token) {
        this.user = user;
        this.token = token;
    }

    public ConfirmationToken getUser() {
        return user;
    }

    public void setUser(ConfirmationToken user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}