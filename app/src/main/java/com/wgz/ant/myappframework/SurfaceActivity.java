package com.wgz.ant.myappframework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wgz.ant.myappframework.luckypan.LuckyPan;

public class SurfaceActivity extends AppCompatActivity {
    private LuckyPan mLuckyPan;
    private ImageView btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_face);

        setTitle("摇奖");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        mLuckyPan = (LuckyPan) findViewById(R.id.id_luckypan);
        btn = (ImageView) findViewById(R.id.id_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLuckyPan.isStart()){
                    mLuckyPan.luckStart(1);
                    btn.setImageResource(R.drawable.stop);
                }else {
                if (!mLuckyPan.isShouldEnd()){
                    mLuckyPan.luckend();
                    btn.setImageResource(R.drawable.start);

                }

                }

            }
        });


    }
}
