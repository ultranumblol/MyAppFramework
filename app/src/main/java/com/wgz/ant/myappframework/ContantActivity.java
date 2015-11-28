package com.wgz.ant.myappframework;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wgz.ant.myappframework.db.DatabaseHelper;
import com.wgz.ant.myappframework.util.OnDataFinishedListener;
import com.wgz.ant.myappframework.util.PraserPeoXML;
import com.wgz.ant.myappframework.util.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContantActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    private List<Map<String, Object>> peos;//联系人列表
    private List<Map<String, Object>> peos2;
    SimpleAdapter simpleAdapter;
    SpUtil spUtil;

    private ListView list1;//下方联系人列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contant);
        initdb();
        initview();
        initquery();

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

    private void initview() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", "该刷新了");
                //设置返回数据
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        list1 = (ListView) findViewById(R.id.list_1);
        //短按打电话监听
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                if (ActivityCompat.checkSelfPermission(ContantActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        cv.put("rank",rank);
        cv.put("date", date);
        db.insert("content1", null, cv);//插入操作
        db.close();

    }

    //初始化数据库
    private void initdb() {
        dbh = new DatabaseHelper(this);
    }

    //初始化查询方法
    private void initquery(){


        spUtil = new SpUtil();
        Intent intent=getIntent();
        final int sid = intent.getIntExtra("sid", 1);//接收上个acitvity传来的sid
        String title = intent.getStringExtra("title");
        setTitle(title);

        peos=spUtil.getInfo(getApplicationContext(), "huancun" + sid);
        peos2=spUtil.getInfo(getApplicationContext(), "huancun" + sid);
        if (peos.size()!=0){
            Log.i("xml", " 本地查询成功====peos数据：" + peos.toString());
            Log.i("xml", " 本地查询成功====peos数据：" + peos.size()+"tiao");
            simpleAdapter = new SimpleAdapter
                    (ContantActivity.this, peos, R.layout.list_contact_item,
                            new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
            list1.setAdapter(simpleAdapter);

        }else{
                //联网异步查询联系人
                PraserPeoXML qData2 = new PraserPeoXML(sid);
                qData2.execute();
                qData2.setOnDataFinishedListener(new OnDataFinishedListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onDataSuccessfully(Object data) {
                        //缓存数据，用sp保存
                        List<Map<String, Object>> huancunpeos1 = new ArrayList<Map<String, Object>>();
                        List<Map<String, Object>> huancunpeos2 = new ArrayList<Map<String, Object>>();
                        huancunpeos1 =(List<Map<String, Object>>) data;
                        huancunpeos2 =(List<Map<String, Object>>) data;
                        spUtil.saveInfo(getApplicationContext(), "huancun" + sid, huancunpeos1);
                        spUtil.saveInfo(getApplicationContext(), "huancun" + sid, huancunpeos2);


                        peos = (List<Map<String, Object>>) data;
                        peos2 = (List<Map<String, Object>>) data;
                        Log.i("xml", " 联网异步查询成功====peos数据：" + peos.toString());
                        simpleAdapter = new SimpleAdapter
                                (ContantActivity.this, peos, R.layout.list_contact_item,
                                        new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
                        list1.setAdapter(simpleAdapter);

                    }

                    @Override
                    public void onDataFailed() {
                        // TODO Auto-generated method stub

                    }
                });



        }

    }
}
