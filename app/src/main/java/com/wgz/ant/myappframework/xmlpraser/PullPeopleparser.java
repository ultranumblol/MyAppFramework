package com.wgz.ant.myappframework.xmlpraser;

import android.util.Xml;

import com.wgz.ant.myappframework.bean.People;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerr on 2015/11/27.
 */
public class PullPeopleparser implements PeopleParser {
    @Override
    public List<People> parse(InputStream is) throws Exception {
        List<People> snews = null;
        People people = null;
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
                    snews = new ArrayList<People>();  //初始化contacts集合
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (pullParser.getName().equals("Table")) {  //判断开始标签元素
                        people = new People();
                    }

                    else if (pullParser.getName().equals("id")) {
                        event=pullParser.next();//让解析器指向id属性的值
                        people.setId(Integer.parseInt(pullParser.getText()));

                    }
                    else if (pullParser.getName().equals("setctorid")) {
                        event=pullParser.next();//让解析器指向pid属性的值
                        people.setSid(Integer.parseInt(pullParser.getText()));
                    }
                    else if (pullParser.getName().equals("name")) {
                        event=pullParser.next();
                        people.setName(pullParser.getText());
                    }
                    else if (pullParser.getName().equals("tel")) {
                        event=pullParser.next();
                        people.setPhone(pullParser.getText());
                    }
                    else if (pullParser.getName().equals("ranke")) {
                        event=pullParser.next();
                        people.setRank(pullParser.getText());
                    }

                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (pullParser.getName().equals("Table")) {  // 判断结束标签元素
                        snews.add(people);
                        people = null;
                    }
                    break;

            }
            // 进入下一个元素并触发相应事件
            event = pullParser.next();
        }

        return snews;

    }

    @Override
    public String serialize(List<People> people) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
