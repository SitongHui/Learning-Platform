package com.example.learningplatform.app;

public class LoginEntity {

    /**
     * userId : 4
     * message : 登陆成功
     */

    private int userId;
    private String message;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
