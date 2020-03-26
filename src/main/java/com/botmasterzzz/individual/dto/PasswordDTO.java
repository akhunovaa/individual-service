package com.botmasterzzz.individual.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Size;

public class PasswordDTO {

    @JsonIgnore
    private long id;

    @Size(min = 3, max = 5000)
    private String password;

    @Size(min = 3, max = 5000)
    private String passwordVerifier;

    @Size(min = 3, max = 5000)
    private String passwordMain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerifier() {
        return passwordVerifier;
    }

    public void setPasswordVerifier(String passwordVerifier) {
        this.passwordVerifier = passwordVerifier;
    }

    public String getPasswordMain() {
        return passwordMain;
    }

    public void setPasswordMain(String passwordMain) {
        this.passwordMain = passwordMain;
    }
}
