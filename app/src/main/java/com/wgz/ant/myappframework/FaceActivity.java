package com.wgz.ant.myappframework;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facepp.error.FaceppParseException;
import com.wgz.ant.myappframework.face.FaceppDetect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_CODE = 0x110;
    private ImageView mPhoto;
    private View mWaiting;
   // private TextView mTip;
    private CoordinatorLayout root;
    private String mCurrentPhotoSrc;
    private FloatingActionButton mGetimg,mDetect;
    private Bitmap mPhotoImg;
    private Paint mpaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_face);
        setTitle("人脸识别-测测你几岁");
        setSupportActionBar(toolbar);
        initViews();
        initEvents();

    }

    private void initEvents() {
        mGetimg.setOnClickListener(this);
        mDetect.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PICK_CODE){

            if (data!=null){
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoSrc= cursor.getString(idx);
                cursor.close();
                
                //照片压缩
                resizePhoto();
                mPhoto.setImageBitmap(mPhotoImg);



            }

        }




        super.onActivityResult(requestCode, resultCode, data);



    }
    /*
    * 照片压缩
    *
    * */
    private void resizePhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(mCurrentPhotoSrc,options);
        double ratio = Math.max( options.outWidth*1.0d/1024f,options.outHeight*1.0d/1024f );
        options.inSampleSize = (int) Math.ceil(ratio);
        options.inJustDecodeBounds=false;
        mPhotoImg = BitmapFactory.decodeFile(mCurrentPhotoSrc,options);


    }

    private void initViews() {
        mpaint = new Paint();
            root = (CoordinatorLayout) findViewById(R.id.root_face);
        mGetimg = (FloatingActionButton) findViewById(R.id.fab_face1);
         mDetect = (FloatingActionButton) findViewById(R.id.fab_face2);
        mPhoto = (ImageView) findViewById(R.id.face_img);
        mWaiting = findViewById(R.id.waiting);
       // mTip = (TextView) findViewById(R.id.face_msg);
    }
    private static  final  int MSG_SUCESS=0x111;
    private static  final  int MSG_ERROR=0x112;

    private Handler mhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  MSG_SUCESS:
                    mWaiting.setVisibility(View.GONE);
                    JSONObject rs = (JSONObject) msg.obj;
                    prepareRsBitmap(rs);
                    mPhoto.setImageBitmap(mPhotoImg);
                    break;
                case  MSG_ERROR:
                    mWaiting.setVisibility(View.GONE);
                    String errormsg = (String) msg.obj;
                     if (TextUtils.isEmpty(errormsg)){
                         Snackbar.make(root,"error",Snackbar.LENGTH_SHORT).show();
                        // mTip.setText("error");

                     }else {
                         Snackbar.make(root,errormsg,Snackbar.LENGTH_SHORT).show();
                        // mTip.setText(errormsg);

                     }


                    break;


            }
        }
    };

    private void prepareRsBitmap(JSONObject rs) {
            Bitmap bitmap =Bitmap.createBitmap(mPhotoImg.getWidth(),mPhotoImg.getHeight(),mPhotoImg.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mPhotoImg,0,0,null);


        try {
            JSONArray faces =   rs.getJSONArray("face");
            int facecount = faces.length();
            for (int i = 0;i<facecount;i++){

                JSONObject face = faces.getJSONObject(i);
                JSONObject posObj = face.getJSONObject("position");
                float x= (float) posObj.getJSONObject("center").getDouble("x");
                float y= (float) posObj.getJSONObject("center").getDouble("y");
                float w = (float) posObj.getDouble("width");
                float h = (float) posObj.getDouble("height");
                x= x/100*bitmap.getWidth();
                y = y/100 * bitmap.getHeight();

                w = w/100*bitmap.getWidth();
                h = h/100*bitmap.getHeight();
                mpaint.setColor(0xffffffff);
                mpaint.setStrokeWidth(2);
                //画box
                canvas.drawLine(x-w/2,y-h/2,x-w/2,y+h/2,mpaint);
                canvas.drawLine(x-w/2,y-h/2,x+w/2,y-h/2,mpaint);
                canvas.drawLine(x+w/2,y-h/2,x+w/2,y+h/2,mpaint);
                canvas.drawLine(x-w/2,y+h/2,x+w/2,y+h/2,mpaint);
                //age sex
                int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
                String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");
                Bitmap agebitmap = buildAgeBitmap(age , "Male".equals(gender));
                int ageWidth = agebitmap.getWidth();
                int ageHeight = agebitmap.getHeight();
                if (bitmap.getWidth()<mPhoto.getWidth()&&bitmap.getHeight()<mPhoto.getHeight()){
                    float ratio  = Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(),bitmap.getHeight()*1.0f/mPhoto.getHeight());
                    agebitmap = Bitmap.createScaledBitmap(agebitmap,(int)(ageWidth*ratio),(int)(ageHeight*ratio),false);



                }
                canvas.drawBitmap(agebitmap,x-agebitmap.getWidth()/2,y-h/2-agebitmap.getHeight(),null);

                mPhotoImg = bitmap;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private Bitmap buildAgeBitmap(int age, boolean isMale) {

        TextView tv = (TextView) findViewById(R.id.age_ang_gender);
        tv.setText(age+"");
        if (isMale){
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male),
                    null,null,null);
        }else {
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female),
                    null,null,null);

        }
             tv.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());
        tv.destroyDrawingCache();
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_face1:
                //选择手机内的图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_CODE);
                break;
            case R.id.fab_face2:
                mWaiting.setVisibility(View.VISIBLE);

                if (mCurrentPhotoSrc!=null&&!mCurrentPhotoSrc.trim().equals("")){
                    resizePhoto();

                }
                else {
                    mPhotoImg = BitmapFactory.decodeResource(getResources(),R.drawable.wuyanzu1);


                }
                FaceppDetect.detect(mPhotoImg, new FaceppDetect.CallBack() {
                    @Override
                    public void sucess(JSONObject result) {
                    Message msg = Message.obtain();
                        msg.what= MSG_SUCESS;
                        msg.obj = result;
                        mhandler.sendMessage(msg);
                    }

                    @Override
                    public void error(FaceppParseException error) {
                        Message msg = Message.obtain();
                        msg.what= MSG_ERROR;
                        msg.obj = error.getErrorMessage();
                        mhandler.sendMessage(msg);
                    }
                });

                break;


        }
    }
}
