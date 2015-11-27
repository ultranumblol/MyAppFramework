package com.wgz.ant.myappframework.bean;

/**
 * Created by qwerr on 2015/11/27.
 */
public class People {
    private int id;//联系人id
    private int sid;//部门id
    private String name;//联系人姓名
    private String phone;//联系人电话
    private String rank;//级别
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    @Override
    public String toString() {
        return "联系人: [名称=" + name + ", 电话=" + phone + "]";
    }
}
