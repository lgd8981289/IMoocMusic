package com.sunday.imoocmusicdemo.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.sunday.imoocmusicdemo.R;
import com.sunday.imoocmusicdemo.activitys.PlayMusicActivity;
import com.sunday.imoocmusicdemo.activitys.WelcomeActivity;
import com.sunday.imoocmusicdemo.helps.MediaPlayerHelp;
import com.sunday.imoocmusicdemo.models.MusicModel;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    public static final int NOTIFICATION_ID = 1;

    private MediaPlayerHelp mMediaPlayerHelp;
    private MusicModel mMusicModel;

    public MusicService() {
    }

    public class MusicBind extends Binder {

        /**
         * 设置音乐
         */
        public void setMusic (MusicModel musicModel) {
            mMusicModel = musicModel;
            startForeground();
        }

        /**
         * 播放音乐
         */
        public void playMusic () {
            /**
             * 1、判断当前音乐是否是已经在播放的音乐
             * 2、如果当前的音乐是已经在播放的音乐的话，那么就直接执行start方法
             * 3、如果当前播放的音乐不是需要播放的音乐的话，那么就调用setPath的方法
             */
            if (mMediaPlayerHelp.getPath() != null
                    && mMediaPlayerHelp.getPath().equals(mMusicModel.getPath())) {
                mMediaPlayerHelp.start();
            } else {
                mMediaPlayerHelp.setPath(mMusicModel.getPath());
                mMediaPlayerHelp.setOnMeidaPlayerHelperListener(new MediaPlayerHelp.OnMeidaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayerHelp.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSelf();
                    }

                });
            }
        }

        /**
         * 停止音乐
         */
        public void stopMusic () {
            mMediaPlayerHelp.pause();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayerHelp = MediaPlayerHelp.getInstance(this);
    }

    /**
     * 设置服务在前台的展示
     */
    private void startForeground () {
//        创建 PendingIntent ，用作notification 被点击时跳转的intent。
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

//        创建 Notification
        Notification notification = new Notification.Builder(this)
                .setContentTitle(mMusicModel.getName())
                .setContentText(mMusicModel.getAuthor())
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .build();


//        设置 Notification 的前台展示
        startForeground(NOTIFICATION_ID, notification);
    }

}
