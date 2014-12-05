#pragma once
#include <time.h>

#include "types.h"
#include "DataArray.h"
#include "SafeString.h"

typedef struct tagDATAITEM DATAITEM;

struct tagDATAITEM
{
    union 
        {
        struct 
            {
            DATATYPE dt;
            union 
			{
                int64_t llVal;
                long lVal;
                unsigned char bVal;
                short iVal;
                float fltVal;
                double dblVal;
                bool boolVal;
                time_t date;
                SString sstringVal;
                DATAARRAY * parray;
                long scode;
                unsigned char * pbVal;
                short * piVal;
                long * plVal;
                int64_t * pllVal;
                float * pfltVal;
                double * pdblVal;
                bool * pboolVal;
                long * pscode;
                int64_t * pcyVal;
                time_t * pdate;
                SString * psstringVal;
                DATAARRAY ** pparray;
                DATAITEM * pvarData;
                void * byref;
                char cVal;
                unsigned short uiVal;
                unsigned long ulVal;
                __u64 ullVal;
                int intVal;
                unsigned int uintVal;
                DECIMAL * pdecVal;
                char * pcVal;
                unsigned short * puiVal;
                unsigned long * pulVal;
                __u64 * pullVal;
                int * pintVal;
                unsigned int * puintVal;
            };
		};
		DECIMAL decVal;
    };
};

void DataItemInit(DATAITEM * di);
void DataItemClear(DATAITEM * di);
bool DataItemChangeType(DATAITEM * pSource, DATATYPE dt);
unsigned long DataItemGetItemSize(DATAITEM * di);
unsigned long GetSizeOfItem(DATAITEM * pData);
void DataItemCopy(DATAITEM * dest, const DATAITEM * source);
unsigned long DataItemGetSize(DATATYPE dt);
int DataItemUnMarshal(unsigned char * dest, DATAITEM * pData);
int writeDataItemData(unsigned char * dest, DATAITEM * pData );
int writeBstrImage(unsigned char * dest, SString theString);
wchar_t * DataItemtoString(DATAITEM * source);

DATAITEM * getDataItem(JNIEnv * env, jobject obj);
bool setDataItem(JNIEnv * env,jobject obj, DATAITEM * pDataItem);
void ThrowFastCacheException(JNIEnv *env, const char* desc, jint hr);
