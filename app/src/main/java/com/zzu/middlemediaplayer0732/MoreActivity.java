package com.zzu.middlemediaplayer0732;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView moreIvBack;
    private TextView bg;
    private RadioGroup rg;
    private TextView Version;
    private TextView Share;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        moreIvBack =  findViewById(R.id.more_iv_back);
        bg = findViewById(R.id.more_tv_exchangebg);
        rg = findViewById(R.id.rg);
       Version = findViewById(R.id.more_tv_version);
       Share = findViewById(R.id.more_tv_share);
        sharedPreferences = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bg.setOnClickListener(this);
        Share.setOnClickListener(this);
        moreIvBack.setOnClickListener(this);
        String versionName=getVersionName();
        Version.setText("当前版本： v"+versionName);
        setRGListener();
    }
    private void setRGListener(){
        //设置改变背景图片的单选按钮的监听
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取目前的默认地址
                int bg = sharedPreferences.getInt("bg",1);
                editor=sharedPreferences.edit();
                Intent intent=new Intent(MoreActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (checkedId) {
                    case R.id.more_rb_pink:
                        if(bg==0){
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",0);
                        editor.apply();
                        break;
                    case R.id.more_rb_green:
                        if(bg==1){
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",1);
                        editor.apply();
                        break;
                    case R.id.more_rb_blue:
                        if(bg==2){
                            Toast.makeText(MoreActivity.this,"您选择的为当前背景，无需改变",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",2);
                        editor.apply();
                        break;
                }
                startActivity(intent);
            }
        });
    }
    private String getVersionName() {
        //获取应用的版本名称
        PackageManager packageManager = getPackageManager();
        String versionName=null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
           versionName=packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_share:
                shareSoftMsg("迷你音乐app是一款超萌超可爱的音乐播放软件，简约风格让人欲罢不能");
                break;
            case R.id.more_tv_exchangebg:
                if(rg.getVisibility()==View.VISIBLE){
                    rg.setVisibility(View.GONE);
                }else{
                    rg.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    private void shareSoftMsg(String s) {
        //分享软件的函数
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"迷你音乐"));
    }
}
