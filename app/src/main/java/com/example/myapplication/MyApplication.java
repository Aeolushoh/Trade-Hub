package com.example.myapplication;

import android.app.Application;

public class MyApplication extends Application {
    private int userid=0;
    private String name=null;

    private String myid=null;
    private String xueyuan=null;

    public int getSharedVariable() {
        return userid;
    }

    public void setSharedVariable(int value) {
        userid = value;
    }

    public String  getnameVariable() {
        return name;
    }

    public void setnameVariable(String  value) {
        name = value;
    }

    public String  getmyidVariable() {
        return myid;
    }

    public void setmyidVariable(String  value) {
        myid = value;
    }
    public String  getxueyuanVariable() {
        return xueyuan;
    }

    public void setxueyuanVariable(String  value) {
        xueyuan = value;
    }
}
