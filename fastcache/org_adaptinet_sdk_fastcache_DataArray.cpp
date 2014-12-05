#include "org_adaptinet_sdk_fastcache_DataArray.h"
#include "DataArray.h"
#include "DataItem.h"
#include "SafeString.h"
#include "JNIException.h"
#include "myenv.h"

DATAARRAY * getDataArray(JNIEnv * env, jobject _this)
{
	DATAARRAY * da = 0;

	jclass c = env->GetObjectClass(_this);
	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c, "dataarray", "J");
		if(field!=0)
		{
			da=(DATAARRAY*)env->GetLongField(_this, field);
		}
	}

	if(da==0)
	{
		ThrowFastCacheException(env, "Corrupted cache server object - underlying dataarray missing.", -1);
	}

	return da;
}



bool setDataArray(JNIEnv * env, jobject _this, DATAARRAY * da)
{
	bool bRet=false;

	jclass c=env->GetObjectClass(_this);

	if(c!=0)
	{
		jfieldID field=env->GetFieldID(c, "dataarray", "J");
		if(field!=0)
		{
			env->SetLongField(_this, field, (jlong)da);
			bRet=true;
		}
	}

	return bRet;

}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_init(JNIEnv *env, jobject _this, jint dt, jlong cel)
{
	try 
	{
		DATAARRAY * da = DataArray(dt,cel);
		setDataArray(env, _this, da);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env,"DataItemChangeType failed",0);
	}
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_clone(JNIEnv *env, jobject _this)
{
	jobject newSA = 0;
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		DATAITEM v1, v2;
		DataItemClear(&v1);
		DataItemClear(&v2);
		v1.dt = DT_ARRAY | dt;
		v1.parray = pda;
		DataItemCopy(&v2, &v1);
		jclass saClass = env->GetObjectClass(_this);
		jmethodID saCons = env->GetMethodID(saClass, "<init>", "()V");
		newSA = env->NewObject(saClass, saCons);
		jfieldID jf = env->GetFieldID(saClass, "dataarray", "J");
		env->SetLongField(newSA, jf, (jlong)v2.parray);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return newSA;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_destroy(JNIEnv *env, jobject _this)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DataArrayFree(pda);
		delete pda;
		jclass saClass = env->GetObjectClass(_this);
		jfieldID jf = env->GetFieldID(saClass, "dataarray", "J");
		env->SetLongField(_this, jf, 0l);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	catch(...)
	{
		ThrowFastCacheException(env,"DataArray Destroy failed",0);
	}
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getvt(JNIEnv *env, jobject _this)
{
	DATATYPE dt;
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DataArrayGetDatatype(pda, &dt);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}

	return (jint)dt;
}

JNIEXPORT jlong JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getElements(JNIEnv *env, jobject _this)
{
	long len = 0;
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DataArrayGetElements(pda, &len);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return len;
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getElemSize(JNIEnv *env, jobject _this)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		return DataArrayGetElemsize(pda);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return 0;
}

