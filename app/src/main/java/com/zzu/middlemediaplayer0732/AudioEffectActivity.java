package com.zzu.middlemediaplayer0732;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioEffectActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private BassBoost bassBoost;
    private Virtualizer virtualizer;
    private AudioManager audioManager;
    private int mMaxVolume=0;
    private int mleftVolume=0;
    private int mRightVolume=0;
    private TextView bassBostTextView;
    private SeekBar bassBostSeekbar;
    private TextView virtualTextView;
    private SeekBar virtualSeekbar;
    private TextView currentVolumeSetTextView;
    private SeekBar currentVolumeSeekbar;
    private TextView leftVolumeTextView;
    private SeekBar leftVolumeSeekbar;
    private TextView rightVolumeTextView;
    private SeekBar rightVolumeSeekbar;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_effect);
        initview();
        playSong();
        bassBoost = new BassBoost(0, mediaPlayer.getAudioSessionId());
        bassBoost.setEnabled(true);

        virtualizer = new Virtualizer(0, mediaPlayer.getAudioSessionId());
        virtualizer.setEnabled(true);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolumeSeekbar.setMax(mMaxVolume);
        leftVolumeSeekbar.setMax(mMaxVolume);
        rightVolumeSeekbar.setMax(mMaxVolume);

    }


    private void playSong() {
        mediaPlayer = MediaPlayer.create(this, R.raw.media1);
        mediaPlayer.start();
    }

    private void initview() {
        bassBostTextView = findViewById(R.id.bassBostTextView);
        bassBostSeekbar = findViewById(R.id.bassBostSeekbar);
        virtualTextView = findViewById(R.id.virtualTextView);
        virtualSeekbar = findViewById(R.id.virtualSeekbar);
        currentVolumeSetTextView = findViewById(R.id.currentVolumeSetTextView);
        currentVolumeSeekbar = findViewById(R.id.currentVolumeSeekbar);
        leftVolumeTextView = findViewById(R.id.leftVolumeTextView);
        leftVolumeSeekbar = findViewById(R.id.leftVolumeSeekbar);
        rightVolumeTextView = findViewById(R.id.RightVolumeTextView);
        rightVolumeSeekbar = findViewById(R.id.rightVolumeSeekbar);
        back=findViewById(R.id.more_iv_back);

        bassBostSeekbar.setOnSeekBarChangeListener(mbassseekbarchangelistener);
        virtualSeekbar.setOnSeekBarChangeListener(mvirtualChangeListener);
        currentVolumeSeekbar.setOnSeekBarChangeListener(mVolumeBarChangerListener);
        leftVolumeSeekbar.setOnSeekBarChangeListener(mleftSeekBarChangeListener);
        rightVolumeSeekbar.setOnSeekBarChangeListener(mRightVolumeChangeListener);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener mbassseekbarchangelistener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (null != bassBoost) {
                bassBoost.setStrength((short) (progress * 10));
            }
            short strength = bassBoost.getRoundedStrength();
            boolean isSupported = bassBoost.getStrengthSupported();
            bassBostTextView.setText("strength" + strength + " " + isSupported);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mvirtualChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            short strength = 0;
            boolean isSupported = false;
            if (null != virtualizer) {
                virtualizer.setStrength((short) (progress * 10));
                strength = virtualizer.getRoundedStrength();
                virtualTextView.setText("strength=" + strength + "," + isSupported);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private SeekBar.OnSeekBarChangeListener mVolumeBarChangerListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                currentVolumeSetTextView.setText(Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private SeekBar.OnSeekBarChangeListener mleftSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mleftVolume = progress;
            setLeftAndRight();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private SeekBar.OnSeekBarChangeListener mRightVolumeChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mRightVolume = progress;
            setLeftAndRight();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void setLeftAndRight() {
        float leftRadio = mleftVolume / (float) mMaxVolume;
        float rightRadio = mRightVolume / (float) mMaxVolume;
        mediaPlayer.setVolume(leftRadio, rightRadio);
        leftVolumeTextView.setText("leftVolume = " + mleftVolume);
        rightVolumeTextView.setText("rightVolume =" + mRightVolume);

    }
}
