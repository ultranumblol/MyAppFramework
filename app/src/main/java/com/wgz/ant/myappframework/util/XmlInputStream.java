package com.wgz.ant.myappframework.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 通过url连接服务器 将返回的xml文件变成流
 * Created by qwerr on 2015/11/27.
 */
public class XmlInputStream {
    public InputStream getStream(String username,String password,String setctorid,String sign){

        //String path = "http://hr.chinaant.com/xmlhandler.aspx?username=123&password=123&sign=5B7D1F8CEFBF1C80AEB73329C8A378A1";
        String path ="";
        String path2 = "http://hr.chinaant.com/xmlhandler.aspx?"+username+"&"+password+"&sign="+sign;

        String path3 = "http://hr.chinaant.com/xmlhandler.aspx?"+username+"&"+setctorid+"&"+password+"&sign="+sign;
        if (setctorid==null) {
            path=path2;
        }if (setctorid!=null){
            path=path3;
        }


        Log.i("xml2", "XML==path：" + path);
        URL myURL = null;
        try {
            myURL = new URL(
                    path);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)myURL.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        conn.setReadTimeout(5*1000);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        InputStream inStream = null;
        try {
            inStream = conn.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inStream;

    }

}
