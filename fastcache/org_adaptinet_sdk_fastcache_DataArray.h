/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_adaptinet_sdk_fastcache_DataArray */

#ifndef _Included_org_adaptinet_sdk_fastcache_DataArray
#define _Included_org_adaptinet_sdk_fastcache_DataArray
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    clone
 * Signature: ()Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_clone
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_destroy
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    DataArrayFree
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_DataArrayFree
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDataType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataType
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getElements
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getElements
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getElemSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getElemSize
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromCharArray
 * Signature: ([C)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromCharArray
  (JNIEnv *, jobject, jcharArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromIntArray
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromIntArray
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromShortArray
 * Signature: ([S)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromShortArray
  (JNIEnv *, jobject, jshortArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromDoubleArray
 * Signature: ([D)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromDoubleArray
  (JNIEnv *, jobject, jdoubleArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromStringArray
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromStringArray
  (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromByteArray
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromByteArray
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromFloatArray
 * Signature: ([F)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromFloatArray
  (JNIEnv *, jobject, jfloatArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromBooleanArray
 * Signature: ([Z)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromBooleanArray
  (JNIEnv *, jobject, jbooleanArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    fromDataItemArray
 * Signature: ([Lorg/adaptinet/sdk/fastcache/DataItem;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromDataItemArray
  (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toCharArray
 * Signature: ()[C
 */
JNIEXPORT jcharArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toCharArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toIntArray
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toIntArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toShortArray
 * Signature: ()[S
 */
JNIEXPORT jshortArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toShortArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toDoubleArray
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toDoubleArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toStringArray
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toStringArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toByteArray
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toByteArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toFloatArray
 * Signature: ()[F
 */
JNIEXPORT jfloatArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toFloatArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toBooleanArray
 * Signature: ()[Z
 */
JNIEXPORT jbooleanArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toBooleanArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    toDataItemArray
 * Signature: ()[Lorg/adaptinet/sdk/fastcache/DataItem;
 */
JNIEXPORT jobjectArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toDataItemArray
  (JNIEnv *, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getChar
 * Signature: (J)C
 */
JNIEXPORT jchar JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getChar
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setChar
 * Signature: (JC)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setChar
  (JNIEnv *, jobject, jlong, jchar);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getChars
 * Signature: (JI[CI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getChars
  (JNIEnv *, jobject, jlong, jint, jcharArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setChars
 * Signature: (JI[CI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setChars
  (JNIEnv *, jobject, jlong, jint, jcharArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getInt
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getInt
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setInt
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setInt
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getInts
 * Signature: (JI[II)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getInts
  (JNIEnv *, jobject, jlong, jint, jintArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setInts
 * Signature: (JI[II)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setInts
  (JNIEnv *, jobject, jlong, jint, jintArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getShort
 * Signature: (J)S
 */
JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getShort
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setShort
 * Signature: (JS)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setShort
  (JNIEnv *, jobject, jlong, jshort);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getShorts
 * Signature: (JI[SI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getShorts
  (JNIEnv *, jobject, jlong, jint, jshortArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setShorts
 * Signature: (JI[SI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setShorts
  (JNIEnv *, jobject, jlong, jint, jshortArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDouble
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDouble
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDateasLong
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDateasLong
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDateasLong
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDateasLong
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDouble
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDouble
  (JNIEnv *, jobject, jlong, jdouble);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDoubles
 * Signature: (JI[DI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDoubles
  (JNIEnv *, jobject, jlong, jint, jdoubleArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDoubles
 * Signature: (JI[DI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDoubles
  (JNIEnv *, jobject, jlong, jint, jdoubleArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getString
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getString
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setString
 * Signature: (JLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setString
  (JNIEnv *, jobject, jlong, jstring);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getStrings
 * Signature: (JI[Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getStrings
  (JNIEnv *, jobject, jlong, jint, jobjectArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setStrings
 * Signature: (JI[Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setStrings
  (JNIEnv *, jobject, jlong, jint, jobjectArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getByte
 * Signature: (J)B
 */
JNIEXPORT jbyte JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getByte
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setByte
 * Signature: (JB)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setByte
  (JNIEnv *, jobject, jlong, jbyte);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getBytes
 * Signature: (JI[BI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBytes
  (JNIEnv *, jobject, jlong, jint, jbyteArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setBytes
 * Signature: (JI[BI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBytes
  (JNIEnv *, jobject, jlong, jint, jbyteArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getFloat
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getFloat
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setFloat
 * Signature: (JF)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setFloat
  (JNIEnv *, jobject, jlong, jfloat);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getFloats
 * Signature: (JI[FI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getFloats
  (JNIEnv *, jobject, jlong, jint, jfloatArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setFloats
 * Signature: (JI[FI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setFloats
  (JNIEnv *, jobject, jlong, jint, jfloatArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getBoolean
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBoolean
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setBoolean
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBoolean
  (JNIEnv *, jobject, jlong, jboolean);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getBooleans
 * Signature: (JI[ZI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBooleans
  (JNIEnv *, jobject, jlong, jint, jbooleanArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setBooleans
 * Signature: (JI[ZI)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBooleans
  (JNIEnv *, jobject, jlong, jint, jbooleanArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDataItem
 * Signature: (JLorg/adaptinet/sdk/fastcache/DataItem;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataItem
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDataArray
 * Signature: (JLorg/adaptinet/sdk/fastcache/DataArray;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataArray
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDataItem
 * Signature: (JLorg/adaptinet/sdk/fastcache/DataItem;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDataItem
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDataArray
 * Signature: (JLorg/adaptinet/sdk/fastcache/DataArray;)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDataArray
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    getDataItems
 * Signature: (JI[Lorg/adaptinet/sdk/fastcache/DataItem;I)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataItems
  (JNIEnv *, jobject, jlong, jint, jobjectArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    setDataItems
 * Signature: (JI[Lorg/adaptinet/sdk/fastcache/DataItem;I)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDataItems
  (JNIEnv *, jobject, jlong, jint, jobjectArray, jint);

/*
 * Class:     org_adaptinet_sdk_fastcache_DataArray
 * Method:    init
 * Signature: (IJ)V
 */
JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_init
  (JNIEnv *, jobject, jint, jlong);

#ifdef __cplusplus
}
#endif
#endif