static unsigned long numElements(DATAARRAY * pda)
{
	long len = 0;
	try
	{
		DataArrayGetElements(pda, &len);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return len;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromCharArray(JNIEnv *env, jobject _this, jcharArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
	
		jchar *iarr = env->GetCharArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_UI2;
			for(unsigned long i=0;i<len;i++) 
			{
				di.iVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
					return;
				}
			}
		} 
		else if(dt==DT_UI2 || dt==DT_I2) 
		{
			void *pData;
			DataArrayAccessData(pda, &pData);
			memcpy(pData, iarr, len*sizeof(jchar));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from char", 0);
			return;
		}
		env->ReleaseCharArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromIntArray(JNIEnv *env, jobject _this, jintArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
  
		jint *iarr = env->GetIntArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_I4;
			for(unsigned long i=0;i<len;i++) 
			{
				di.lVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
					return;
				}			
			}
		} 
		else if(dt==DT_I4) 
		{
			void *pData;
			DataArrayAccessData(pda, &pData);
			memcpy(pData, iarr, len*sizeof(unsigned long));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from unsigned long", -1);
			return;
		}
		env->ReleaseIntArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromShortArray(JNIEnv *env, jobject _this, jshortArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
	
		jshort *iarr = env->GetShortArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_I2;
			for(unsigned long i=0;i<len;i++) 
			{
				di.iVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
					return;
				}
			}
		} 
		else if(dt==DT_I2) 
		{
			void *pData;
			DataArrayAccessData(pda, &pData);
			memcpy(pData, iarr, len*sizeof(short));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from short", -1);
			return;
		}
		env->ReleaseShortArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromDoubleArray(JNIEnv *env, jobject _this, jdoubleArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
  
		jdouble *iarr = env->GetDoubleArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_R8;
			for(unsigned long i=0;i<len;i++) 
			{
				di.dblVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
				}
			}
		} 
		else if(dt==DT_R8 || dt==DT_DATE) 
		{
			void *pData;
			DataArrayAccessData(pda, &pData);
			memcpy(pData, iarr, len*sizeof(double));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from double", -1);
		}
		env->ReleaseDoubleArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromStringArray(JNIEnv *env, jobject _this, jobjectArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
  
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_SSTR;
			for(unsigned long i=0;i<len;i++) 
			{
				jstring s = (jstring)env->GetObjectArrayElement(a, i);
				const char *str = env->GetStringUTFChars(s, 0);
				SString bs = SafeString(str);
				di.dt = DT_SSTR;
				di.psstringVal = &bs;
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
				}
				env->ReleaseStringUTFChars(s, str);
				DataItemClear(&di);
			}
		} 
		else if(dt==DT_SSTR) 
		{
			for(unsigned long i=0;i<len;i++) 
			{
				jstring s = (jstring)env->GetObjectArrayElement(a, i);
				const char *str = env->GetStringUTFChars(s, 0);
				SString bs = SafeString(str);
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&bs)==RET_ERROR)
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
				env->ReleaseStringUTFChars(s, str);
			}
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from string\n", 0);
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromByteArray(JNIEnv *env, jobject _this, jbyteArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
  
		jbyte *iarr = env->GetByteArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_UI1;
			for(unsigned long i=0;i<len;i++) 
			{
				di.bVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
				}
			}
		}
		else if(dt==DT_UI1 || dt==DT_I1) 
		{
			jbyte *pData;
			DataArrayAccessData(pda, (void **)&pData);
			memcpy(pData, iarr, len*sizeof(jbyte));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from byte", -1);
		}
		env->ReleaseByteArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromFloatArray(JNIEnv *env, jobject _this, jfloatArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
		  jfloat *iarr = env->GetFloatArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_R4;
			for(unsigned long i=0;i<len;i++) 
			{
				di.fltVal = iarr[i];
				unsigned long x = i;
				if(DataArrayPutElement(pda,&x,&di)==RET_ERROR)
				{
					ThrowFastCacheException(env, "dataarray index past end of array", 0);
				}
			}
		} 
		else if(dt==DT_R4) 
		{
			void *pData;
			DataArrayAccessData(pda, &pData);
			memcpy(pData, iarr, len*sizeof(float));
			DataArrayUnaccessData(pda);
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from float", -1);
		}
		env->ReleaseFloatArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromBooleanArray(JNIEnv *env, jobject _this, jbooleanArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}

		jboolean *iarr = env->GetBooleanArrayElements(a, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_BOOL;
			for(unsigned long i=0;i<len;i++) 
			{
				di.bVal = iarr[i] ? 1 : 0;
				unsigned long x = i;
				DataArrayPutElement(pda,&x,&di);
			}
		} 
		else if(dt==DT_BOOL) 
		{
			bool di;
			for(unsigned long i=0;i<len;i++) 
			{
				di = iarr[i] ? 1 : 0;
				unsigned long x = i;
				DataArrayPutElement(pda,&x,&di);
			}
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from boolean", -1);
		}
	
		env->ReleaseBooleanArrayElements(a, iarr, 0);
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_fromDataItemArray(JNIEnv *env, jobject _this, jobjectArray a)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(a);
		if(len > numElements(pda)) 
		{
			len = numElements(pda);
		}
		if(dt==DT_DATAITEM) 
		{
			for(unsigned long i=0;i<len;i++) 
			{
				jobject var = env->GetObjectArrayElement(a, i);
				DATAITEM * di  = getDataItem(env, var);
				unsigned long x = i;
				if(di) DataArrayPutElement(pda,&x,di);
			}
		} 
		else 
		{
			ThrowFastCacheException(env, "dataarray cannot be assigned from dataItem", -1);
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
}

