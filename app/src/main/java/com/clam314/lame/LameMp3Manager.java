package com.clam314.lame;

import com.buihha.audiorecorder.Mp3Recorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by clam314 on 2016/8/24
 */
public enum LameMp3Manager implements Mp3Recorder.OnFinishRecorder{
    instance;

    private Mp3Recorder mp3Recorder;
    private boolean cancel = false;
    private boolean stop = false;
    private MediaRecorderListener mediaRecorderListener;

    LameMp3Manager(){
        mp3Recorder = new Mp3Recorder();
        mp3Recorder.setOnFinishRecorder(this);
    }

    public void setMediaRecorderListener(MediaRecorderListener listener){
        mediaRecorderListener = listener;
    }

    public void startRecorder(String basePath){
        cancel = stop = false;
        mp3Recorder.setBasePath(basePath);
        try {
            mp3Recorder.startRecording();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cancelRecorder(){
        try {
            mp3Recorder.stopRecording();
            cancel = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stopRecorder(){
        try {
            mp3Recorder.stopRecording();
            stop = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onFinishRecorder(String mp3FilePath) {
        if(cancel){
            File mp3 = new File(mp3FilePath);
            if(mp3.exists()){
                mp3.delete();
            }
            cancel = false;
        }else if(stop){
            stop = false;
            if(mediaRecorderListener != null){
                mediaRecorderListener.onRecorderFinish(209,mp3FilePath);
            }
        }
    }
}
