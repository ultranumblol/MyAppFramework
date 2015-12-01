package com.wgz.ant.myappframework.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wgz.ant.myappframework.R;
import com.wgz.ant.myappframework.db.DatabaseHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/30.
 */
public class GroupManagerAdapter  extends BaseAdapter {
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;
    private Context context;

    DatabaseHelper dbh ;

    public GroupManagerAdapter(List<Map<String, Object>> list,Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;
        if (convertView == null){
            holder = new Viewholder();
            convertView = inflater.inflate(R.layout.recycler_item,null);
            holder.deleteIMG = (ImageView) convertView.findViewById(R.id.deleteGroup);
            holder.groupName = (TextView) convertView.findViewById(R.id.mangroup_name);
            holder.pid = (TextView) convertView.findViewById(R.id.gmpid);
            convertView.setTag(holder);
        }else {
            holder = (Viewholder) convertView.getTag();

        }
        Map<String,Object> map = list.get(position);
        holder.groupName.setText(map.get("gname").toString());
        holder.pid.setText(map.get("pid").toString());
        holder.deleteIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                new AlertDialog.Builder(context)
                        .setTitle("删除")

                        .setMessage("是否删除该分组？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(context, "删除成功" + position, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)


                        .show();

            }
        });


        return convertView;
    }
    private class Viewholder {
        private ImageView deleteIMG;
        private TextView groupName;
        private TextView pid;

    }
    //删除联系人操作
    private void deletecontent(String pid){
        SQLiteDatabase db = dbh.getWritableDatabase();
        String sql2 ="delete from content1 where pid =?";
        String sql3 ="delete from fenzu where pid =?";
        String[] bindArgs = new String[]{pid};
        db.execSQL(sql2, bindArgs);
        db.execSQL(sql3,bindArgs);
        sql2= null;
        sql3=null;
        db.close();

    }
}