package com.wgz.ant.myappframework;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wgz.ant.myappframework.adapter.RecycleAdapter;
import com.wgz.ant.myappframework.db.DatabaseHelper;
import com.wgz.ant.myappframework.explosionfield.ExplosionField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupManagerActivity extends AppCompatActivity {

    DatabaseHelper dbh ;
    List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
    private TextView maddgroup;
    private RecyclerView grouplist;
    RecycleAdapter adapter;
    CoordinatorLayout gmrootview;
    //爆炸区域
     private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DatabaseHelper(getApplicationContext());
        setContentView(R.layout.activity_group_manager);
        setTitle("分组管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
         mExplosionField = ExplosionField.attach2Window(this);
        //addListener(findViewById(R.id.rootLayout));
    }

    @Override
    public void finish() {
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("result", "该刷新了");
        //设置返回数据
        setResult(RESULT_OK, intent);
        super.finish();
    }
    private void init() {
        gmrootview = (CoordinatorLayout) findViewById(R.id.gmana_rootview);
        maddgroup = (TextView) findViewById(R.id.addgroup);
        grouplist = (RecyclerView) findViewById(R.id.recycler_view);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", "该刷新了");
                //设置返回数据
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/
        flush();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }
    //刷新list
    private void flush(){
        data1 = new ArrayList<Map<String, Object>>();
        data1=queryGroup();
        Log.i("xml", "data10000000000000:" + data1.toString());
        grouplist.setLayoutManager(new LinearLayoutManager(GroupManagerActivity.this));
        adapter = new RecycleAdapter(data1,getApplicationContext());
        adapter.setOnItemClickLitener(new RecycleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //addListener(view);
                TextView pidtv = (TextView) view.findViewById(R.id.gmpid);
                TextView idtv = (TextView) view.findViewById(R.id.gmid);
                ImageView dele = (ImageView) view.findViewById(R.id.deleteGroup);
                //mExplosionField.explode(pidtv);
               //mExplosionField.explode(idtv);
                mExplosionField.explode(dele);
                final String pid = pidtv.getText().toString();
                final String gid = idtv.getText().toString();
                Snackbar.make(gmrootview, "是否删除该分组?", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pid.equals("1") || pid.equals("2")) {
                            //Toast.makeText(getApplicationContext(), "默认分组不能删除！", Toast.LENGTH_SHORT).show();
                            Snackbar.make(gmrootview, "默认分组不能删除!", Snackbar.LENGTH_SHORT).show();
                        } else {

                            deletecontent(pid, gid);// 删除操作
                            flush();
                        }
                    }
                }).show();
            }

        });
        grouplist.setAdapter(adapter);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings2) {
            final EditText inputServer = new EditText(GroupManagerActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupManagerActivity.this);
            builder.setTitle("请输入分组名称：").setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    int num2 = data1.size() + 1;
                    insertGroup(inputServer.getText().toString(), num2 + "");
                    flush();

                }
            });
            builder.show();

            return true;
        }
        if (id == android.R.id.home){
            finish();
            return true;

        }
       /* if (id == R.id.action_add) {
            Snackbar.make(rootlayout, "music！", Snackbar.LENGTH_LONG)
                    .setAction("Action", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();

            return true;
        }*/

        return super.onOptionsItemSelected(item);
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
    private List<Map<String, Object>> queryGroup(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c =db.query("fenzu", null, null, null, null, null, null);
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();

        if(c.moveToFirst()){//判断游标是否为空
            for(int i = 0;i<c.getCount();i++){
                c.moveToPosition(i);//移动到指定记录
                String gname = c.getString(c.getColumnIndex("gname"));
                String pid = c.getString(c.getColumnIndex("pid"));
                String gid = c.getString(c.getColumnIndex("id"));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("gname", gname);
                map.put("pid",pid);
                map.put("id",gid);
                data.add(map);

            }
        }
        db.close();
        return data;
    }
    /**
     * 删除联系人
     * @param pid
     */
    private void deletecontent(String pid,String id){
        SQLiteDatabase db = dbh.getWritableDatabase();
        String sql2 ="delete from content1 where pid =?";
        String[] bindArgs = new String[]{pid};
        String sql3 ="delete from fenzu where id =?";
        String[] bindArgs2 = new String[]{id};
        db.execSQL(sql3, bindArgs2);
        db.execSQL(sql2, bindArgs);
        sql2= null;
        sql3=null;
        db.close();

    }
}
