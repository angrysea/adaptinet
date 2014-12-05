#pragma once
#include <iconv.h>
#include <string>
#include <vector>
#include <iostream>

#include <jni.h>

jstring convert(JNIEnv * env, std::wstring s);
jstring convert(JNIEnv * env, wchar_t * s, int length);
std::wstring convert(JNIEnv * env, jstring js);

