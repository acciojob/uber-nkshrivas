package com.driver.controllers;


public class DriverDTO {
private String mobile;
private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DriverDTO(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
