package com.wgz.ant.myappframework.util;

/**
 * 异步查询返回数据接口
 * Created by qwerr on 2015/11/27.
 */
public interface    OnDataFinishedListener {
    public void onDataSuccessfully(Object data);

    public void onDataFailed();
}
