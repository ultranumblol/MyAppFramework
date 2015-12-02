package com.wgz.ant.myappframework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wgz.ant.myappframework.R;

import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/12/2.
 */
public class MyListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> data;
    private LayoutInflater mInflater;

    public MyListAdapter(List<Map<String, Object>> data,Context context) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_contact_item,null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone = (TextView) convertView.findViewById(R.id.number);
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();


        }
        Map<String,Object> map = data.get(position);
        holder.name.setText(map.get("name").toString());
        holder.phone.setText(map.get("phone").toString());
        holder.rank.setText(map.get("ranke").toString());


        return convertView;
    }
    public class ViewHolder {
        TextView name;
        TextView phone;
        TextView rank;


    }

}
