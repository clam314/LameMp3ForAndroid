package com.clam314.lame;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by clam314 on 2016/7/20
 *
 * 对系统的MediaPlay进行简单的封装，使其只需要三步就可以播放音频文件
 */
public class MediaPlayUtil {

    private static MediaPlayer mediaPlayer;

    //播放前需要初始化
    public static void init(Context context){
        if(mediaPlayer == null){
            synchronized (MediaPlayUtil.class){
                mediaPlayer = getMediaPlayer(context);
            }
        }
    }

    //播放音频
    public static void playSound(String path, MediaPlayer.OnCompletionListener listener) {
       if(mediaPlayer == null){
          throw new RuntimeException("MediaPlayer no init,please call init() before");
       }
        try {
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(listener);
            mediaPlayer.start();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        } catch (SecurityException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.reset();
        }
    }

    //播放完毕需要释放MediaPlayer的资源
    public static void release(){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //初始化MediaPlayer
    private static MediaPlayer getMediaPlayer(Context context){
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaplayer;
    }
}
