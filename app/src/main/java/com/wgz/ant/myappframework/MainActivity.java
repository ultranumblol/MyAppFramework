package com.wgz.ant.myappframework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wgz.ant.myappframework.adapter.FragmentAdapter;
import com.wgz.ant.myappframework.fragment.Fragment1;
import com.wgz.ant.myappframework.fragment.Fragment2;
import com.wgz.ant.myappframework.util.CheckNetWork;
import com.wgz.ant.myappframework.util.SpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewpager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private List<Map<String, Object>> peos;//联系人列表
    SpUtil spUtil;
    private CoordinatorLayout rootlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
        private void init(){
            rootlayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
           // tabLayout = (TabLayout) findViewById(R.id.tabs);
            List<String> titles = new ArrayList<>();
            titles.add("组织机构");
            titles.add("联系人");
            initAllQuery();

            mViewpager = (ViewPager) findViewById(R.id.viewpager_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("蚂蚁集团通讯录");
            setSupportActionBar(toolbar);
            fragment1 = new Fragment1();
            fragment2 = new Fragment2();

           // tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
           // tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));

            fragments = new ArrayList<>();
            fragments.add(fragment1);
            fragments.add(fragment2);

            FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);

            mViewpager.setAdapter(adapter);
           // tabLayout.setupWithViewPager(mViewpager);
           // tabLayout.setTabsFromPagerAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean net = false;
                    CheckNetWork cnet = new CheckNetWork();
                    net = cnet.checkNetWorkStatus(getApplicationContext());
                    if (net == false) {
                        Snackbar.make(rootlayout, "没有网络！", Snackbar.LENGTH_SHORT).setAction("同步", null).show();
                    } else {
                        Snackbar.make(rootlayout, "点击同步更新", Snackbar.LENGTH_LONG).setAction("同步", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File("/data/data/" + MainActivity.this.getPackageName().toString() + "/shared_prefs", "finals.xml");
                                if (file.exists()) {
                                    file.delete();
                                    Intent intent = MainActivity.this.getIntent();
                                    MainActivity.this.finish();
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "同步成功！", Toast.LENGTH_SHORT).show();
                                    // Snackbar.make(rootlayout,"同步成功！",Snackbar.LENGTH_SHORT).setAction("同步", null).show();
                                } else {
                                    Snackbar.make(rootlayout, "数据已经是最新！", Snackbar.LENGTH_SHORT).setAction("同步", null).show();

                                }
                            }
                        }).show();
                    }

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getHeaderCount();
            View view =  navigationView.getHeaderView(0);
            List<Map<String , Object>> list3 = new ArrayList<Map<String ,Object>>();
            list3 = getuserinfo(peos,getphonesp());
            Log.i("xml", "list3登陆用户信息====" + list3.toString());
            //Log.i("xml", navigationView.getHeaderCount() + "============12222222222222222222222222");
            TextView phone = (TextView)view.findViewById(R.id.head_phone);
            TextView rank = (TextView) view.findViewById(R.id.head_rank);
            TextView name = (TextView) view.findViewById(R.id.head_name);
            phone.setText(getphonesp());
            rank.setText(list3.get(0).get("ranke").toString());
            name.setText(list3.get(0).get("name").toString());
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void initAllQuery(){
        spUtil = new SpUtil();
        peos=spUtil.getInfo(MainActivity.this, "huancun" + 0);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(MainActivity.this, GroupManagerActivity.class), 0);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("title","公司简介");
            startActivity(intent);


        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("title","企业精神");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("title","关于本产品");
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("title","关于作者");

            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this,MessageActivity.class);
            intent.putExtra("title","意见反馈");
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Snackbar.make(rootlayout, "确认注销账号？", Snackbar.LENGTH_LONG)
                    .setAction("注销", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File file2= new File("/data/data/"+MainActivity.this.getPackageName().toString()+"/shared_prefs","userphone.xml");
                            if(file2.exists()){
                                SharedPreferences sp2 = getSharedPreferences("userphone", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sp2.edit();
                                editor2.clear().commit();
                                file2.delete();

                            }
                            File file= new File("/data/data/"+MainActivity.this.getPackageName().toString()+"/shared_prefs","autologin.xml");
                            if(file.exists()){
                                SharedPreferences sp = getSharedPreferences("autologin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear().commit();
                                file.delete();

                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                Toast.makeText(getApplicationContext(), "注销成功！", Toast.LENGTH_SHORT).show();

                                // Snackbar.make(rootlayout,"同步成功！",Snackbar.LENGTH_SHORT).setAction("同步", null).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"注销成功！",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }
    /**

     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");
        Fragment f = getSupportFragmentManager().findFragmentById(fragments.get(1).getId());
        f.onActivityResult(requestCode, resultCode, data);
    }
    private String getphonesp(){
        SharedPreferences preferences = getSharedPreferences("userphone", Context.MODE_PRIVATE);
        String userphone = preferences.getString("userphone", "--");
        return userphone;
    }
    //获得登陆用户信息
    private ArrayList<Map<String, Object>> getuserinfo(List<Map<String, Object>> constest,String phone){
        ArrayList<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
        data2.clear();
        int length = constest.size();
        for (int i = 0; i < length; i++){
            if (constest.get(i).get("phone").toString().equals(phone)){
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("name",peos.get(i).get("name").toString());
                item.put("phone", peos.get(i).get("phone").toString());
                item.put("ranke", peos.get(i).get("ranke").toString());
                data2.add(item);
            }

        }
        return data2;
    }

}