JNIEXPORT jcharArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toCharArray(JNIEnv *env, jobject _this)
{
	jcharArray iarr = 0;
	try
	{
		DATAARRAY *da = getDataArray(env, _this);
		unsigned long num = DataArrayGetElemsize(da);
		DATATYPE dt;
		DataArrayGetDatatype(da, &dt);
	
		if(dt==DT_UI2 || dt==DT_I2) 
		{
			iarr = env->NewCharArray(num);
			void *pData;
			DataArrayAccessData(da, &pData);
			env->SetCharArrayRegion(iarr, 0, num, (jchar *)pData);
			DataArrayUnaccessData(da);
			return iarr;
		} 
		else if(dt==DT_DATAITEM) 
		{
			jcharArray iarr = env->NewCharArray(num);
			DATAITEM di;
			DataItemInit(&di);
			for(unsigned long i=0;i<num;i++) 
			{
				unsigned long ix = i;
				DataArrayGetElement(da, &ix, (void*) &di);
				if(!DataItemChangeType(&di, DT_UI2))
				{
					return 0;
				}
				jchar val = di.uiVal;
				env->SetCharArrayRegion(iarr, i, 1, &val);
			}
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return iarr;
}

JNIEXPORT jintArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toIntArray(JNIEnv *env, jobject _this)
{
	jintArray iarr = 0;
	try
	{
		DATAARRAY * da = getDataArray(env, _this);
		unsigned long num = DataArrayGetElemsize(da);
		DATATYPE dt;
		DataArrayGetDatatype(da, &dt);
		if(dt==DT_I4) 
		{
			iarr = env->NewIntArray(num);
			void *pData;
			DataArrayAccessData(da, &pData);
			env->SetIntArrayRegion(iarr, 0, num, (jint *)pData);
			DataArrayUnaccessData(da);
		} 
		else if(dt==DT_DATAITEM) 
		{
			jintArray iarr = env->NewIntArray(num);
			DATAITEM di;
			DataItemInit(&di);
			for(unsigned long i=0;i<=num;i++) 
			{
				unsigned long ix = i;
				DataArrayGetElement(da, &ix, (void*) &di);
				if(!DataItemChangeType(&di, DT_I4))
				{
					return 0;
				}
				jint val = di.lVal;
				env->SetIntArrayRegion(iarr, i, 1, &val);
			}
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return iarr;
}


JNIEXPORT jshortArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toShortArray(JNIEnv *env, jobject _this)
{
	jshortArray iarr = 0;
	try
	{
		DATAARRAY *da = getDataArray(env, _this);
		int num = DataArrayGetElemsize(da);
		DATATYPE dt;
		DataArrayGetDatatype(da, &dt);
		if(dt==DT_I2) 
		{
			iarr = env->NewShortArray(num);
			void *pData;
			DataArrayAccessData(da, &pData);
			env->SetShortArrayRegion(iarr, 0, num, (jshort *)pData);
			DataArrayUnaccessData(da);
		} 
		else if(dt==DT_DATAITEM) 
		{
			jshortArray iarr = env->NewShortArray(num);
			DATAITEM di;
			DataItemInit(&di);
			for(int i=0;i<num;i++) 
			{
				unsigned long ix = i;
				DataArrayGetElement(da, &ix, (void*) &di);
				if(!DataItemChangeType(&di, DT_I2))
				{
					return 0;
				}
				jshort val = di.iVal;
				env->SetShortArrayRegion(iarr, i, 1, &val);
			}
		}
	}
	catch(JNIException & ex)
	{
		JNIException::lastError = ex.getErrorCode();
	}
	return iarr;
}

JNIEXPORT jdoubleArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toDoubleArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_R8) 
	{
		jdoubleArray iarr = env->NewDoubleArray(num);
		void *pData;
		DataArrayAccessData(da, &pData);
		env->SetDoubleArrayRegion(iarr, 0, num, (jdouble *)pData);
		DataArrayUnaccessData(da);
		return iarr;
	}
	else if(dt==DT_DATAITEM) 
	{
		jdoubleArray iarr = env->NewDoubleArray(num);
		DATAITEM di;
		DataItemInit(&di);
		for(int i=0;i<=num;i++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_R8))
			{
				return 0;
			}
			jdouble val = di.dblVal;
			env->SetDoubleArrayRegion(iarr, i, 1, &val);
		}
		return iarr;
	}
	return 0;
}

JNIEXPORT jobjectArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toStringArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_DATAITEM) 
	{
		jobjectArray iarr = env->NewObjectArray(num, sClass, 0);
		DATAITEM di;
		DataItemInit(&di);
		for(int i=0;i<num;i++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_SSTR))
			{
				return 0;
			}
			SString bs = di.sstringVal;
			jstring js = env->NewStringUTF(GetAnsiString(bs));
			env->SetObjectArrayElement(iarr, i, js);
		}
		return iarr;
	} 
	else if(dt==DT_SSTR) 
	{
	    jobjectArray iarr = env->NewObjectArray(num, sClass, 0);
	    for(int i=0;i<=num;i++) 
		{
			SString bs = 0;
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &bs);
			jstring js = env->NewStringUTF(GetAnsiString(bs));
			env->SetObjectArrayElement(iarr, i, js);
		}
		return iarr;
	}
	ThrowFastCacheException(env, "dataarray cannot be converted to string[]", 0);
	return 0;
}

