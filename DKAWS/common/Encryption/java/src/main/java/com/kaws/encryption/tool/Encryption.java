package com.kaws.encryption.tool;

/**
 * Created by Administrator on 2015/11/20.
 */
public class Encryption {
    static {
        System.loadLibrary("encryption");
    }
    public static native byte[] HmacSHA1Encrypt(byte[] encryptText);
}
