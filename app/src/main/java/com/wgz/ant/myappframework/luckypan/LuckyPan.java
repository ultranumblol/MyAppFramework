package com.wgz.ant.myappframework.luckypan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wgz.ant.myappframework.R;

/**
 * Created by qwerr on 2016/1/21.
 */
public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback {
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

    /**
     * 盘快的奖项
     */
    private String[] mStrs = new String[]{"单反"," ipad"," 谢谢参与","iphone ","工服"," 谢谢参与"};
    /**
     * 奖项的图片
     */
    private int[] mImgs = new int[]{R.drawable.danfan,R.drawable.ipad,R.drawable.f040,R.drawable.iphone,R.drawable.meizi,R.drawable.f040};
    /**
     * 盘块的颜色
     */
    private int[] mColor = new int[]{0xFFFFC30,0xFFF17E01,0xFFFFC30,0xFFF17E01,0xFFFFC30,0xFFF17E01};
    /**
     * 盘块数
     */
    private int mItemCount = 6;
    /**
     * 与图片对应的bitmap数组
     */
    private Bitmap[] mImgsBitmap;
    /**
     * 整个盘块的范围
     */
    private RectF mRange = new RectF();
    /**
     * 盘块的直径
     */
    private int mRadius;
    /**
     * 绘制盘块的笔触
     */
    private Paint mArcPaint;
    /**
     * 绘制文字的笔触
     */
    private Paint mTextPaint;
    /**
     * 滚动的速度
     */
    private double mSpeed = 0;

    private volatile float mStartAngle = 0;
    /**
     * 判断是否点击了停止按钮
     */
    private boolean isShouldEnd;
    /**
     * 转盘的中心位置
     */
    private int mCenter;

    /**
     * padding 直接以paddingleft为主
     */
    private int mPadding;

    private  Bitmap mBitmapbg = BitmapFactory.decodeResource(getResources(),R.drawable.bg2);

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics());
    public LuckyPan(Context context) {
        this(context,null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         int width = Math.min(getMeasuredHeight(),getMeasuredWidth());

        mPadding = getPaddingLeft();
        //径
        mRadius = width - mPadding*2;
        //中心点
        mCenter = width/2;

        setMeasuredDimension(width,width);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        //初始化画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        //初始化盘块绘制的范围
        mRange = new RectF(mPadding,mPadding,mPadding+mRadius,mPadding+mRadius);

        //初始化图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0 ; i<mItemCount;i++){
             mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),mImgs[i]);
        }





        isRunning = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){

                    long start  = System.currentTimeMillis();
                    draw();
                    long end = System.currentTimeMillis();
                    if (end-start<50){
                        try {
                            Thread.sleep(50-(end-start));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }


        });
        thread.start();
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas!=null){
                //绘制背景
                drawbg();
                //绘制盘块
                float tmpAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;
                for (int i = 0 ; i<mItemCount; i++){

                    mArcPaint.setColor(mColor[i] );
                    //绘制盘块
                    mCanvas.drawArc(mRange,tmpAngle,sweepAngle,true,mArcPaint);
                    //绘制文本
                    drawText(tmpAngle,sweepAngle,mStrs[i]);

                    drawIcon(tmpAngle,mImgsBitmap[i]);
                    tmpAngle += sweepAngle;
                }
             mStartAngle +=mSpeed;

                //如果点击了停止
                if (isShouldEnd){
                    mSpeed-=1;
                }
                if (mSpeed<=0){
                    mSpeed = 0;
                    isShouldEnd = false;
                }


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
            public  void  luckStart(int index){
                //计算每一项的角度
                float angle = 360/mItemCount;

                //计算每一项中奖范围

                float from = 270 - (index+1)*angle;
                float end = from+angle;


                //设置停下来需要旋转的距离
                float targetFrom = 4*360 + from;
                float targetEnd = 4*360 + end;

                float v1 = (float) ((-1+ Math.sqrt(1+8*targetFrom))/2);
                float v2 = (float) ((-1+ Math.sqrt(1+8*targetEnd))/2);

                mSpeed = v1 +Math.random()*(v2-v1);


                //mSpeed = 50;
                isShouldEnd = false;


            }
    public  void luckend(){

        mStartAngle = 0;
        isShouldEnd = true;

    }

    /**\
     * 转盘是否再转
     * @return
     */
    public boolean isStart(){

        return  mSpeed!=0;
    }
    /**\
     * 是否点了停止
     * @return
     */
    public boolean isShouldEnd(){

        return  isShouldEnd;
    }


    /**
     * 绘制图标
     * @param tmpAngle
     * @param bitmap
     */
    private void drawIcon(float tmpAngle, Bitmap bitmap) {
        //设置图片的宽度为直径1/8
        int imgWidth = mRadius/8;

        float angle = (float) ((tmpAngle + 360/mItemCount/2)*Math.PI/180);

        int x = mCenter + (int) (mRadius/2/2*Math.cos(angle));
        int y = mCenter + (int) (mRadius/2/2*Math.sin(angle));

        //确定图片位置
        Rect rect = new Rect(x-imgWidth/2,y-imgWidth/2,x+imgWidth/2,y+imgWidth/2);

        mCanvas.drawBitmap(bitmap,null,rect,null);




    }

    /**
     * 绘制每个盘块的文本
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private void drawText(float tmpAngle, float sweepAngle, String mStr) {
        Path  path = new Path();
        path.addArc(mRange,tmpAngle,sweepAngle);

        //利用水平偏移让文字居中
        float textwidth= mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius*Math.PI/mItemCount/2-textwidth/2);
        int vOffset = mRadius/2/6;
        mCanvas.drawTextOnPath(mStr,path,hOffset,vOffset,mTextPaint);


    }

    /**
     * 绘制背景
     */
    private void drawbg() {
        mCanvas.drawColor(0xffffffff);
        mCanvas.drawBitmap(mBitmapbg,null,new Rect(mPadding/2,mPadding/2,getMeasuredWidth()-mPadding/2,getMeasuredHeight()-mPadding/2),null);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;

    }
}
