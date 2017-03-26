package com.clam314.lame;
import android.media.AudioFormat;

/**
 * Created by clam314 on 2017/3/26.
 */


public enum PCMFormat {
    PCM_8BIT (1, AudioFormat.ENCODING_PCM_8BIT),
    PCM_16BIT (2, AudioFormat.ENCODING_PCM_16BIT);

    private int bytesPerFrame;
    private int audioFormat;

    PCMFormat(int bytesPerFrame, int audioFormat) {
        this.bytesPerFrame = bytesPerFrame;
        this.audioFormat = audioFormat;
    }

    public int getBytesPerFrame() {
        return bytesPerFrame;
    }

    public void setBytesPerFrame(int bytesPerFrame) {
        this.bytesPerFrame = bytesPerFrame;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }
}