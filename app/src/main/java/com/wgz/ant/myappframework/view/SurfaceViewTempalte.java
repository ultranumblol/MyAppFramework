package com.wgz.ant.myappframework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by qwerr on 2016/1/21.
 */
public class SurfaceViewTempalte extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    /**
     * 用于绘制的线程
     */
    private Thread thread;
    /**
     * 线程控制开关
     */
    private boolean isRunning;

    public SurfaceViewTempalte(Context context) {
        this(context,null);
    }

    public SurfaceViewTempalte(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        //可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    draw();
                }

            }


        });
        thread.start();
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas!=null){
            //draw something

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (mCanvas!=null){
                mHolder.unlockCanvasAndPost(mCanvas);

            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;

    }
}
