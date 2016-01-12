package com.wgz.ant.myappframework.autochat;

import java.util.Date;

/**
 * Created by qwerr on 2016/1/11.
 */
public class ChatMsgBean {
    private String name;
    private Date date;
    private Type type;
    private String msg;

    public enum Type{
        INCOMING,OUTCOMING;

    }

    public ChatMsgBean() {
    }

    public ChatMsgBean(Date date, Type type, String msg) {
        this.date = date;
        this.type = type;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
