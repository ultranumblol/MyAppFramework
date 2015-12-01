package com.wgz.ant.myappframework.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wgz.ant.myappframework.ContantActivity;
import com.wgz.ant.myappframework.R;
import com.wgz.ant.myappframework.adapter.SimpleTreeAdapter;
import com.wgz.ant.myappframework.adapter.TreeListViewAdapter;
import com.wgz.ant.myappframework.bean.FileBean;
import com.wgz.ant.myappframework.bean.Node;
import com.wgz.ant.myappframework.db.DatabaseHelper;
import com.wgz.ant.myappframework.util.OnDataFinishedListener;
import com.wgz.ant.myappframework.util.PraserConXML;
import com.wgz.ant.myappframework.util.PraserPeoXML;
import com.wgz.ant.myappframework.util.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/25.
 */
public class Fragment1 extends Fragment {
    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private ImageView ivDeleteText2;
    private EditText etSearch2;
    DatabaseHelper dbh;
    private TreeListViewAdapter mAdapter;
    private NestedScrollView reboundScrollView,reboundScrollView2;
    private List<Map<String, Object>> peos;//联系人列表
    private List<Map<String, Object>> peos2;
    private ListView mTree,listView2;
    SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> constest;
    SpUtil spUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,null);

        initview(view);
        return  view;
    }

    private void initview(View view) {
        dbh = new DatabaseHelper(getActivity());
        reboundScrollView = (NestedScrollView) view.findViewById(R.id.zuzhi_sv);
        reboundScrollView2 = (NestedScrollView) view.findViewById(R.id.zuzhi_sv2);
        mTree = (ListView) view.findViewById(R.id.list_1_1);
        listView2 = (ListView) view.findViewById(R.id.list_1_2);
        ivDeleteText2 = (ImageView)view.findViewById(R.id.ivDeleteText2);
        etSearch2 = (EditText)view.findViewById(R.id.etSearch2);
        initAllQuery();
        //搜索功能
//        listView2.setAdapter(new SimpleAdapter(getActivity(),testData(),R.layout.list_contact_item,
//                new String[]{"name", "phone"}, new int[]{R.id.name, R.id.number}));
        //文字删除监听
        ivDeleteText2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch2.setText("");
            }
        });
        //搜索框输入文字监听
        etSearch2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText2.setVisibility(View.GONE);
                    reboundScrollView.setVisibility(View.VISIBLE);
                    reboundScrollView2.setVisibility(View.GONE);
                } else {
                    ivDeleteText2.setVisibility(View.VISIBLE);
                    reboundScrollView.setVisibility(View.GONE);
                    reboundScrollView2.setVisibility(View.VISIBLE);
                }
                search1();


            }
        });
        //短按打电话监听
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                final TextView name = (TextView) arg1.findViewById(R.id.name);
                final TextView phone = (TextView) arg1.findViewById(R.id.number);
                final TextView rank = (TextView) arg1.findViewById(R.id.rank);
                //添加数据到数据库最近联系人
                insert2(name.getText().toString(), phone.getText().toString(), 2 + "", rank.getText().toString());
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
                        .parse("tel:" + phone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(dialIntent);

            }
        });



        initDatas();
    }
    /**
     * 插入数据到最近联系人
     * @param name
     * @param phone
     */
    private void insert2(String name,String phone,String pid,String rank){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化一个cv 用来装数据
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("pid",pid);
        cv.put("rank", rank);
        cv.put("date", date);
        db.insert("content1", null, cv);//插入操作
        db.close();

    }

    private void initAllQuery(){
        spUtil = new SpUtil();
        peos=spUtil.getInfo(getActivity(), "huancun" + 0);
        peos2=spUtil.getInfo(getActivity(), "huancun" + 0);
        if (peos.size()>1){
            Log.i("xml", " 本地查询成功====peos数据：" + peos.toString());
            Log.i("xml", " 本地查询成功====peos数据：" + peos.size()+"条");
            simpleAdapter = new SimpleAdapter
                    (getActivity(), peos, R.layout.list_contact_item,
                            new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
            listView2.setAdapter(simpleAdapter);

        }else if (peos.size()<=1){
            PraserPeoXML qData2 = new PraserPeoXML(0);
            qData2.execute();
            qData2.setOnDataFinishedListener(new OnDataFinishedListener() {
                @Override
                public void onDataSuccessfully(Object data) {
                    //缓存数据，用sp保存
                    List<Map<String, Object>> huancunpeos1 = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> huancunpeos2 = new ArrayList<Map<String, Object>>();
                    huancunpeos1 =(List<Map<String, Object>>) data;
                    huancunpeos2 =(List<Map<String, Object>>) data;
                    spUtil.saveInfo(getActivity(), "huancun" + 0, huancunpeos1);
                    spUtil.saveInfo(getActivity(), "huancun" + 0, huancunpeos2);


                    peos = (List<Map<String, Object>>) data;
                    peos2 = (List<Map<String, Object>>) data;
                    Log.i("xml", " 联网异步查询成功====peos数据：" + peos.toString());
                    simpleAdapter = new SimpleAdapter
                            (getActivity(), peos, R.layout.list_contact_item,
                                    new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
                    listView2.setAdapter(simpleAdapter);
                }

                @Override
                public void onDataFailed() {

                }
            });

        }



    }

    //初始化数据
    private  void initDatas(){
         spUtil = new SpUtil();
        constest=spUtil.getInfo(getActivity().getApplicationContext(), "huancun");
        //根据sp中是否缓存了数据来选择是从本地加载还是联网
        if (constest.size()<2){
            //异步联网查询xml数据，组织机构数据
            Log.i("xml", " 异步联网查询xml数据，组织机构数据");
            PraserConXML qData = new PraserConXML();
            qData.execute();
            qData.setOnDataFinishedListener(new OnDataFinishedListener() {
                @Override
                public void onDataSuccessfully(Object data) {
                    //缓存数据，用sp保存
                    List<Map<String, Object>> huancundata = new ArrayList<Map<String, Object>>();
                    huancundata =(List<Map<String, Object>>) data;
                    spUtil.saveInfo(getActivity().getApplicationContext(), "huancun", huancundata);
                    //Log.i("xml", " fragment6=======");
                    //第一次加载 直接用查询结果显示界面
                    constest = (List<Map<String, Object>>) data;
                    // Log.i("xml", " fragment6=======constest" + constest.toString());
                    for (int i = 0; i < constest.size(); i++) {
                        int id = (Integer) constest.get(i).get("id");
                        int pid = (Integer) constest.get(i).get("pid");
                        String name = constest.get(i).get("name").toString();
                        String phone = constest.get(i).get("phone").toString();
                        mDatas.add(new FileBean(id, pid, name, phone));
                    }
                    // id , pid , name ,dianhua
				/*mDatas.add(new FileBean(1, 0, "name1","18811111111"));
				mDatas.add(new FileBean(2, 1, "name1","18822222222"));
				mDatas.add(new FileBean(3, 1, "name1","18833333333"));
				 */
                    Log.i("xml", " fragment6=======" + mDatas.size() + "tiao");
                    try {
                        mAdapter = new SimpleTreeAdapter<FileBean>(mTree, getActivity().getApplicationContext(), mDatas, 0);

                        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                            @Override
                            public void onClick(Node node, int position) {
                                if (node.isLeaf()) {
                                    String tielename = node.getName();

                                    int setctorid = node.getId();
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), ContantActivity.class);
                                    intent.putExtra("sid", setctorid);
                                    intent.putExtra("title", tielename);
                                    getActivity().startActivityForResult(intent, 0);
                                }
                            }

                        });

                        mTree.setAdapter(mAdapter);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onDataFailed() {

                }
            });


        }
        else {
            Log.i("xml", " fragment6=======缓存中的constest:"+constest.toString());
            for (int i = 0; i < constest.size(); i++) {
                int id = Integer.parseInt(constest.get(i).get("id").toString()) ;
                int pid =Integer.parseInt(constest.get(i).get("pid").toString()) ;
                String name = constest.get(i).get("name").toString();
                String phone = constest.get(i).get("phone").toString();
                mDatas.add(new FileBean(id, pid, name, phone));
            }
            // id , pid , name ,dianhua
				/*mDatas.add(new FileBean(1, 0, "name1","18811111111"));
				mDatas.add(new FileBean(2, 1, "name1","18822222222"));
				mDatas.add(new FileBean(3, 1, "name1","18833333333"));
				 */
            Log.i("xml", " fragment6=======缓存中的mdatas：" + mDatas.size() + "条");
            try {
                mAdapter = new SimpleTreeAdapter<FileBean>(mTree, getActivity().getApplicationContext(), mDatas, 0);

                mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position) {
                        if (node.isLeaf()) {
                            String tielename = node.getName();

                            int setctorid = node.getId();
                            Intent intent = new Intent(getActivity(), ContantActivity.class);
                           intent.putExtra("sid", setctorid);
                            intent.putExtra("title", tielename);
                           getActivity().startActivityForResult(intent, 0);

                        }
                    }

                });

                mTree.setAdapter(mAdapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }
    private void search1(){
        String input = etSearch2.getText().toString();
        //Log.i("xml","search方法： constest有："+constest.toString());
        getmDataSub(peos2, input);//获取更新数据
    }
    /**
     * 更新data数据
     * @param constest
     * @param data
     */
    private void getmDataSub(List<Map<String, Object>> constest,String data)
    {

        ArrayList<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
        data2.clear();
        int length = constest.size();
        //Log.i("xml","进去getmDataSub方法时peos2的长度为"+length);
        for(int i = 0; i < length; i++){

            if (constest.get(i).get("phone").toString().contains(data)||
                    constest.get(i).get("name").toString().contains(data)) {
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("name",peos2.get(i).get("name").toString());
                item.put("phone", peos2.get(i).get("phone").toString());
                item.put("ranke", peos2.get(i).get("ranke").toString());
                data2.add(item);
                //Log.i("xml","getmDataSub方法的循环后peos2：："+constest.get(i).get("phone").toString());
            }
        }
        //Log.i("xml","getmDataSub方法后peos"+constest.toString());
        //更新
        simpleAdapter =new SimpleAdapter(getActivity(), data2, R.layout.list_contact_item,
                new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
        listView2.setAdapter(simpleAdapter);

    }

}
