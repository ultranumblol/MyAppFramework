package com.wgz.ant.myappframework.util;

import android.os.AsyncTask;

import com.wgz.ant.myappframework.bean.People;
import com.wgz.ant.myappframework.xmlpraser.PeopleParser;
import com.wgz.ant.myappframework.xmlpraser.PullPeopleparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 解析联系人XML流
 * Created by qwerr on 2015/11/27.
 */
public class PraserPeoXML extends AsyncTask<android.R.integer, android.R.integer, List<Map<String, Object>>> {
    private List<People> mpeoples;
    private List<Map<String, Object>> peos;
    OnDataFinishedListener onDataFinishedListener;
    private int sid;

    public PraserPeoXML(int sid) {
        super();
        this.sid = sid;
    }
    @Override
    protected List<Map<String, Object>> doInBackground(android.R.integer... params) {
        peos = new ArrayList<Map<String,Object>>();
        SignMaker sm = new SignMaker();
        String setctorid = "setctorid="+sid;
        String sign =sm.getsign("username=123","password=123", setctorid);

        XmlInputStream xmlInputStream = new XmlInputStream();

        //获取xml文件流
        InputStream is= xmlInputStream.getStream("username=123","password=123", setctorid, sign);
        PeopleParser pParser = new PullPeopleparser();
        try {
            mpeoples=pParser.parse(is);
            for(People peoples:mpeoples){

                Map<String, Object> map = new HashMap<String, Object>();
                if (peoples.getPhone()==null) {
                    map.put("phone", "---");
                }
                if (peoples.getPhone()!=null) {
                    map.put("phone", peoples.getPhone());
                }
                map.put("id", peoples.getId());
                map.put("sid", peoples.getSid());
                map.put("name", peoples.getName());
                map.put("ranke",peoples.getRank());
                peos.add(map);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return peos;
    }
    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }

    @Override
    protected void onPostExecute(List<Map<String, Object>> result) {
        if(result!=null){
            onDataFinishedListener.onDataSuccessfully(result);
        }else{
            onDataFinishedListener.onDataFailed();
        }

    }

}