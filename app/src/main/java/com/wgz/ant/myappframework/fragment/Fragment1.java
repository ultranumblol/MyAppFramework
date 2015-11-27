package com.wgz.ant.myappframework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wgz.ant.myappframework.ContantActivity;
import com.wgz.ant.myappframework.R;
import com.wgz.ant.myappframework.adapter.SimpleTreeAdapter;
import com.wgz.ant.myappframework.adapter.TreeListViewAdapter;
import com.wgz.ant.myappframework.bean.FileBean;
import com.wgz.ant.myappframework.bean.Node;
import com.wgz.ant.myappframework.util.OnDataFinishedListener;
import com.wgz.ant.myappframework.util.PraserConXML;
import com.wgz.ant.myappframework.util.SpUtil;

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
    private TreeListViewAdapter mAdapter;
    private NestedScrollView reboundScrollView,reboundScrollView2;

    private ListView mTree,listView2;
    private List<Map<String, Object>> constest;
    SpUtil spUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,null);

        initview(view);
        return  view;
    }

    private void initview(View view) {
        reboundScrollView = (NestedScrollView) view.findViewById(R.id.zuzhi_sv);
        reboundScrollView2 = (NestedScrollView) view.findViewById(R.id.zuzhi_sv2);
        mTree = (ListView) view.findViewById(R.id.list_1_1);
        listView2 = (ListView) view.findViewById(R.id.list_1_2);
        ivDeleteText2 = (ImageView)view.findViewById(R.id.ivDeleteText2);
        etSearch2 = (EditText)view.findViewById(R.id.etSearch2);
        //搜索功能
        listView2.setAdapter(new SimpleAdapter(getActivity(),testData(),R.layout.list_contact_item,
                new String[]{"name", "phone"}, new int[]{R.id.name, R.id.number}));
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
                //search1();


            }
        });
        initDatas();
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
        //getmDataSub(peos2, input);//获取更新数据
    }
    private List<Map<String, Object>> testData(){
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0 ; i <20 ; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map . put("name", "ceshi"+i);
            map. put("phone", "qqqq" + i + i);
            data.add(map);


        }

        return data;

    }

}
