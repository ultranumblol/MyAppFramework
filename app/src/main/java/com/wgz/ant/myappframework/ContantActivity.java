package com.wgz.ant.myappframework;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wgz.ant.myappframework.db.DatabaseHelper;
import com.wgz.ant.myappframework.util.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContantActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    private List<Map<String, Object>> peos;//联系人列表
    //private List<Map<String, Object>> peos2;
    SimpleAdapter simpleAdapter;
    SpUtil spUtil;
    CoordinatorLayout mcdlayout;
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
        mcdlayout = (CoordinatorLayout) findViewById(R.id.contant_rootview);
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
        //长按dialog监听
        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                final TextView name = (TextView) arg1.findViewById(R.id.name);
                final TextView phone = (TextView) arg1.findViewById(R.id.number);
                final TextView rank = (TextView) arg1.findViewById(R.id.rank);
                testDialog(name.getText().toString(), phone.getText().toString(), rank.getText().toString());
                return true;
            }
        });

    }
    /**
     * 添加单个联系人到手机通讯录
     * @param name
     * @param phoneNum
     */
    public void testInsert(String name,String phoneNum){
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = this.getApplicationContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        //往data表插入姓名数据
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        this.getApplicationContext().getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);

        //往data表插入电话数据
        values.clear();
        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNum);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getApplicationContext().getContentResolver().insert(
                android.provider.ContactsContract.Data.CONTENT_URI, values);

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
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("gname", gname);
                map.put("pid",pid);
                data.add(map);

            }
        }
        db.close();
        return data;
    }
    /**
     * 插入数据到常用联系人
     * @param name
     * @param phone
     */
    private void insert(String name,String phone,String pid,String rank){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        date = null;
        Log.i("xml","当前时间=============："+date);

        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化一个cv 用来装数据
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("pid", pid);
        cv.put("rank",rank);
        cv.put("date",date);
        db.insert("content1", null, cv);//插入操作
        db.close();


    }
    //弹窗，里面是一个listview
    private void testDialog(final String dataname, final String dataphone, final String datarank){
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        data=queryGroup();
        LinearLayout linearLayoutMain = new LinearLayout(this);//自定义一个布局文件
        linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ListView listView = new ListView(this);
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        //this为获取当前的上下文
        listView.setFadingEdgeLength(0);

        List<Map<String, String>> nameList = new ArrayList<Map<String, String>>();
        //建立一个数组存储listview上显示的数据
        Map<String, String> defMap1 = new HashMap<String, String>();
        defMap1.put("name","添加到手机通讯录");
        defMap1.put("pid","-1");
        nameList.add(defMap1);
        Map<String, String> defMap2 = new HashMap<String, String>();
        defMap2.put("name", "添加到常用联系人");
        defMap2.put("pid","-2");
        nameList.add(defMap2);
        Map<String, String> defMap3 = new HashMap<String, String>();
        defMap3.put("name","发送短信");
        defMap3.put("pid","-3");
        nameList.add(defMap3);

        for (int m = 2; m < data.size(); m++) {//initData为一个list类型的数据源
            Map<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("name", "添加联系人到"+data.get(m).get("gname").toString()+"分组");
            nameMap.put("pid", data.get(m).get("pid").toString());
            nameList.add(nameMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(ContantActivity.this,
                nameList, R.layout.layout_parent3,
                new String[] { "name","pid" },
                new int[] { R.id.dialog_gname,R.id.dialog_pid });
        listView.setAdapter(adapter);

        linearLayoutMain.addView(listView);//往这个布局中加入listview

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("请选择").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .create();
        dialog.setCanceledOnTouchOutside(true);//使除了dialog以外的地方不能被点击
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                TextView name = (TextView) arg1
                        .findViewById(R.id.dialog_gname);//取得每条item中的textview控件
                TextView pid = (TextView) arg1
                        .findViewById(R.id.dialog_pid);//取得每条item中的textview控件

                if (name.getText().toString().equals("添加到手机通讯录")) {
                    testInsert(dataname, dataphone);
                    //Toast.makeText(ContantActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(mcdlayout,"添加成功！",Snackbar.LENGTH_SHORT).show();
                }

                if (name.getText().toString().equals("添加到常用联系人")) {
                    //添加数据到数据库常用联系人
                    insert(dataname, dataphone, 1 + "", datarank);
                    //Toast.makeText(ContantActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(mcdlayout, "添加成功！", Snackbar.LENGTH_SHORT).show();
                }

                if (name.getText().toString().equals("发送短信")) {
                    String num = dataphone;
                    Uri smsToUri = Uri.parse("smsto:" + num);

                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

                    intent.putExtra("sms_body", "");

                    startActivity(intent);
                }
                if (name.getText().toString().contains("分组")){
                    //Log.i("xml",name.getText().toString()+"=="+pid.getText().toString());
                    //Log.i("xml",dataname+"=="+dataphone+"=="+pid.getText().toString()+"=="+datarank);
                    insert(dataname, dataphone, pid.getText().toString()+"", datarank);
                    //Toast.makeText(ContantActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(mcdlayout, "添加成功！", Snackbar.LENGTH_SHORT).show();
                }


                dialog.cancel();
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    //初始化数据库
    private void initdb() {
        dbh = new DatabaseHelper(this);
    }
    /**
     * 更新data数据
     * @param constest
     * @param sid
     */
    private ArrayList<Map<String, Object>> getmDataSub(List<Map<String, Object>> constest, String sid)
    {

        ArrayList<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
        data2.clear();
        int length = constest.size();

        for(int i = 0; i < length; i++){

            if (constest.get(i).get("sid").toString().equals(sid)) {
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("name",constest.get(i).get("name").toString());
                item.put("phone", constest.get(i).get("phone").toString());
                item.put("ranke", constest.get(i).get("ranke").toString());
                data2.add(item);
            }
        }
        //Log.i("xml","getmDataSub方法后peos"+constest.toString());
        //更新
        return data2;
    }

    //初始化查询方法
    private void initquery(){


        spUtil = new SpUtil();
        Intent intent=getIntent();
        final int sid = intent.getIntExtra("sid", 1);//接收上个acitvity传来的sid
        String title = intent.getStringExtra("title");
        setTitle(title);

        peos=spUtil.getInfo(getApplicationContext(), "huancun" + 0);
        //peos2=spUtil.getInfo(getApplicationContext(), "huancun" + 0);
        if (peos.size()>1){


            List<Map<String , Object>> list3 = new ArrayList<Map<String ,Object>>();
            list3 = getmDataSub(peos,sid+"");
            //Log.i("xml", " 本地查询成功====peos数据：" + peos.toString());
            Log.i("xml", " 本地查询成功====peos数据：" + peos.size()+"条");
            simpleAdapter = new SimpleAdapter
                    (ContantActivity.this, list3, R.layout.list_contact_item,
                            new String[]{"name", "phone", "ranke"}, new int[]{R.id.name, R.id.number, R.id.rank});
            list1.setAdapter(simpleAdapter);

        }/*else if (peos.size()<=1){
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



        }*/

    }
}
