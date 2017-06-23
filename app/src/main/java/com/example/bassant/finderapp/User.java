package com.example.bassant.finderapp;

/**
 * Created by Bassant on 5/5/2017.
 */

public class User {
    private String userName;
    private String password;
    private String email;
    private String mobile;
  public User()
    {}

   public User(String uN ,String pw ,String e ,String m )
   {
       userName = uN ;
       password = pw ;
       email = e ;
       mobile = m ;
   }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
