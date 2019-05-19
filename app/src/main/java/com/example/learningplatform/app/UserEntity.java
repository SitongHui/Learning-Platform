package com.example.learningplatform.app;

public class UserEntity {


    /**
     * status : success
     * data : {"id":1,"username":"xingshang","avatar":"/images/female.png","gender":"男 ","phone":"18149184677","password":"123456","department":"计算机学院"}
     */

    private String status;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * username : xingshang
         * avatar : /images/female.png
         * gender : 男
         * phone : 18149184677
         * password : 123456
         * department : 计算机学院
         */

        private int id;
        private String username;
        private String avatar;
        private String gender;
        private String phone;
        private String password;
        private String department;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }
}
