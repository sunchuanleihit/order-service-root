package com.loukou.order.service.resp.dto;

import java.io.Serializable;

public class UserRespDto implements Serializable {

	private static final long serialVersionUID = 4674111604507250133L;

    private int id;

    private String userName;
    private String password;
    private String sex;
    private int age;
    private String description;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public void setPassword(String passWord) {
        this.password = passWord;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
