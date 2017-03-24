package com.clam314.lame;

import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by clam314 on 2016/7/21
 */
public class MediaRecorderManager {
    private static String finishSavePath;
    private static MediaRecorder mediaRecorder;


    private static void init(String basePath) throws IllegalStateException,IOException {
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        finishSavePath = basePath + File.separator + "xcrm"+ System.currentTimeMillis() +".m4a";
        mediaRecorder.setOutputFile(finishSavePath);
        mediaRecorder.prepare();
    }

    public static void startRecorder(String savePath){
        if(TextUtils.isEmpty(savePath)){
            return;
        }
        try {
            init(savePath);
            mediaRecorder.start();
        }catch (IllegalStateException e){
            e.printStackTrace();
            resetRecorder();
        }catch (IOException e){
            e.printStackTrace();
            resetRecorder();
        }
    }

    public static void cancelRecorder(){
        if(mediaRecorder == null){
            return;
        }
        resetRecorder();
        deleteFile(finishSavePath);
    }

    public static String stopRecorder(){
        if(mediaRecorder == null){
            return null;
        }
        boolean error = false;
        try {
            mediaRecorder.stop();
        }catch (RuntimeException r){
            r.printStackTrace();
            error = true;
        }finally {
            resetRecorder();
        }
        try {
            File file = new File(finishSavePath);
            if(file.exists() && file.isFile()){
                if(error){
                    file.delete();
                    return null;
                }
                return file.getPath();
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void deleteFile(final String path){
        if(TextUtils.isEmpty(path)){
            return;
        }
        try {
            File file = new File(path);
            if(file.exists() && file.isFile()){
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void resetRecorder(){
        if(mediaRecorder != null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
