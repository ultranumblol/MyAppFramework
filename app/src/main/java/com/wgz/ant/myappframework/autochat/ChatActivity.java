package com.wgz.ant.myappframework.autochat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wgz.ant.myappframework.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ListView mMsgs;
    private ChatMsgAdapter adapter;
    private List<ChatMsgBean> mDatas;
    private EditText inputmsg;
    private FloatingActionButton fab;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //等待子线程返回
            ChatMsgBean frommsg = (ChatMsgBean) msg.obj;
            mDatas.add(frommsg);
            adapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size()-1);

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("聊天");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initview();
        initdata();
        initListner();


    }

    private void initListner() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String toMsg = inputmsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)){
                    Toast.makeText(ChatActivity.this,"消息不能为空！",Toast.LENGTH_SHORT).show();
                    return;

                }
                ChatMsgBean toMessage = new ChatMsgBean();
                toMessage.setMsg(toMsg);
                toMessage.setDate(new Date());
                toMessage.setType(ChatMsgBean.Type.OUTCOMING);
                mDatas.add(toMessage);
                adapter.notifyDataSetChanged();
                inputmsg.setText("");

                new Thread(){
                    @Override
                    public void run() {
                       ChatMsgBean fromMsg = HttpUtils.sendMessage(toMsg);
                        Message message  = Message.obtain();
                        message.obj = fromMsg;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });
    }

    private void initdata() {
        mDatas = new ArrayList<ChatMsgBean>();
        mDatas.add(new ChatMsgBean(new Date(), ChatMsgBean.Type.INCOMING," hi!"));
        //mDatas.add(new ChatMsgBean(new Date(), ChatMsgBean.Type.OUTCOMING,"hihihihi!"));
        adapter = new ChatMsgAdapter(this,mDatas);
        mMsgs.setAdapter(adapter);

    }

    private void initview() {
        fab = (FloatingActionButton) findViewById(R.id.btn_send_msg);
        mMsgs = (ListView) findViewById(R.id.msg_listview);
        inputmsg = (EditText) findViewById(R.id.input_msg);

    }

}