JNIEXPORT jbyteArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toByteArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_I1 || dt==DT_UI1) 
	{
		jbyteArray iarr = env->NewByteArray(num);
		jbyte *pData;
		DataArrayAccessData(da, (void **)&pData);
		env->SetByteArrayRegion(iarr, 0, num, pData);
		DataArrayUnaccessData(da);
		return iarr;
	} 
	else if(dt==DT_DATAITEM) 
	{
		jbyteArray iarr = env->NewByteArray(num);
		DATAITEM di;
		DataItemInit(&di);
		for(int i=0,j=0;i<=num;i++,j++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_UI1))
			{
				return 0;
			}
			jbyte val = di.bVal;
			env->SetByteArrayRegion(iarr, j, 1, &val);
		}
		return iarr;
	}
	return 0;
}

JNIEXPORT jfloatArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toFloatArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_R4) 
	{
		jfloatArray iarr = env->NewFloatArray(num);
		void *pData;
		DataArrayAccessData(da, &pData);
		env->SetFloatArrayRegion(iarr, 0, num, (jfloat *)pData);
		DataArrayUnaccessData(da);
		return iarr;
	} 
	else if(dt==DT_DATAITEM) 
	{
		jfloatArray iarr = env->NewFloatArray(num);
		DATAITEM di;
		DataItemInit(&di);
		for(int i=0;i<=num;i++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_R4))
			{
				return 0;
			}
			jfloat val = di.fltVal;
			env->SetFloatArrayRegion(iarr, i, 1, &val);
		}
		return iarr;
	}
	return 0;
}

JNIEXPORT jbooleanArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toBooleanArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_BOOL) 
	{
	    jbooleanArray iarr = env->NewBooleanArray(num);
	    bool di;
	    for(int i=0,j=0;i<num;i++,j++) 
		{
	    	unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			jboolean val = (di == 1 ? JNI_TRUE : JNI_FALSE);
			env->SetBooleanArrayRegion(iarr, j, 1, &val);
		}
		return iarr;
	}
	else if(dt==DT_DATAITEM) 
	{
		jbooleanArray iarr = env->NewBooleanArray(num);
		DATAITEM di;
		DataItemInit(&di);
		for(int i=0;i<=num;i++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_BOOL))
			{
				return 0;
			}
			jboolean val = di.bVal==1 ? JNI_TRUE : JNI_FALSE;
			env->SetBooleanArrayRegion(iarr, i, 1, &val);
		}
		return iarr;
	}
	return 0;
}

JNIEXPORT jobjectArray JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_toDataItemArray(JNIEnv *env, jobject _this)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);

	jobjectArray varr = env->NewObjectArray(num, dataItemClass, 0);
	for(int i=0;i<num;i++) 
	{
		unsigned long ix = i;
	    jobject newDataItem = env->NewObject(dataItemClass, dataItemCons);
	    DATAITEM *di = getDataItem(env, newDataItem);
	    DataArrayGetElement(da, &ix, (void*) di);
		env->SetObjectArrayElement(varr, i, newDataItem);
	}
	return varr;
}


#define GET1DCODE(dtType, dtAccess, jtyp) \
  DATAARRAY *da = getDataArray(env, _this); \
  DATATYPE dt; \
  DataArrayGetDatatype(da, &dt); \
  if(dt==DT_DATAITEM) { \
    DATAITEM di; \
    DataItemInit(&di); \
    DataArrayGetElement(da, (unsigned long *)&idx, (void*) &di); \
    if(!DataItemChangeType(&di, dtType)) { \
      ThrowFastCacheException(env, "DataItemChangeType failed", -1); \
      return 0; \
    } \
    return (jtyp)di.dtAccess; \
  } else if(dt==dtType) { \
    jtyp jc; \
    DataArrayGetElement(da, (unsigned long *)&idx, (void*) &jc); \
    return jc; \
  } else { \
    return 0; \
  }

