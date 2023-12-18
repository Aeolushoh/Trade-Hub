package com.example.myapplication.entity;

public class User {

    private int id;
    private String name;
    private String myid;
    private String password;
    private String xueyuan;

    public User() {
    }

    public User(int id, String name, String myid, String password, String xueyuan) {
        this.id = id;
        this.name = name;
        this.myid = myid;
        this.password = password;
        this.xueyuan = xueyuan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getXueyuan() {
        return xueyuan;
    }

    public void setXueyuan(String xueyuan) {
        this.xueyuan = xueyuan;
    }
}