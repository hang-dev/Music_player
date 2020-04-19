package com.zzu.middlemediaplayer0732;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView nextIv, playIv, lastIv;
    private TextView singerTv, songTv,currenttime,maxtime;
    private RecyclerView musicRv;
    private SharedPreferences preferences;
    private RelativeLayout layout;
    private ImageView back;
    private VolumeShaper volumeShaper;
    MediaPlayer mediaPlayer;
    //数据源
    List<LocalMusicBean> mDatas;
    //创建适配器
    private LocalMusicAdapter adapter;
    int selectedPosition=-1;
    int bgNum;
    int currentPlayPosition;
    int duration;
    private SeekBar seekBar;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
           seekBar.setProgress(msg.what);
           currenttime.setText(formate(msg.what));
           }
    };
//讲音乐时间转化为xx:xx形式
    private String formate(int what) {
        Date date = new Date(what);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(date);
        return time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        exchangeBg();
        mediaPlayer = new MediaPlayer();
        mDatas = new ArrayList<>();
        //设置布局管理器
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        musicRv.setLayoutManager(linearLayout);
        adapter = new LocalMusicAdapter(this, mDatas, new LocalMusicAdapter.OnItemClickLister() {
            @Override
            public void onClick(int pos) {
                //选择的音乐序号
                selectedPosition=pos;
                //创建一个线程，定时读取当前播放进程
                Scheduled();
                playMusicBean(pos);

            }
        });
        musicRv.setAdapter(adapter);
//动态请求权限
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x123);

        //加载本地数据源

    }
//更改壁纸
    private void exchangeBg() {
       preferences = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum= preferences.getInt("bg", 1);
        switch (bgNum) {
            case 0:
                layout.setBackgroundResource(R.mipmap.bg2);
                break;
            case 1:
                layout.setBackgroundResource(R.mipmap.bg);
                break;
            case 2:
                layout.setBackgroundResource(R.mipmap.bg3);
                break;
        }
    }

    private void Scheduled() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
            }
        },0,1,TimeUnit.SECONDS);
    }

    private void playMusicBean(int pos) {
        LocalMusicBean musicBean = mDatas.get(pos);
        duration=musicBean.getNum_duration();
       singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setMax(duration);
        maxtime.setText(musicBean.getDuration());
        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        //设置新的播放路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            localMusicData();
        }
    }

    private void localMusicData() {
        //加载本地存储当中的音乐到集合当中
        //1.获取contentresolver对象

        ContentResolver resolver = getContentResolver();
        //2.获取本地音乐存储的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        3 开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);
//        4.遍历Cursor
        int id = 0;
        while (cursor.moveToNext()) {
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String time=formate(duration);
//            将一行当中的数据封装到对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path,duration);
            mDatas.add(bean);
        }
//        数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        currenttime=findViewById(R.id.music_firsttime);
        maxtime=findViewById(R.id.music_time);
        back=findViewById(R.id.more_iv_back);
        layout=findViewById(R.id.main_layout);
        seekBar=findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pg, boolean fromUser) {
                if(fromUser==true){
                mediaPlayer.seekTo(pg);}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nextIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music_bottom_iv_play:
                handler.sendEmptyMessageDelayed(1,1000);
               if(selectedPosition==-1){
                   Toast.makeText(this,"请选择一首音乐",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(mediaPlayer.isPlaying()){
                   pauseMusic();
               }else {
                   playMusic();
               }
                break;
            case R.id.local_music_bottom_iv_next:
                if(selectedPosition==-1){
                    Toast.makeText(this,"请选择一首音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedPosition==mDatas.size()-1){
                    Toast.makeText(this,"这已经是最后一首歌了",Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedPosition=selectedPosition+1;
                playMusicBean(selectedPosition);
                break;
            case R.id.local_music_bottom_iv_last:
                if(selectedPosition==-1){
                    Toast.makeText(this,"请选择一首音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedPosition==0){
                    Toast.makeText(this,"这已经是第一首歌了",Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedPosition=selectedPosition-1;
                playMusicBean(selectedPosition);
                break;
            case R.id.more_iv_back:
                Intent intent=new Intent(MainActivity.this,MoreActivity.class);
                startActivity(intent);
        }
    }

    private void pauseMusic() {
        currentPlayPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        playIv.setImageResource(R.mipmap.pause1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            currentPlayPosition=0;
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if(currentPlayPosition==0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    volumeshaper();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                mediaPlayer.seekTo(currentPlayPosition);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.play1);
        }
    }

    private void volumeshaper() {
        VolumeShaper.Configuration build = new VolumeShaper.Configuration.Builder().setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR).setCurve(new float[]{0f, 0.5f, 1f}, new float[]{0f, 1f, 0f}).setDuration(1000 * 60 * 3).build();
        volumeShaper=mediaPlayer.createVolumeShaper(build);
        volumeShaper.apply(VolumeShaper.Operation.PLAY);

    }

}
