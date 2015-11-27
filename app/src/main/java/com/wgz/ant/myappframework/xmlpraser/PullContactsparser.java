package com.wgz.ant.myappframework.xmlpraser;

import android.util.Xml;

import com.wgz.ant.myappframework.bean.Contacts;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerr on 2015/11/27.
 */
public class PullContactsparser implements ContactsParser {
    @Override
    public List<Contacts> parse(InputStream is) throws Exception {
        List<Contacts> snews = null;
        Contacts contacts = null;
        // 由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser pullParser = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        pullParser.setInput(is, "UTF-8");
        // 产生第一个事件
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    snews = new ArrayList<Contacts>();  //初始化contacts集合
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (pullParser.getName().equals("Table")) {  //判断开始标签元素
                        contacts = new Contacts();
                    }

                    else if (pullParser.getName().equals("id")) {
                        event=pullParser.next();//让解析器指向id属性的值
                        contacts.setId(Integer.parseInt(pullParser.getText()));

                    }
                    else if (pullParser.getName().equals("pid")) {
                        event=pullParser.next();//让解析器指向pid属性的值
                        contacts.setPid(Integer.parseInt(pullParser.getText()));
                    }
                    else if (pullParser.getName().equals("name")) {
                        event=pullParser.next();
                        contacts.setName(pullParser.getText());
                    }
                    else if (pullParser.getName().equals("phone")) {
                        event=pullParser.next();
                        contacts.setPhone(pullParser.getText());
                    }

                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (pullParser.getName().equals("Table")) {  // 判断结束标签元素
                        snews.add(contacts);
                        contacts = null;
                    }
                    break;

            }
            // 进入下一个元素并触发相应事件
            event = pullParser.next();
        }

        return snews;

    }

    @Override
    public String serialize(List<Contacts> contacts) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
