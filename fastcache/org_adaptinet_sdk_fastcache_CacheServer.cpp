/*
 * -Djava.library.path=/home/anthony/workspace/fastcache/Debug
 * -classpath /home/anthony/workspace/sdk/target/classes:/home/anthony/workspace/instrumentclient/bin:/home/anthony/workspace/SynthesisWS/bin
 * com.db.instrument.client.Client
 *
 * -classpath /home/anthony/workspace/sdk/target/classes:/home/anthony/workspace/sdksamples/target/classes
 * org.adaptinet.sdk.samples.fastcache.Sample1
 */



#include "CacheServer.h"
#include "org_adaptinet_sdk_fastcache_CacheServer.h"
#include "jconverters.h"
#include "JNIException.h"

JNIEnv * myEnv=nullptr;
jclass sClass=nullptr;
jclass dosCls=nullptr;
jmethodID dosCons=nullptr;
jmethodID dosWriteInt=nullptr;
jmethodID dosWriteBytes=nullptr;
jclass disCls=nullptr;
jmethodID disCons=nullptr;
jmethodID disReadInt=nullptr;
jmethodID disReadBytes=nullptr;
jclass dataItemClass=nullptr;
jmethodID dataItemCons=nullptr;
jclass dataArrayClass=nullptr;
jmethodID dataArrayCons=nullptr;

jclass failClass=nullptr;
jmethodID failCons=nullptr;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *pjvm, void *reserved)
{
	pjvm->AttachCurrentThread((void **)&myEnv, NULL);
	sClass = myEnv->FindClass("java/lang/String");
	dosCls=myEnv->FindClass("java/io/DataOutputStream");
	dosCons=myEnv->GetMethodID(dosCls,"<init>","(Ljava/io/OutputStream;)V");
	dosWriteInt=myEnv->GetMethodID(dosCls,"writeInt","(I)V");
	dosWriteBytes=myEnv->GetMethodID(dosCls,"write","([B)V");
	disCls=myEnv->FindClass("java/io/DataInputStream");
	disCons=myEnv->GetMethodID(disCls,"<init>","(Ljava/io/InputStream;)V");
	disReadInt=myEnv->GetMethodID(disCls,"readInt","()I");
	disReadBytes=myEnv->GetMethodID(disCls,"readFully","([B)V");
	dataItemClass=myEnv->FindClass("org/adaptinet/sdk/fastcache/DataItem");
	dataItemCons=myEnv->GetMethodID(dataItemClass, "<init>", "()V");
	dataArrayClass=myEnv->FindClass("org/adaptinet/sdk/fastcache/DataArray");
	dataArrayCons=myEnv->GetMethodID(dataArrayClass,"<init>","()V");
	failClass=myEnv->FindClass("org/adaptinet/sdk/adaptinetex/FastCacheException");
	failCons=myEnv->GetMethodID(failClass, "<init>", "(ILjava/lang/String;)V");

	return JNI_VERSION_1_8;
}

void ThrowFastCacheException(JNIEnv * env, const char* desc, jint hr)
{
	if (!desc)
	{
		desc = "Java/JNI Error";
	}
	jstring js = env->NewStringUTF(desc);
	jthrowable fail = (jthrowable)env->NewObject(failClass, failCons, hr, js);
	env->Throw(fail);
	throw JNIException(1);
}


CacheServer * getCacheServer(JNIEnv * env, jobject _this)
{
	CacheServer * cs = 0;

	jclass c=env->GetObjectClass(_this);
	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c,"cacheserver","J");
		if(field!=0)
		{
			cs=(CacheServer*)env->GetLongField(_this, field);
			if(cs==0)
			{
				cs = CacheServer::getCacheServer();
				setCacheServer(env, _this, cs);
			}
		}
	}

	if(cs==0)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - underlying CacheServer missing.", -1);
	}

	return cs;
}

bool setCacheServer(JNIEnv * env, jobject _this, CacheServer * cs)
{
	bool bRet=false;

	jclass c=env->GetObjectClass(_this);

	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c,"cacheserver","J");
		if(field!=0)
		{
			env->SetLongField(_this, field, (jlong)cs);
			bRet=true;
		}
	}

	return bRet;
}


JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_CacheServer_init(JNIEnv * env, jobject _this)
{
	try
	{
		CacheServer * cs = getCacheServer(env, _this);
		cs = CacheServer::getCacheServer();
		setCacheServer(env, _this, cs);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - Error releasing DataItem.", -1);
	}
}


JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_CacheServer_putValue
  (JNIEnv * env, jobject _this, jstring key, jobject value)
{
	CacheServer * cs = getCacheServer(env, _this);
	try
	{
		DATAITEM *di = getDataItem(env, value);
		std::wstring str = convert(env, key);
		cs->putValue(str,*di);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - Error releasing DataItem.", -1);
	}

	return true;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_CacheServer_getValue
  (JNIEnv * env, jobject _this, jstring key)
{
	jobject value = 0;
	try
	{
		CacheServer * cs = getCacheServer(env, _this);
		jclass dataItemClass = env->FindClass("org/adaptinet/sdk/fastcache/DataItem");
		jmethodID dataItemCons = env->GetMethodID(dataItemClass, "<init>", "()V");
		value = env->NewObject(dataItemClass, dataItemCons);
		std::wstring str = convert(env, key);
		DATAITEM * di = getDataItem(env, value);
		cs->getValue(str,*di);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - Error releasing DataItem.", -1);
	}
	return value;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_CacheServer_destroy(JNIEnv *env, jobject _this)
{
	try
	{
		CacheServer * cs = getCacheServer(env, _this);
		delete cs;
		jclass saClass = env->GetObjectClass(_this);
		jfieldID jf = env->GetFieldID(saClass, "cacheserver", "J");
		env->SetLongField(_this, jf, 0l);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env,"CacheServer Destroy failed",0);
	}
}
