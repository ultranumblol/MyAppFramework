package com.wgz.ant.myappframework.face;

import android.graphics.Bitmap;
import android.util.Log;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by qwerr on 2015/12/16.
 */
public class FaceppDetect {

    public interface  CallBack{
        void sucess(JSONObject result);
        void error(FaceppParseException error);


    }

    public static void detect(final Bitmap bm, final CallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    HttpRequests requests = new HttpRequests(Constant.KEY,Constant.SECRET,true,true);
                    Bitmap bmSmall = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmSmall.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] arrays =  stream.toByteArray();
                    PostParameters parameters = new PostParameters();
                    parameters.setImg(arrays);
                    JSONObject jsonObject = requests.detectionDetect(parameters);
                    Log.i("xml",jsonObject.toString());

                    if (callback!=null){
                        callback.sucess(jsonObject);


                    }


                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    if (callback!=null){
                        callback.error(e);


                    }

                }

            }
        }).start();


    }


}
