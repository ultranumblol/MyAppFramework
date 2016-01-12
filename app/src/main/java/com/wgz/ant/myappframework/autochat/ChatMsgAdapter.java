package com.wgz.ant.myappframework.autochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wgz.ant.myappframework.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by qwerr on 2016/1/11.
 */
public class ChatMsgAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ChatMsgBean> data;

    public ChatMsgAdapter(Context context, List<ChatMsgBean> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
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
    public int getItemViewType(int position) {
        ChatMsgBean chatMsgBean = data.get(position);
        if (chatMsgBean.getType()== ChatMsgBean.Type.INCOMING){
            return 0;
        }else {
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ChatMsgBean chatMsgBean = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView==null){
            if (getItemViewType(position)==0){
                convertView = inflater.inflate(R.layout.item_from_msg,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.id_frommsg_date);
                viewHolder.msg = (TextView) convertView.findViewById(R.id.id_frommsg_msg);

            }else {
                convertView = inflater.inflate(R.layout.item_to_msg,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.date = (TextView) convertView.findViewById(R.id.id_tomsg_date);
                viewHolder.msg = (TextView) convertView.findViewById(R.id.id_tomsg_msg);

            }
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
viewHolder.msg.setText(chatMsgBean.getMsg());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.date.setText(df.format(chatMsgBean.getDate()));

        return convertView;
    }

    private final class  ViewHolder{
        TextView date;
        TextView msg;

    }
}
