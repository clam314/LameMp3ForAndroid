//
// Created by clam314 on 2017/3/26.
//

#include <jni.h>

extern "C"
{
void Java_com_clam314_lame_SimpleLame_close(JNIEnv *env, jclass type);

jint Java_com_clam314_lame_SimpleLame_encode(JNIEnv *env, jclass type, jshortArray buffer_l_,
                                             jshortArray buffer_r_, jint samples, jbyteArray mp3buf_);

jint Java_com_clam314_lame_SimpleLame_flush(JNIEnv *env, jclass type, jbyteArray mp3buf_);

void Java_com_clam314_lame_SimpleLame_init__IIIII(JNIEnv *env, jclass type, jint inSampleRate,
                                                   jint outChannel, jint outSampleRate, jint outBitrate, jint quality);
}