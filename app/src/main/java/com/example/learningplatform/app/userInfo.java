package com.example.learningplatform.app;

public class userInfo {
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String department;

    public userInfo(){

    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
}


