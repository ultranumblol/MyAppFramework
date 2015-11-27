package com.wgz.ant.myappframework.util;

import android.os.AsyncTask;

import com.wgz.ant.myappframework.bean.Contacts;
import com.wgz.ant.myappframework.xmlpraser.ContactsParser;
import com.wgz.ant.myappframework.xmlpraser.PullContactsparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/27.
 */
public class PraserConXML  extends AsyncTask<android.R.integer, android.R.integer, List<Map<String, Object>>> {
    private List<Contacts> mcontacts;
    private List<Map<String, Object>> cons;
    OnDataFinishedListener onDataFinishedListener;
    @Override
    protected List<Map<String, Object>> doInBackground(android.R.integer... params) {
        cons = new ArrayList<Map<String,Object>>();
        SignMaker sm = new SignMaker();
        String sign =sm.getsign("username=123","password=123", null);
        XmlInputStream xmlInputStream = new XmlInputStream();
        //InputStream is = getActivity().getAssets().open("ant.xml");
        InputStream is= xmlInputStream.getStream("username=123","password=123", null, sign);
        ContactsParser cParser = new PullContactsparser();
        try {
            mcontacts=cParser.parse(is);
            for(Contacts contacts:mcontacts){
                Map<String, Object> map = new HashMap<String, Object>();
                if (contacts.getPhone()==null) {
                    map.put("phone", "---");
                }
                if (contacts.getPhone()!=null) {
                    map.put("phone", contacts.getPhone());
                }
                map.put("id", contacts.getId());
                map.put("pid", contacts.getPid());
                map.put("name", contacts.getName());
                cons.add(map);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return cons;
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
