package com.wgz.ant.myappframework.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by qwerr on 2015/11/27.
 */
public class SignMaker {
    /**
     * 获得加密的签名
     * @param name 传入的用户名，格式：username=xxx；
     * @param passwd 传入的密码，格式：password=xxx；
     * @return  返回md5加密的签名
     */
    public String getsign(String name,String passwd,String setctorid){
        MD5Util md5Util = new MD5Util();
        ArrayList<String> pass = new ArrayList<String>();
        if (setctorid==null) {
            pass.add(name);
            pass.add(passwd);
            //Log.i("xml", "11111111111111"+pass.toString());
        }if(setctorid!=null) {
            pass.add(name);
            pass.add(passwd);
            pass.add(setctorid);
            //Log.i("xml", "2222222222"+pass.toString());

        }
        Log.i("xml", "===========" + pass.toString());
        Collections.sort(pass);//对数组里的元素按首字母排序
        String result = "";
        String seprater = "&";
        if (pass.size()==2) {
            result=pass.get(0)+seprater+pass.get(1);
        }if(pass.size()==3) {
            result=pass.get(0)+seprater+pass.get(1)+seprater+pass.get(2);
        }
        String sign1=md5Util.MD5(result);
        Log.i("xml","加密内容："+result +"加密后"+sign1);
        return sign1;


    }
}
