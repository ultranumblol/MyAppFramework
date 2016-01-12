package com.wgz.ant.myappframework.autochat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by qwerr on 2016/1/11.
 */
public class HttpUtils {
    private static  final String URL = "http://www.tuling123.com/openapi/api";
    private static  final String KEY = "25801582b5e3574793c899e636ecd802";
    public  static ChatMsgBean sendMessage(String msg){
        ChatMsgBean chatMsgBean = new ChatMsgBean();
        String jsonRes = doGet(msg);
        Gson gson = new Gson();
        try {
            Result result = gson.fromJson(jsonRes, Result.class);
            chatMsgBean.setMsg(result.getText());
        } catch (JsonSyntaxException e) {

           chatMsgBean.setMsg("error!");
        }
        chatMsgBean.setDate(new Date());
        chatMsgBean.setType(ChatMsgBean.Type.INCOMING);


        return chatMsgBean;
    }
    private static String doGet(String msg){
        String result = "";

        String path = setparams(msg);
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            java.net.URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5*1000);
            con.setReadTimeout(5*1000);
            is = con.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            bos = new ByteArrayOutputStream();

            while ((len=is.read(buf))!=-1){
                bos.write(buf,0,len);
            }
            bos.flush();
            result = new String(bos.toByteArray());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return result;
    }

    private static String setparams(String msg) {
        String url = URL+ "?key="+KEY+"&info="+ msg;
        return url;
    }
}
