package com.wgz.ant.myappframework;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wgz.ant.myappframework.db.DatabaseHelper;
import com.wgz.ant.myappframework.util.OnDataFinishedListener;
import com.wgz.ant.myappframework.util.PraserPeoXML;
import com.wgz.ant.myappframework.util.SpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContantActivity extends AppCompatActivity {
    DatabaseHelper dbh ;
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
                setResult(RESULT_OK,intent);
                ContantActivity.this.finish();
            }
        });
        list1 = (ListView) findViewById(R.id.list_1);
    }
    //初始化数据库
    private void initdb() {
        dbh = new DatabaseHelper(this);
    }
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
