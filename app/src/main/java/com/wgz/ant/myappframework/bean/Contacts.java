package com.wgz.ant.myappframework.bean;

/**
 * Created by qwerr on 2015/11/27.
 */
public class Contacts {
    private int id;//联系人id
    private int pid;//父id
    private String name;//联系人姓名
    private String phone;//联系人电话
    public int getId() {
        return id;
    }
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
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
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Override
    public String toString() {
        return "联系人: [名称=" + name + ", 电话=" + phone + "]";
    }
}
