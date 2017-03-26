package com.clam314.lame;


import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by clam314 on 2017/3/26.
 */


public class DataEncodeThread extends Thread implements AudioRecord.OnRecordPositionUpdateListener {

    private static final String TAG = DataEncodeThread.class.getSimpleName();

    public static final int PROCESS_STOP = 1;

    private StopHandler handler;


    private byte[] mp3Buffer;

    //用于存取待转换的PCM数据
    private List<ChangeBuffer> mChangeBuffers = Collections.synchronizedList(new LinkedList<ChangeBuffer>());

    private FileOutputStream os;

    private CountDownLatch handlerInitLatch = new CountDownLatch(1);


    private static class StopHandler extends Handler {

        WeakReference<DataEncodeThread> encodeThread;

        public StopHandler(DataEncodeThread encodeThread) {
            this.encodeThread = new WeakReference<>(encodeThread);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROCESS_STOP) {
                DataEncodeThread threadRef = encodeThread.get();

                //录音停止后，将剩余的PCM数据转换完毕
                for (;threadRef.processData() > 0;);

                removeCallbacksAndMessages(null);
                threadRef.flushAndRelease();
                getLooper().quit();
            }
            super.handleMessage(msg);
        }
    }


    public DataEncodeThread(FileOutputStream os, int bufferSize) {
        this.os = os;
        mp3Buffer = new byte[(int) (7200 + (bufferSize * 2 * 1.25))];
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new StopHandler(this);
        handlerInitLatch.countDown();
        Looper.loop();
    }


    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "Error when waiting handle to init");
        }
        return handler;
    }

    @Override
    public void onMarkerReached(AudioRecord recorder) {
        // Do nothing
    }

    @Override
    public void onPeriodicNotification(AudioRecord recorder) {
        //由AudioRecord进行回调，满足帧数，通知数据转换
        processData();
    }


    //从缓存区ChangeBuffers里获取待转换的PCM数据，转换为MP3数据,并写入文件
    private int processData() {
        if(mChangeBuffers != null && mChangeBuffers.size() > 0) {
            ChangeBuffer changeBuffer = mChangeBuffers.remove(0);
            short[] buffer = changeBuffer.getData();
            int readSize = changeBuffer.getReadSize();
            Log.d(TAG, "Read size: " + readSize);
            if (readSize > 0) {
                int encodedSize = SimpleLame.encode(buffer, buffer, readSize, mp3Buffer);
                if (encodedSize < 0) {
                    Log.e(TAG, "Lame encoded size: " + encodedSize);
                }
                try {
                    os.write(mp3Buffer, 0, encodedSize);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Unable to write to file");
                }
                return readSize;
            }
        }
        return 0;
    }


    private void flushAndRelease() {
        final int flushResult = SimpleLame.flush(mp3Buffer);

        if (flushResult > 0) {
            try {
                os.write(mp3Buffer, 0, flushResult);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addChangeBuffer(short[] rawData, int readSize){
        mChangeBuffers.add(new ChangeBuffer(rawData, readSize));
    }

    private class ChangeBuffer{

        private short[] rawData;
        private int readSize;

        public ChangeBuffer(short[] rawData, int readSize){
            this.rawData = rawData.clone();
            this.readSize = readSize;
        }
        public short[] getData(){
            return rawData;
        }
        public int getReadSize(){
            return readSize;
        }
    }
}