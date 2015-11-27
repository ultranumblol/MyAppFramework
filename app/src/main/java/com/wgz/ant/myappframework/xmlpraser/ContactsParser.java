package com.wgz.ant.myappframework.xmlpraser;

import com.wgz.ant.myappframework.bean.Contacts;

import java.io.InputStream;
import java.util.List;

/**
 * Created by qwerr on 2015/11/27.
 */
public interface ContactsParser {
    /**
     * 解析输入流 得到Contacts对象集合
     * @param is
     * @return
     * @throws Exception
     */
    public List<Contacts> parse(InputStream is) throws Exception;

    /**
     * 序列化Contacts对象集合 得到XML形式的字符串
     * @param contacts
     * @return
     * @throws Exception
     */
    public String serialize(List<Contacts> contacts) throws Exception;
}
