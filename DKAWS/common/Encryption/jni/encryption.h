#include <jni.h>
/* Header for class com_kaws_encryption_tool_Encryption */

#ifndef _Included_com_kaws_encryption_tool_Encryption
#define _Included_com_kaws_encryption_tool_Encryption
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_kaws_encryption_tool_Encryption
 * Method:    HmacSHA1Encrypt
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_kaws_encryption_tool_Encryption_HmacSHA1Encrypt
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