#define SET1DCODE(dtType, dtAccess) \
  DATAARRAY *da = getDataArray(env, _this); \
  DATATYPE dt; \
  DataArrayGetDatatype(da, &dt); \
  if(dt==DT_DATAITEM) { \
    DATAITEM di; \
    DataItemInit(&di); \
    di.dt = dtType; \
    di.dtAccess = c; \
    DataArrayPutElement(da,(unsigned long *)&idx,&di); \
  } else if(dt==dtType) { \
    DataArrayPutElement(da,(unsigned long *)&idx,&c); \
  } else { \
    ThrowFastCacheException(env, "dataarray type mismatch", -1); \
	return; \
  } \

#define GETARRAYCODE(dtType, varType2, dtAccess, jtyp, jsetArr) \
  DATAARRAY *da = getDataArray(env, _this); \
  DATATYPE dt; \
  DataArrayGetDatatype(da, &dt); \
  if(dt==dtType || dt==varType2) { \
    jtyp *pData; \
    DataArrayAccessData(da, (void **)&pData); \
    env->jsetArr(ja, ja_start, nelem, &pData[idx]); \
    DataArrayUnaccessData(da); \
    return; \
  } else if(dt==DT_DATAITEM) { \
    DATAITEM di; \
    DataItemInit(&di); \
    for(unsigned long i=idx, j=ja_start;i<idx+nelem;i++,j++) { \
      unsigned long ix = i; \
      DataArrayGetElement(da, &ix, (void*) &di); \
      if(!DataItemChangeType(&di, dtType)) { \
        return; \
      } \
      jtyp val = di.dtAccess; \
      env->jsetArr(ja, j, 1, &val); \
    } \
  }

#define SETARRAYCODE(dtType, varType2, dtAccess, jtyp, jgetArr, jrelArr) \
  DATAARRAY * pda = getDataArray(env, _this); \
  DATATYPE dt; \
  DataArrayGetDatatype(pda, &dt); \
  jtyp *iarr = env->jgetArr(ja, 0); \
  if(dt==DT_DATAITEM) { \
    DATAITEM di; \
    DataItemInit(&di); \
    di.dt = dtType; \
    for(unsigned long i=ja_start,j=idx;i<ja_start+nelem;i++,j++) { \
      di.dtAccess = iarr[i]; \
      unsigned long x = j; \
      DataArrayPutElement(pda,&x,&di); \
    } \
  } else if(dt==dtType || dt==varType2) { \
    jtyp *pData; \
    DataArrayAccessData(pda, (void **)&pData); \
    memcpy(&pData[idx], &iarr[ja_start], nelem*sizeof(jtyp)); \
    DataArrayUnaccessData(pda); \
  } else { \
    ThrowFastCacheException(env, "dataarray type mismatch", -1); \
	return; \
  } \
  env->jrelArr(ja, iarr, 0);

