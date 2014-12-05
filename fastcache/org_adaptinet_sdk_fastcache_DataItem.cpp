#include <cwchar>
#include "DataItem.h"
#include "org_adaptinet_sdk_fastcache_DataItem.h"
#include "JNIException.h"
#include "jconverters.h"
#include "myenv.h"

DATAITEM * getDataItem(JNIEnv * env, jobject _this)
{
	DATAITEM * pDataItem=0;

	jclass c=env->GetObjectClass(_this);
	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c,"dataitem","J");
		if(field!=0)
		{
			pDataItem=(DATAITEM*)env->GetLongField(_this, field);
		}
	}

	if(pDataItem==0)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - underlying dataitem missing.", -1);
	}

	return pDataItem;
}

bool setDataItem(JNIEnv * env, jobject _this, DATAITEM * pDataItem)
{
	bool bRet=false;

	jclass c=env->GetObjectClass(_this);
	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c,"dataitem","J");
		if(field!=0)
		{
			env->SetLongField(_this, field, (jlong)pDataItem);
			bRet=true;
		}
	}

	return bRet;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_release(JNIEnv * env, jobject _this)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt!=DT_EMPTY) {
			DataItemClear(di);
		}
		setDataItem(env,_this,0);
		delete di;
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

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_init(JNIEnv * env, jobject _this)
{
	try
	{
		DATAITEM * di = new DATAITEM();
		DataItemInit(di);
		setDataItem(env, _this, di);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jbyteArray JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_Save(JNIEnv * env, jobject _this)
{
	jbyteArray ba = nullptr;
	try
	{
		DATAITEM * item = getDataItem(env, _this);

		jint size = DataItemGetItemSize(item);
		jbyte * pBuf=new jbyte[size];
		memset(pBuf,0x00,size);
		int mSize = DataItemUnMarshal((unsigned char*)pBuf, item);
		ba=env->NewByteArray(size);
		env->SetByteArrayRegion(ba,0,size,pBuf);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ba;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_Load(JNIEnv * env, jobject _this, jobject inStream)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		di=new DATAITEM();
		DataItemInit(di);
		setDataItem(env, _this, di);
	
		jobject dis=env->NewObject(disCls,disCons,inStream);

		jint size=env->CallIntMethod(dis,disReadInt);
		jbyteArray ba=env->NewByteArray(size);
		env->CallVoidMethod(dis,disReadBytes,ba);
		jbyte *pBuf=env->GetByteArrayElements(ba,0);

		DataItemUnMarshal((unsigned char *)pBuf,di);
		env->ReleaseByteArrayElements(ba,pBuf,0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toInt(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try 
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di, DT_I4)) 
		{
			ThrowFastCacheException(env,"DataItemChangeType failed",0);
		}
		ret = di->lVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jlong JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toDate(JNIEnv * env, jobject _this)
{
	jlong ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di, DT_DATE))
		{
			ThrowFastCacheException(env, "DataItemChangeType failed",0);
		}
		ret = di->date;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toBoolean(JNIEnv * env, jobject _this)
{
	jboolean ret = false;
	try
	{
		DATAITEM * di = getDataItem(env, _this);

		if(!DataItemChangeType(di,DT_BOOL))
		{
			ThrowFastCacheException(env,"DataItemChangeType failed",0);
		}
		ret = di->boolVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_cloneIndirect(JNIEnv * env, jobject _this)
{
	jobject newDI = 0;
	/*
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		jclass daClass = env->GetObjectClass(_this);
		jmethodID daCons = env->GetMethodID(daClass, "<init>", "()V");
		newDI = env->NewObject(daClass, daCons);
		jfieldID jf = env->GetFieldID(daClass, "dataitem", "J");
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	*/
	return newDI;
}

JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toDouble(JNIEnv * env, jobject _this)
{
	jdouble ret = 0.0;
	try
	{
		DATAITEM *di = getDataItem(env, _this);
		if(!DataItemChangeType(di, DT_R8)) 
		{
			ThrowFastCacheException(env, "DataItemChangeType failed",0);
		}
		ret = di->dblVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putShortRef(JNIEnv * env, jobject _this, jshort s)
{
	try
	{
		DATAITEM * di = getDataItem(env,_this);
		DataItemClear(di);
		short * ps = new short;
		*ps=s;
		di->dt = DT_I2|DT_BYREF;
		di->piVal = ps;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putIntRef(JNIEnv * env, jobject _this,jint s)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		long * ps = new long;
		*ps=s;
		di->dt = DT_I4|DT_BYREF;
		di->plVal = ps;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDoubleRef(JNIEnv * env, jobject _this,jdouble s)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		double *ps = new double;
		*ps=s;
		di->dt = DT_R8|DT_BYREF;
		di->pdblVal = ps;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDateRef(JNIEnv * env, jobject _this,jdouble s)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		time_t *ps = new time_t;
		*ps=s;
		di->dt = DT_DATE|DT_BYREF;
		di->pdate = ps;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putStringRef(JNIEnv * env, jobject _this, jstring s)
{
	try
	{
		DATAITEM  *di = getDataItem(env ,_this);
		DataItemClear(di);
		std::wstring str = convert(env, s);
		SString ss = SafeString(str.c_str());
		di->dt = DT_SSTR | DT_BYREF;
		di->psstringVal=&ss;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getShortRef(JNIEnv * env, jobject _this)
{
	jshort ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt == (DT_I2|DT_BYREF)) 
		{
			ret = *di->piVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getIntRef(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt == (DT_I4|DT_BYREF)) 
		{
			ret = *di->plVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putShort(JNIEnv * env, jobject _this, jshort s)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt = DT_I2;
		di->iVal = (short)s;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getShort(JNIEnv * env, jobject _this)
{
	jshort ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==DT_I2) 
		{
			ret = (jshort)di->iVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getDoubleRef(JNIEnv * env, jobject _this)
{
	jdouble ret = 0.0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_R8|DT_BYREF)) 
		{
			ret = (jdouble)*di->pdblVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getDateRef(JNIEnv * env, jobject _this)
{
	jdouble ret = 0.0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_DATE|DT_BYREF)) 
		{
			ret = (jdouble)*di->pdate;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jstring JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getStringRef(JNIEnv * env, jobject _this)
{
	jstring ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_SSTR|DT_BYREF)) 
		{
			SString *ss = di->psstringVal;
			ret = convert(env, *ss, SafeStringLen(*ss));
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_DataItemClear(JNIEnv * env, jobject _this)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}

}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_clone(JNIEnv * env, jobject _this)
{
	jobject newDI = 0;
	/*
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		jclass daClass = env->GetObjectClass(_this);
		jmethodID daCons = env->GetMethodID(daClass, "<init>", "()V");
		newDI = env->NewObject(daClass, daCons);
		jfieldID jf = env->GetFieldID(daClass, "dataitem", "J");
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	*/
	return newDI;
}
	
JNIEXPORT jstring JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toString(JNIEnv * env, jobject _this)
{
	jstring ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		wchar_t * buffer = DataItemtoString(di);
		if(buffer!=0)
		{
			ret = convert(env, buffer, wcslen(buffer));
			delete buffer;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getInt(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==DT_I4) 
		{
			ret = (jint)di->lVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getDate(JNIEnv * env, jobject _this)
{
	jdouble ret = 0.0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==DT_DATE) 
		{
			ret = (jdouble)di->date;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putInt(JNIEnv * env, jobject _this, jint i)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_I4;
		di->lVal=(int)i;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDate(JNIEnv * env, jobject _this, jdouble date)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_DATE;
		di->date=date;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jbyte JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toByte(JNIEnv * env, jobject _this)
{
	jbyte ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di,DT_UI1)) 
		{
			ThrowFastCacheException(env,"DataItemChangeType failed",0);
		}
		ret = di->bVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getBoolean(JNIEnv * env, jobject _this)
{
	jboolean ret = false;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_BOOL)) 
		{
			ret = di->boolVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jbyte JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getByte(JNIEnv * env, jobject _this)
{
	jbyte ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_UI1)) 
		{
			ret = (jbyte)di->bVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putBoolean(JNIEnv * env, jobject _this, jboolean b)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_BOOL;
		di->boolVal = (b == JNI_TRUE ? true : false);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putByte(JNIEnv * env, jobject _this, jbyte b)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_UI1;
		di->bVal=b;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toError(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di,DT_ERROR)) 
		{
			ThrowFastCacheException(env,"DataItemChangeType failed", 0);
		}
		ret = *di->pscode;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toObject(JNIEnv * env, jobject _this)
{
	try
	{
		ThrowFastCacheException(env, "toObject not implemeneted", 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return 0;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getEmpty(JNIEnv * env, jobject _this)
{
	try
	{
		ThrowFastCacheException(env, "getEmpty not implemeneted", 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putEmpty(JNIEnv * env, jobject _this)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt = DT_EMPTY;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jlong JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getError(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt == DT_ERROR) 
		{
			ret = (jlong)di->pscode;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putError(JNIEnv * env, jobject _this, jint i)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_ERROR;
		di->scode=(int)i;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}


JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getDouble(JNIEnv * env, jobject _this)
{
	jdouble ret = 0.0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==DT_R8) 
		{
			ret = (jdouble)di->dblVal;
		}	
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getObject(JNIEnv * env, jobject _this)
{
	try
	{
		ThrowFastCacheException(env, "getObject not implemeneted", 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return 0;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDouble(JNIEnv * env, jobject _this, jdouble d)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_R8;
		di->dblVal=(double)d;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putFloatRef(JNIEnv * env, jobject _this, jfloat val)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		float * pf = new float;
		*pf=val;
		di->dt=DT_R4|DT_BYREF;
		di->pfltVal=pf;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putErrorRef(JNIEnv * env, jobject _this, jint i)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		di->dt=DT_ERROR|DT_BYREF;
		di->scode=(int)i;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putBooleanRef(JNIEnv * env, jobject _this, jboolean b)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		bool * br = new bool;
		*br = b ? true : false;
		di->dt=DT_BOOL|DT_BYREF;
		di->pboolVal=br;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putByteRef(JNIEnv * env, jobject _this, jbyte b)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
		unsigned char * br = new unsigned char;
		*br=b;
		di->dt=DT_UI1|DT_BYREF;
		di->pbVal=br;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}

}

JNIEXPORT jstring JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getString(JNIEnv * env, jobject _this)
{
	jstring ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==DT_SSTR) 
		{
			ret = convert(env, di->sstringVal, SafeStringLen(di->sstringVal));
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putString(JNIEnv * env, jobject _this, jstring s)
{
	try
	{
		if(s!=0)
		{
			DATAITEM*di=getDataItem(env,_this);
			DataItemClear(di);
			di->dt = DT_SSTR;
			std::wstring str = convert(env, s);
			di->dt = DT_SSTR;
			di->sstringVal = SafeString(str.c_str());
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jfloat JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getFloatRef(JNIEnv * env, jobject _this)
{
	jfloat ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_R4|DT_BYREF)) 
		{
			ret = (jfloat)*di->pfltVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getErrorRef(JNIEnv * env, jobject _this)
{
	jint ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_ERROR|DT_BYREF)) 
		{
			ret = (jint)di->scode;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}


JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getBooleanRef(JNIEnv * env, jobject _this)
{
	jboolean ret = false;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_BOOL|DT_BYREF)) 
		{
			ret = *di->pboolVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getObjectRef(JNIEnv * env, jobject _this)
{
	try
	{
		ThrowFastCacheException(env, "getObjectRef not implemeneted", 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return 0;
}


JNIEXPORT jbyte JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getByteRef(JNIEnv * env ,jobject _this)
{
	jbyte ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_UI1|DT_BYREF)) 
		{
			ret = *di->pbVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jfloat JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toFloat(JNIEnv * env, jobject _this)
{
	jfloat ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di, DT_R4)) 
		{
			ThrowFastCacheException(env,"DataItemChangeType failed",0);
		}
		ret = di->fltVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toDataArray(JNIEnv * env, jobject _this)
{
	jobject da = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if((di->dt & DT_ARRAY) == 0) 
		{
			ThrowFastCacheException(env,"DataItem not array",-1);
		}
		da = env->NewObject(dataArrayClass,dataArrayCons);
		jfieldID jf = env->GetFieldID( dataArrayClass,"dataarray","J");
		env->SetLongField(da,jf,(jlong)di->parray);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return da;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDataArrayRef(JNIEnv * env, jobject _this, jobject da)
{
	try
	{
		if(da!=0)
		{
			DATAARRAY * pda = getDataArray(env, da);
			DATAITEM * di = getDataItem(env, _this);
			DATATYPE dt = pda->dt;
			DATAARRAY ** da = new DATAARRAY*;
			*da=pda;
			DataArrayGetDatatype(pda, &dt);
			di->dt=DT_ARRAY | dt | DT_BYREF;
			di->pparray=da;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDataArray(JNIEnv * env, jobject _this, jobject da)
{
	try
	{
		if(da!=0)
		{
			DATAARRAY * pda=getDataArray(env,da);
			DATAITEM * di=getDataItem(env,_this);
			DATATYPE dt;
			DataArrayGetDatatype(pda,&dt);
			di->dt=DT_ARRAY | dt;
			di->parray=pda;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}


JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_noParam(JNIEnv * env, jobject _this)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		di->dt=DT_ERROR;
		di->scode=RET_PARAMNOTFOUND;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jfloat JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getFloat(JNIEnv * env, jobject _this)
{
	jfloat ret = 0.0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di->dt==(DT_R4)) 
		{
			ret = di->fltVal;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putFloat(JNIEnv * env, jobject _this, jfloat val)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di!=0)
		{
			DataItemClear(di);
			di->dt=DT_R4;
			di->fltVal=val;
		}
		else
		{
			ThrowFastCacheException(env, "Cache corrupted error DataItem getting dataItem outFloat", -1);
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{	
		ThrowFastCacheException(env, "Cache corrupted error DataItem  setting float", -1);
	} 

}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_changeType(JNIEnv * env, jobject _this,jshort t)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di!=0) 
		{
			if(!DataItemChangeType(di,t))
			{
				ThrowFastCacheException(env,"DataItemChangeType failed",0);
			}
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getvt(JNIEnv * env, jobject _this)
{
	jshort ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(di!=0) 
		{
			ret = di->dt;
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_toShort(JNIEnv * env, jobject _this)
{
	jshort ret = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		if(!DataItemChangeType(di,DT_I2)) 
		{
			ThrowFastCacheException(env,"DataItemChangeType failed",0);
		}
		ret = di->iVal;
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return ret;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_putDataArrayRefHelper(JNIEnv * env, jobject _this,jint pDA)
{
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		DataItemClear(di);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataItem_getDataArray(JNIEnv * env, jobject _this)
{
	jobject value = 0;
	try
	{
		DATAITEM * di = getDataItem(env, _this);
		value = env->NewObject(dataArrayClass, dataArrayCons);

		setDataArray(env, value, di->parray);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{	
		ThrowFastCacheException(env, "Cache corrupted error DataItem getDataArray", -1);
	} 
	return value;
}
