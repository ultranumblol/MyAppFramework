package com.wgz.ant.myappframework.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgz.ant.myappframework.R;
import com.wgz.ant.myappframework.adapter.RecycleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/25.
 */
public class Fragment4 extends Fragment {
    RecyclerView mrecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,null);

        initview(view);
        return  view;
    }

    private void initview(View view) {
        mrecyclerView = (RecyclerView) view.findViewById(R.id.recycler_frag1);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        mrecyclerView.setLayoutManager(layoutManager);

        mrecyclerView.setAdapter(new RecycleAdapter(data()));

    }

    private List<Map<String, Object>> data(){
        List<Map<String,Object>> da1 = new ArrayList<Map<String, Object>>();
        for (int i = 0 ; i<20;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("ceshi","ceshi"+i);
            da1.add(map);



        }

        return da1;
    }
}
