package com.clam314.lame;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {
    private MediaRecorderButton btRecorder;
    private ImageView btPlayer;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建播放时用的动画
        AnimationDrawable frameDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_voice_left);
        btPlayer = (ImageView)findViewById(R.id.bt_player);
        btPlayer.setImageDrawable(frameDrawable);
        //动画显示在第一帧
        frameDrawable.selectDrawable(0);
        btPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(TextUtils.isEmpty(filePath)){
                    Toast.makeText(v.getContext(),"文件路径错误",Toast.LENGTH_SHORT).show();
                }else {
                    if(v.getTag() != null){
                        String tag =  (String) v.getTag();
                        //语音正在播放，此时单击停止播放
                        if("showing".equals(tag)){
                            //释放资源
                            MediaPlayUtil.release();
                            ((AnimationDrawable)((ImageView)v).getDrawable()).stop();
                            ((AnimationDrawable)((ImageView)v).getDrawable()).selectDrawable(0);
                            v.setTag("showed");
                            return;
                        }
                    }

                    //开始播放语音，并开始动画
                    ((AnimationDrawable)((ImageView)v).getDrawable()).start();
                    v.setTag("showing");
                    MediaPlayUtil.init(getApplicationContext());
                    MediaPlayUtil.playSound(filePath, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((AnimationDrawable)((ImageView)v).getDrawable()).stop();
                            ((AnimationDrawable)((ImageView)v).getDrawable()).selectDrawable(0);
                            v.setTag("showed");
                            MediaPlayUtil.release();
                        }
                    });
                }
            }
        });


        btRecorder = (MediaRecorderButton)findViewById(R.id.bt_media_recorder);
        btRecorder.setFinishListener(new MediaRecorderButton.RecorderFinishListener() {
            @Override
            public void onRecorderFinish(int status, String path, String second) {
                if(status == MediaRecorderButton.RECORDER_SUCCESS){
                    filePath = path;
                }
            }
        });
        btRecorder.setRecorderStatusListener(new MediaRecorderButton.RecorderStatusListener() {
            @Override
            public void onStart(int status) {
                Toast.makeText(MainActivity.this,"开始录音",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnd(int status) {
                //status除了是状态还有录音的秒数
                switch (status){
                    case MediaRecorderButton.END_RECORDER_TOO_SHORT:
                        Toast.makeText(MainActivity.this,"讲话时间太短啦！",Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        Toast.makeText(MainActivity.this,"录音50秒啦，10秒后自动发送",Toast.LENGTH_SHORT).show();
                        break;
                    case MediaRecorderButton.END_RECORDER_60S:
                        Toast.makeText(MainActivity.this,"录音已经自动发送",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
