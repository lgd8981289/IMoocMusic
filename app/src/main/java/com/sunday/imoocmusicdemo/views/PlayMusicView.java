package com.sunday.imoocmusicdemo.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.ServiceUtils;
import com.bumptech.glide.Glide;
import com.sunday.imoocmusicdemo.R;
import com.sunday.imoocmusicdemo.helps.MediaPlayerHelp;
import com.sunday.imoocmusicdemo.models.MusicModel;
import com.sunday.imoocmusicdemo.services.MusicService;

public class PlayMusicView extends FrameLayout {

    private Context mContext;

    private MusicModel mMusicModel;
    private MusicService.MusicBind mMusicBinder;
    private Intent mServiceIntent;
    private boolean isPlaying, isBindService;
    private View mView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvIcon , mIvNeedle, mIvPlay;

    private Animation mPlayMusicAnim, mPlayNeedleAnim, mStopNeedleAnim;



    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init (Context context) {
//        MediaPlayer
        mContext = context;

        mView = LayoutInflater.from(mContext).inflate(R.layout.play_music, this, false);

        mFlPlayMusic = mView.findViewById(R.id.fl_play_music);
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trigger();
            }
        });
        mIvIcon = mView.findViewById(R.id.iv_icon);
        mIvNeedle = mView.findViewById(R.id.iv_needle);
        mIvPlay = mView.findViewById(R.id.iv_play);

        /**
         * 1、定义所需要执行的动画
         *      1、光盘转动的动画
         *      2、指针指向光盘的动画
         *      3、指针离开光盘的动画
         * 2、startAnimation
         */

        mPlayMusicAnim = AnimationUtils.loadAnimation(mContext, R.anim.play_music_anim);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(mContext, R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(mContext, R.anim.stop_needle_anim);

        addView(mView);
    }

    /**
     * 切换播放状态
     */
    private void trigger () {
        if (isPlaying) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic () {
        isPlaying = true;
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

//        启动服务
        startMusicService();

    }

    /**
     * 停止播放
     */
    public void stopMusic () {
        isPlaying = false;
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

        mMusicBinder.stopMusic();
    }

    /**
     * 设置光盘中显示的音乐封面图片
     */
    private void setMusicIcon () {
        Glide.with(mContext)
                .load(mMusicModel.getPoster())
                .into(mIvIcon);
    }

    /**
     * 设置音乐播放模型
     */
    public void setMusic (MusicModel musicModel) {
        this.mMusicModel = musicModel;

        setMusicIcon();
    }


    /**
     * 启动音乐服务
     */
    private void startMusicService () {

        if (mServiceIntent == null) {
            mServiceIntent = new Intent(mContext, MusicService.class);
            mContext.startService(mServiceIntent);
        } else {
            mMusicBinder.playMusic();
        }


//        当前未绑定，绑定服务，同时修改绑定状态
        if (!isBindService) {
            isBindService = true;
            mContext.bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 销毁方法，需要在 activity 被销毁的时候调用
     */
    public void destroy () {
//        如果已绑定服务，则解除绑定，同时修改绑定状态
        if (isBindService) {
            isBindService = false;
            mContext.unbindService(conn);
        }

    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder = (MusicService.MusicBind) service;
            mMusicBinder.setMusic(mMusicModel);
            mMusicBinder.playMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
