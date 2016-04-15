#include "encryption.h"
jbyteArray jStringToJbyteArray(JNIEnv *env, jstring str) {
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env,str, mid, strencode);
    return barr;
}
JNIEXPORT jbyteArray JNICALL Java_com_kaws_encryption_tool_Encryption_HmacSHA1Encrypt(JNIEnv *env, jclass type, jbyteArray encryptText_){

    jstring str = (*env)->NewStringUTF(env,"96d4ca89fb62420e");
    jbyteArray jby = jStringToJbyteArray(env, str);
    jclass key = (*env)->FindClass(env,"javax/crypto/spec/SecretKeySpec");
    jmethodID keyid = (*env)->GetMethodID(env,key, "<init>", "([BLjava/lang/String;)V");
    jobject keyo = (*env)->NewObject(env,key, keyid, jby, (*env)->NewStringUTF(env,"HmacSHA256"));
    jclass mac = (*env)->FindClass(env,"javax/crypto/Mac");
    jmethodID macid = (*env)->GetStaticMethodID(env,mac, "getInstance","(Ljava/lang/String;)Ljavax/crypto/Mac;");
    jobject maco = (*env)->CallStaticObjectMethod(env,mac, macid, (*env)->NewStringUTF(env,"HmacSHA256"));
    jmethodID init = (*env)->GetMethodID(env,mac, "init", "(Ljava/security/Key;)V");
    (*env)->CallVoidMethod(env,maco, init, keyo);
    jmethodID doFinal = (*env)->GetMethodID(env,mac, "doFinal", "([B)[B");
    return (jbyteArray) (*env)->CallObjectMethod(env,maco, doFinal, encryptText_);
  }