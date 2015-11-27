package com.wgz.ant.myappframework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgz.ant.myappframework.R;

import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/25.
 */
public class RecycleAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Map<String,Object>> data;

    public RecycleAdapter(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewholder viewholder = (MyViewholder)holder;
            viewholder.textView1.setText(data.get(position).get("ceshi").toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public  class MyViewholder extends RecyclerView.ViewHolder{
        TextView textView1;

        public MyViewholder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.recycler_tv1);
        }
    }

}
