package com.wgz.ant.myappframework.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.wgz.ant.myappframework.R;
import com.wgz.ant.myappframework.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/25.
 */
public class Fragment2 extends Fragment {
    private ExpandableListView expandableListView_one;
    SimpleExpandableListAdapter adapter;
    private TextView addgroup;
    DatabaseHelper dbh ;
    List<Map<String, String>> gruops;
    List<List<Map<String, String>>> childs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,null);
        initview(view);

        return  view;
    }

    private void initview(View view) {
        expandableListView_one = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListView_one.setGroupIndicator(null);//设置展开状况图标
        dbh = new DatabaseHelper(view.getContext());
        initdata();
    }

    /**
     * 刷新方法
     */
    private void flush(){
        gruops =queryGroup();
        childs = GroupContent(queryContent(), gruops.size());

        //Log.i("xml", "childs999999999999999" + childs.toString());
        adapter = new mysimp(getActivity().getApplicationContext(), gruops, R.layout.layout_parent, new String[]{"gname"}, new int[]{R.id.textGroup},
                childs, R.layout.layout_children,
                new String[]{"name", "phone", "pid","rank","date","id"},
                new int[]{R.id.textChild, R.id.textChild2, R.id.child_pid,R.id.child_rank,R.id.child_date,R.id.child_id}
        );
        expandableListView_one.setAdapter(adapter);
    }
    //simp适配器
    private class mysimp extends  SimpleExpandableListAdapter {
        public mysimp(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    //初始化数据
    private void initdata() {
        gruops = new ArrayList<Map<String, String>>();
        childs = new ArrayList<List<Map<String, String>>>();
        gruops =queryGroup();
        if(gruops.size()<1){
            insertGroup("常用联系人", "1");
            insertGroup("近期联系人", "2");
        }
        flush();


    }
    /**
     * 根据contactsActivity返回的“改刷新了”的信息，刷新列表
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String result = data.getExtras().getString("result");
        if(result.equals("该刷新了")){
            flush();
        }
    }
    /**
     * 插入分组数据
     * @param gname
     * @param pid
     */
    private void insertGroup(String gname,String pid){
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化一个cv 用来装数据
        cv.put("pid", pid);
        cv.put("gname", gname);
        db.insert("fenzu", null, cv);//插入操作
        db.close();

    }
    /**
     * 查询数据库数据，分组
     */
    private List<Map<String, String>> queryGroup(){

        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c =db.query("fenzu", null, null, null, null, null, null);
        List<Map<String,String>> data = new ArrayList<Map<String, String>>();

        if(c.moveToFirst()){//判断游标是否为空
            for(int i = 0;i<c.getCount();i++){
                c.moveToPosition(i);//移动到指定记录
                String id = c.getString(c.getColumnIndex("pid"));
                String gname = c.getString(c.getColumnIndex("gname"));
                Map<String, String> map = new HashMap<String, String>();
                map.put("pid", id);

                map.put("gname", gname);
                data.add(map);

            }
        }
        Log.i("xml", "data的长度：=================" + data.size());
        db.close();
        return data;
    };
    /**
     * 查询数据库数据，分组
     */
    private List<Map<String, String>> queryContent(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.query("content1", null, null, null, null, null, "id desc");

        List<Map<String,String>> data = new ArrayList<Map<String, String>>();
        if(c.moveToFirst()){//判断游标是否为空
            for(int i = 0;i<c.getCount();i++){
                c.moveToPosition(i);//移动到指定记录
                String pid = c.getString(c.getColumnIndex("pid"));
                String name = c.getString(c.getColumnIndex("name"));
                String phone = c.getString(c.getColumnIndex("phone"));
                String rank = c.getString(c.getColumnIndex("rank"));
                String date = c.getString(c.getColumnIndex("date"));
                String id = c.getString(c.getColumnIndex("id"));
                Map<String, String> map = new HashMap<String, String>();
                map.put("pid", pid);
                map.put("phone",phone);
                map.put("name", name);
                map.put("rank",rank);
                map.put("date",date);
                map.put("id",id);
                data.add(map);
            }
        }
        //Log.i("xml","diyicichaxun======"+data.size());
        Log.i("xml","diyicichaxun======"+data.toString());
        db.close();
        return data;
    };
    /**
     * 重组联系人数据，按分组类型分组
     */
    private List<List<Map<String, String>>> GroupContent(List<Map<String, String>> data,int count){
        List<List<Map<String,String>>>  childs2 = new ArrayList<List<Map<String, String>>>();
        for (int i=1;i<=count;i++){
            List<Map<String,String>> data2 = new ArrayList<Map<String, String>>();
            for (int j = 0 ;j<data.size();j++){
                int k = Integer.parseInt(data.get(j).get("pid").toString());
                if (k==i){
                    Map<String, String> map = new HashMap<String, String>();
                    String name = data.get(j).get("name").toString();
                    String phone = data.get(j).get("phone").toString();
                    String pid = data.get(j).get("pid").toString();
                    String rank = data.get(j).get("rank").toString();
                    String id = data.get(j).get("id").toString();
                    if (data.get(j).get("date")==null){
                        String date = " ";
                        map.put("date",date);
                    }
                    else{String date = data.get(j).get("date").toString();
                        map.put("date",date);}
                    map.put("phone",phone);
                    map.put("name", name);
                    map.put("pid", pid);
                    map.put("rank",rank);
                    map.put("id",id);

                    data2.add(map);
                }
            }
            childs2.add(data2);

        }

        return childs2;
    }

}
