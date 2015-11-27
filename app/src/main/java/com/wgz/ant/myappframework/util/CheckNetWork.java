package com.wgz.ant.myappframework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by qwerr on 2015/11/27.
 */
public class CheckNetWork {


    public CheckNetWork() {

    }
    /**
     * 判断网络状态
     * @param context
     * @return
     */
    public static boolean checkNetWorkStatus(Context context){
        boolean result;
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if ( netinfo !=null && netinfo.isConnected() ) {
            result=true;
            Log.i("xml", "The net was connected");
        }else{
            result=false;
            Log.i("xml", "The net was bad!");
        }
        return result;
    }

}
