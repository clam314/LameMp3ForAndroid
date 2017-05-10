# LameMp3ForAndroid
![image](https://github.com/clam314/Image/blob/master/lameforandroid.png?raw=true)<br>
<br>
本次demo使用AndroidStudio+Cmake+NDK进行开发<br>
利用Android SDK提供的AndroidRecorder进行录音，得到PCM数据，并使用jni调用Lame这个C库将PCM数据转换为MP3文件。并使用MediaPlayer对录音的MP3文件进行播放。<br>
<br>
#### 主要类的介绍
- Mp3Recorder—— 是负责调用AudioRecorder进行录音的类<br>
- SimpleLame——是负责将MP3Recorder录制出的PCM数据转换成MP3文件<br>
- DataEncodeThread——是负责执行PCM转MP3的线程<br>
- LameMp3Manager——是对Mp3Recorder的多一次封装，增加了取消后删除之前录制的数据的逻辑<br>
- MediaPlayerUtil——是对系统的MediaPlay进行简单的封装，使其只需要三步就可以播放音频文件<br>
- MediaRecorderButton ——是一个仿微信录音按键的控件，按下录制，松开结束，录制时上滑则取消录制<br>
#### 录制的流程
1. Mp3Recorder调用startRecording()开始录制并初始化DataEncoderThread线程，并定期将录制的PCM数据，传入DataEncoderThread中。
2. 在DataEncoderThread里，SimpleLame将Mp3Recorder传入的PCM数据转换成MP3格式并写入文件，其中SimpleLame通过jni对Lame库进行调用
3. Mp3Recorder调用stopRecording()停止录制，并通知DataEncoderThread线程录制结束，DataEncoderThread将剩余的数据转换完毕。
#### 编译过程
- http://www.jianshu.com/p/065bfe6d3ec2#
#### 详细点的讲解
- http://www.jianshu.com/p/4a3e24e45ce9
