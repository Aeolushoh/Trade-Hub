package com.example.myapplication.entity;

public class Msg {
    private String msg;
    private String from;

    public Msg(String msg, String from, int id) {
        this.msg = msg;
        this.from = from;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private int id;
}
