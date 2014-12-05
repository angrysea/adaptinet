#pragma once
#include <jni.h>
#include "types.h"
#include "SafeString.h"

typedef struct tagDATAARRAY
{
    unsigned long cbElements;
    unsigned long cElements;
    unsigned long cLocks;
	DATATYPE dt;
    void * pvData;
} DATAARRAY;

DATAARRAY * DataArray(DATATYPE dt, unsigned long cElements);
DATAARRAY * DataArrayAllocDescriptor();
DATAARRAY * DataArrayCopy(const DATAARRAY & aCopy);
void DataArrayFree(DATAARRAY * da);
void DataArrayDestroyDescriptor(DATAARRAY * da);
void Redim(DATAARRAY * da, unsigned long cElements);
void DataArrayGetElements(DATAARRAY * da, long * lElements);
unsigned long DataArrayGetElemsize(DATAARRAY * da);
void DataArrayLock(DATAARRAY * da);
void DataArrayUnlock(DATAARRAY * da);
void DataArrayAccessData(DATAARRAY * da, void ** ppvData);
void DataArrayUnaccessData(DATAARRAY * da);
void DataArrayGetElement(DATAARRAY * da, unsigned long * rgIndices, void * pv);
long DataArrayPutElement(DATAARRAY * da, unsigned long * rgIndices,  void * pv);
long DataArrayPtrOfIndex(DATAARRAY * da, unsigned long *rgIndices, void ** ppvData);
DATAARRAY * DataArrayCopy(const DATAARRAY * aCopy);
void DataArrayGetDatatype(DATAARRAY * da, DATATYPE * pvt);
unsigned long DataArrayGetSize(DATAARRAY * da);
wchar_t * DataArraytoString(DATAARRAY * da);

DATAARRAY * getDataArray(JNIEnv * env, jobject da);
bool setDataArray(JNIEnv * env, jobject obj, DATAARRAY * pDA);