JNIEXPORT jchar JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getChar(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_UI2, uiVal, jchar)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setChar(JNIEnv *env, jobject _this, jlong idx, jchar c)
{
	SET1DCODE(DT_UI2, uiVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getChars(JNIEnv *env, jobject _this, jlong idx, jint nelem, jcharArray ja, jint ja_start)
{
	GETARRAYCODE(DT_UI2, DT_I2, uiVal, jchar, SetCharArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setChars(JNIEnv *env, jobject _this, jlong idx, jint nelem, jcharArray ja, jint ja_start)
{
	SETARRAYCODE(DT_UI2, DT_I2, uiVal, jchar, GetCharArrayElements, ReleaseCharArrayElements);
}

JNIEXPORT jint JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getInt(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_I4, lVal, jint)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setInt(JNIEnv *env, jobject _this, jlong idx, jint c)
{
	SET1DCODE(DT_I4, lVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getInts(JNIEnv *env, jobject _this, jlong idx, jint nelem, jintArray ja, jint ja_start)
{
	GETARRAYCODE(DT_I4, DT_I4, lVal, jint, SetIntArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setInts(JNIEnv *env, jobject _this, jlong idx, jint nelem, jintArray ja, jint ja_start)
{
	SETARRAYCODE(DT_I4, DT_I4, lVal, jint, GetIntArrayElements, ReleaseIntArrayElements);
}

JNIEXPORT jshort JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getShort(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_I2, iVal, jshort)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setShort(JNIEnv *env, jobject _this, jlong idx, jshort c)
{
	SET1DCODE(DT_I2, iVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getShorts(JNIEnv *env, jobject _this, jlong idx, jint nelem, jshortArray ja, jint ja_start)
{
	GETARRAYCODE(DT_I2, DT_I2, iVal, jshort, SetShortArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setShorts(JNIEnv *env, jobject _this, jlong idx, jint nelem, jshortArray ja, jint ja_start)
{
	SETARRAYCODE(DT_I2, DT_I2, iVal, jshort, GetShortArrayElements, ReleaseShortArrayElements);
}

JNIEXPORT jdouble JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDouble(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_R8, dblVal, jdouble)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDouble(JNIEnv *env, jobject _this, jlong idx, jdouble c)
{
	SET1DCODE(DT_R8, dblVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDoubles(JNIEnv *env, jobject _this, jlong idx, jint nelem, jdoubleArray ja, jint ja_start)
{
	GETARRAYCODE(DT_R8, DT_R8, dblVal, jdouble, SetDoubleArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDoubles(JNIEnv *env, jobject _this, jlong idx, jint nelem, jdoubleArray ja, jint ja_start)
{
	SETARRAYCODE(DT_R8, DT_R8, dblVal, jdouble,GetDoubleArrayElements, ReleaseDoubleArrayElements);
}

JNIEXPORT jstring JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getString(JNIEnv *env, jobject _this, jlong idx)
{
	DATAARRAY * pda = getDataArray(env, _this);
	DATATYPE dt;
	DataArrayGetDatatype(pda, &dt);
	if(dt==DT_DATAITEM) 
	{
	    DATAITEM di;
	    DataItemInit(&di);
	    DataArrayGetElement(pda, (unsigned long *)&idx, &di);
	    if(!DataItemChangeType(&di, DT_SSTR)) 
		{
			return 0;
		}

		return env->NewStringUTF(GetAnsiString(di.sstringVal));
	} 
	else if(dt==DT_SSTR) 
	{
		SString bs = 0;
		DataArrayGetElement(pda, (unsigned long *)&idx, &bs);
		jstring js = env->NewStringUTF(GetAnsiString(bs));
		return js;
	}
	ThrowFastCacheException(env, "dataarray cannot get string", 0);
	return 0;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getStrings(JNIEnv *env, jobject _this, jlong idx, jint nelem, jobjectArray ja, jint ja_start)
{
	DATAARRAY *da = getDataArray(env, _this);
	int num = DataArrayGetElemsize(da);

	DATATYPE dt; 
	DataArrayGetDatatype(da, &dt); 
	if(dt==DT_DATAITEM) 
	{
		DATAITEM di;
		for(int i=0, j=ja_start;i<num;i++,j++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_SSTR))
			{
				return;
			}
			jstring js = env->NewStringUTF(GetAnsiString(di.sstringVal));
			env->SetObjectArrayElement(ja, j, js);
			DataItemClear(&di);
		} 
	}
	else if(dt==DT_SSTR) 
	{
		SString bs = 0;
		for(int i=idx, j=ja_start;i<idx+nelem;i++,j++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &bs);
			jstring js = env->NewStringUTF(GetAnsiString(bs));
			env->SetObjectArrayElement(ja, j, js);
		} 
	} 
	else 
	{
		ThrowFastCacheException(env, "dataarray cannot get strings", 0);
		return;
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setStrings(JNIEnv *env, jobject _this, jlong idx, jint nelem, jobjectArray ja, jint ja_start)
{
	DATAARRAY * pda = getDataArray(env, _this); 
	DATATYPE dt;
	DataArrayGetDatatype(pda, &dt); 
	unsigned long len = env->GetArrayLength(ja);
	if(len > numElements(pda)) 
	{ 
	    len = numElements(pda); 
	} 
	if(dt==DT_DATAITEM) 
	{
		DATAITEM di;
		DataItemInit(&di);
		for(long i=ja_start,j=idx;i<ja_start+nelem;i++,j++)
		{
			jstring s = (jstring)env->GetObjectArrayElement(ja, i);
			const char *str = env->GetStringUTFChars(s, 0);
			di.dt = DT_SSTR;
			di.sstringVal = SafeString(str);
			unsigned long x = j;
			DataArrayPutElement(pda,&x,&di); 
			DataItemClear(&di);
		} 
	} 
	else if(dt==DT_SSTR) 
	{
		for(long i=ja_start,j=idx;i<ja_start+nelem;i++,j++)
		{
			jstring s = (jstring)env->GetObjectArrayElement(ja, i);
			const char *str = env->GetStringUTFChars(s, 0);
			unsigned long x = j;
			DataArrayPutElement(pda,&x,SafeString(str)); 
		} 
	} 
	else 
	{
		ThrowFastCacheException(env, "dataarray cannot set strings", 0);
		return;
	} 
}

JNIEXPORT jbyte JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getByte(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_UI1, bVal, jbyte)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setByte(JNIEnv *env, jobject _this, jlong idx, jbyte c)
{
	SET1DCODE(DT_UI1, bVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBytes(JNIEnv *env, jobject _this, jlong idx, jint nelem, jbyteArray ja, jint ja_start)
{
	GETARRAYCODE(DT_UI1, DT_I1, bVal, jbyte, SetByteArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBytes(JNIEnv *env, jobject _this, jlong idx, jint nelem, jbyteArray ja, jint ja_start)
{
	SETARRAYCODE(DT_UI1, DT_I1, bVal, jbyte, GetByteArrayElements, ReleaseByteArrayElements);
}

JNIEXPORT jfloat JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getFloat(JNIEnv *env, jobject _this, jlong idx)
{
	GET1DCODE(DT_R4, fltVal, jfloat)
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setFloat(JNIEnv *env, jobject _this, jlong idx, jfloat c)
{
	SET1DCODE(DT_R4, fltVal);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getFloats(JNIEnv *env, jobject _this, jlong idx, jint nelem, jfloatArray ja, jint ja_start)
{
	GETARRAYCODE(DT_R4, DT_R4, fltVal, jfloat, SetFloatArrayRegion);
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setFloats(JNIEnv *env, jobject _this, jlong idx, jint nelem, jfloatArray ja, jint ja_start)
{
	SETARRAYCODE(DT_R4, DT_R4, fltVal, jfloat, GetFloatArrayElements, ReleaseFloatArrayElements);
}

JNIEXPORT jboolean JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBoolean(JNIEnv *env, jobject _this, jlong idx)
{
	DATAARRAY *da = getDataArray(env, _this);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_DATAITEM) 
	{
		DATAITEM di;
		DataItemInit(&di);
		DataArrayGetElement(da, (unsigned long *)&idx, (void*) &di);
		if(!DataItemChangeType(&di, DT_BOOL))
		{
			ThrowFastCacheException(env, "dataarray change type failed", -1);
			return 0;
		}
		jboolean jb = di.boolVal==true ? JNI_TRUE: JNI_FALSE;
		return jb;
	} 
	else if(dt==DT_BOOL) 
	{
		bool vb;
		DataArrayGetElement(da, (unsigned long *)&idx, (void*) &vb);
		jboolean jb = vb==true ? JNI_TRUE: JNI_FALSE;
		return jb;
	} 
	else 
	{
		return 0;
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBoolean(JNIEnv *env, jobject _this, jlong idx, jboolean c)
{
	DATAARRAY *da = getDataArray(env, _this);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_DATAITEM) 
	{
		DATAITEM di;
		DataItemInit(&di);
		di.dt = DT_BOOL;
		di.boolVal = c==JNI_TRUE ? true : false;
		DataArrayPutElement(da,(unsigned long *)&idx,&di);
	} 
	else if(dt==DT_BOOL) 
	{
		bool vb = c==JNI_TRUE ? true : false;
		DataArrayPutElement(da,(unsigned long *)&idx,&vb);
	} 
	else 
	{
		ThrowFastCacheException(env, "dataarray type mismatch", -1);
		return;
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getBooleans(JNIEnv *env, jobject _this, jlong idx, jint nelem, jbooleanArray ja, jint ja_start)
{
	DATAARRAY *da = getDataArray(env, _this);
	unsigned long num = DataArrayGetElemsize(da);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	if(dt==DT_BOOL) 
	{
		bool di;
		for(unsigned long i=0, j=ja_start;i<num;i++,j++) 
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			jboolean val = di==true ? JNI_TRUE : JNI_FALSE;
			env->SetBooleanArrayRegion(ja, j, 1, &val);
		}
	}
	else if(dt==DT_DATAITEM) 
	{
		DATAITEM di;
		DataItemInit(&di);
		for(unsigned long i=idx, j=ja_start;i<idx+nelem;i++,j++)
		{
			unsigned long ix = i;
			DataArrayGetElement(da, &ix, (void*) &di);
			if(!DataItemChangeType(&di, DT_BOOL))
			{
				return;
			}
			jboolean val = di.boolVal==true ? JNI_TRUE : JNI_FALSE;
			env->SetBooleanArrayRegion(ja, j, 1, &val);
		}
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setBooleans(JNIEnv *env, jobject _this, jlong idx, jint nelem, jbooleanArray ja, jint ja_start)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this);
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt);
		unsigned long len = env->GetArrayLength(ja);
		if(len > numElements(pda))
		{
			len = numElements(pda);
		}
		jboolean *iarr = env->GetBooleanArrayElements(ja, 0);
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			di.dt = DT_BOOL;
			for(unsigned long i=ja_start,j=idx;i<ja_start+nelem;i++,j++)
			{
				di.boolVal = iarr[i]==JNI_TRUE ? true : false;
				unsigned long x = j;
				DataArrayPutElement(pda,&x,&di);
			}
		} 
		else if(dt==DT_BOOL) 
		{
			bool di;
			for(unsigned long i=ja_start,j=idx;i<ja_start+nelem;i++,j++)
			{
				di = iarr[i]==JNI_TRUE ? true : false;
				unsigned long x = j;
				DataArrayPutElement(pda,&x,&di);
			}
		}
		else 
		{
			ThrowFastCacheException(env, "dataarray type mismatch", -1);
			return;
		} 
		env->ReleaseBooleanArrayElements(ja, iarr, 0);
	}
	catch(...)
	{	
		ThrowFastCacheException(env, "dataarray error setting dataItem", -1);
	} 
}

JNIEXPORT jobject JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataItem(JNIEnv *env, jobject _this, jlong idx)
{
	DATAARRAY * pda = getDataArray(env, _this);
	DATATYPE dt;
	DataArrayGetDatatype(pda, &dt);
	jobject newDataItem = env->NewObject(dataItemClass, dataItemCons);
	DATAITEM *di = getDataItem(env, newDataItem);
	if(dt==DT_DATAITEM) 
	{
	    DataArrayGetElement(pda, (unsigned long *)&idx, di);
	} 
	else 
	{
		ThrowFastCacheException(env, "dataarray type is not dataItem/dispatch", -1);
	}
	return newDataItem;
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDataItem(JNIEnv *env, jobject _this, jlong idx, jobject s)
{
	DATAARRAY *da = getDataArray(env, _this);
	DATATYPE dt;
	DataArrayGetDatatype(da, &dt);
	DATAITEM *di = getDataItem(env, s);
	if(dt==DT_DATAITEM) 
	{
	    DataArrayPutElement(da,(unsigned long *)&idx,di);
	} 
	else 
	{
		ThrowFastCacheException(env, "dataarray type is not dataItem/dispatch", -1);
	}
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_getDataItems(JNIEnv *env, jobject _this, jlong idx, jint nelem, jobjectArray ja, jint ja_start)
{
	try
	{
		DATAARRAY *da = getDataArray(env, _this);
		unsigned long num = DataArrayGetElemsize(da);
		DATATYPE dt; 
		DataArrayGetDatatype(da, &dt); 
		if(dt==DT_DATAITEM) 
		{
			for(unsigned long i=0, j=ja_start;i<num;i++,j++) 
			{
				unsigned long ix = i;
				jobject newDataItem = env->NewObject(dataItemClass, dataItemCons);
				DATAITEM *di = getDataItem(env, newDataItem);
				DataArrayGetElement(da, &ix, (void*) di);
				env->SetObjectArrayElement(ja, j, newDataItem);
			} 
		}
		else 
		{
			ThrowFastCacheException(env, "dataarray type is not dataItem", -1);
		}
	}
	catch(...)
	{	
		ThrowFastCacheException(env, "dataarray error setting dataItem", -1);
	} 
}

JNIEXPORT void JNICALL Java_org_adaptinet_sdk_fastcache_DataArray_setDataItems(JNIEnv *env, jobject _this, jlong idx, jint nelem, jobjectArray ja, jint ja_start)
{
	try
	{
		DATAARRAY * pda = getDataArray(env, _this); 
		DATATYPE dt;
		DataArrayGetDatatype(pda, &dt); 
		unsigned long len = env->GetArrayLength(ja);
		if(len > numElements(pda)) 
		{ 
			len = numElements(pda); 
		} 
		if(dt==DT_DATAITEM) 
		{
			DATAITEM di;
			DataItemInit(&di);
			for(unsigned long i=ja_start,j=idx;i<ja_start+nelem;i++,j++)
			{
				jobject var = env->GetObjectArrayElement(ja, i);
				DATAITEM *di  = getDataItem(env, var);
				unsigned long x = j;
				DataArrayPutElement(pda,&x,di);
			} 
		}
		else 
		{	
			ThrowFastCacheException(env, "dataarray type is not dataItem", -1);
		} 
	}
	catch(...)
	{	
		ThrowFastCacheException(env, "dataarray error setting dataItem", -1);
	} 
}
