#pragma once
#include <string>

#include <jni.h>

#include "InstanceNode.h"
#include "DataItem.h"

class CacheServer
{
public:
	~CacheServer();
	static CacheServer * getCacheServer();
	bool putValue(const std::wstring & key, const DATAITEM & value);
	bool getValue(const std::wstring & key, DATAITEM & dataItem);

private:
	InstanceNode root;

	static CacheServer * theCacheServer;
};

CacheServer * getCacheServer(JNIEnv * env, jobject obj);
bool setCacheServer(JNIEnv * env, jobject _this, CacheServer * cs);
