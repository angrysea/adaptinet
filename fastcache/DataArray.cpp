#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "SafeString.h"
#include "DataArray.h"
#include "DataItem.h"

#define ARRAYBUFFERSIZE 4

DATAARRAY * DataArray(DATATYPE dt, unsigned long cElements)
{
	DATAARRAY * da = new DATAARRAY;
	da->dt = dt;
    da->cbElements = DataItemGetSize(dt);
	da->cElements = cElements;
    da->cLocks = 0;
	unsigned long size = da->cElements*da->cbElements;
	da->pvData = malloc(size+ARRAYBUFFERSIZE);
	memset(da->pvData, 0x00, size+ARRAYBUFFERSIZE);
	return da;
}

DATAARRAY * DataArrayAllocDescriptor()
{
	DATAARRAY * da = new DATAARRAY;
	da->cElements = 0;
    da->cLocks = 0;
	da->pvData = 0;
	return da;
}
DATAARRAY * DataArrayCopy(const DATAARRAY * aCopy)
{
	DATAARRAY * da = new DATAARRAY;
	da->dt = aCopy->dt;
    da->cbElements = aCopy->dt;
	da->cElements = aCopy->cElements;
    da->cLocks = 0;
	unsigned long size = da->cElements*da->cbElements;
	da->pvData = malloc(size+ARRAYBUFFERSIZE);
	memset(da->pvData, 0x00, size+ARRAYBUFFERSIZE);

	switch(da->dt)
	{
	case DT_EMPTY:
	case DT_NULL:
		break;
	case DT_I2:
	case DT_I4:
	case DT_R4:
	case DT_R8:
	case DT_ERROR:
	case DT_BOOL:
	case DT_DECIMAL:
	case DT_I1:
	case DT_UI1:
	case DT_UI2:
	case DT_UI4:
	case DT_I8:
	case DT_UI8:
	case DT_INT:
	case DT_UINT:
	case DT_VOID:
	case DT_LONG:
	case DT_DATE:
		memcpy(da->pvData, aCopy->pvData, size);
		break;
	case DT_SSTR:
	case DT_DATAITEM:
	{
		for(unsigned long i = 0; i < da->cElements; i++)
		{
			void * src = ((unsigned char *)aCopy->pvData) + (aCopy->cbElements * i);
			void * dest = ((unsigned char *)da->pvData) + (da->cbElements * i);

			switch(da->dt)
			{
			case DT_SSTR:
				*((SString *)dest) = SafeStringCopy(*(SString*)src);
				break;
			case DT_DATAITEM:
			{
				DATAITEM * di = *(DATAITEM **)src;
				if(di!=0) 
				{
					*(DATAITEM **)dest = new DATAITEM;
					DataItemInit(*(DATAITEM **)dest);
					DataItemCopy(*(DATAITEM **)dest, di);
				}
				break;
			}
			}
		}
		break;
	}
	case DT_PTR:
	case DT_LPSTR:
	case DT_INT_PTR:
	case DT_UINT_PTR:
	case DT_DOUBLE_PTR:
		break;
	}
	return da;
}

void DataArrayFree(DATAARRAY * da)
{
	if(da->pvData==0) 
	{
		da->cElements=0;
	}

	for(unsigned long i = 0; i < da->cElements; i++)
	{
		void * src = ((unsigned char *)da->pvData) + (da->cbElements * i);
		switch(da->dt)
		{
		case DT_SSTR:
			SafeStringFree(*(SString*)src);
			break;
		case DT_DATAITEM:
		{
			DATAITEM * di = *(DATAITEM **)src;
			if(di!=0) 
			{
				DataItemClear((DATAITEM*)di);
				delete di;
			}
			break;
		}

		case DT_PTR:
		case DT_LPSTR:
		case DT_INT_PTR:
		case DT_UINT_PTR:
		case DT_DOUBLE_PTR:
			delete [] src;
			break;
		}
	}

	if(da->pvData!=0 && da->cElements)
	{
		free(da->pvData);
		da->pvData = 0;
		da->cElements = 0;
	}
}

void DataArrayDestroyDescriptor(DATAARRAY * da)
{
	if(da->pvData!=0)
	{
		free(da->pvData);
	}
	delete da;
}

unsigned long DataArrayGetElemsize(DATAARRAY * da) 
{
	return DataItemGetSize(da->dt);
}

void DataArrayLock(DATAARRAY * da)
{
	da->cLocks++;
}

void DataArrayUnlock(DATAARRAY * da)
{
	da->cLocks--;
}

void DataArrayAccessData(DATAARRAY * da, void ** ppvData)
{
	 DataArrayLock(da);
	 *ppvData = da->pvData;
}

void DataArrayUnaccessData(DATAARRAY * da)
{
	 DataArrayUnlock(da);
}

void DataArrayGetElement(DATAARRAY * da, unsigned long * rgIndices, void * pv)
{
	void * src = ((unsigned char *)da->pvData) + (da->cbElements * *rgIndices);
	
	switch(da->dt)
	{
	case DT_EMPTY:
	case DT_NULL:
		break;
	case DT_I2:
	case DT_I4:
	case DT_R4:
	case DT_R8:
	case DT_ERROR:
	case DT_BOOL:
	case DT_DECIMAL:
	case DT_I1:
	case DT_UI1:
	case DT_UI2:
	case DT_UI4:
	case DT_I8:
	case DT_UI8:
	case DT_INT:
	case DT_UINT:
	case DT_VOID:
	case DT_LONG:
	case DT_DATE:
		memcpy(pv, src, da->cbElements);
		break;
	case DT_SSTR:
		*((SString *)pv) = SafeStringCopy(*(SString*)src);
		break;
	case DT_DATAITEM:
	{
		DATAITEM * di = *(DATAITEM **)src;
		if(di!=0) 
		{
			DataItemCopy((DATAITEM *)pv, di);
		}
		break;
	}

	case DT_PTR:
	case DT_LPSTR:
	case DT_INT_PTR:
	case DT_UINT_PTR:
	case DT_DOUBLE_PTR:
	default:
		memcpy(pv, src, sizeof(void*));
		break;
	}
}

long DataArrayPutElement(DATAARRAY * da, unsigned long * rgIndices, void * pv)
{
	if(da->cLocks!=0)
		return RET_ERROR;

	if(da->cElements<=*rgIndices)
		return RET_ERROR;

	void * dest = ((unsigned char *)da->pvData) + (da->cbElements* *rgIndices);

	switch(da->dt)
	{
	case DT_EMPTY:
	case DT_NULL:
		break;
	case DT_I2:
	case DT_I4:
	case DT_R4:
	case DT_R8:
	case DT_ERROR:
	case DT_BOOL:
	case DT_DECIMAL:
	case DT_I1:
	case DT_UI1:
	case DT_UI2:
	case DT_UI4:
	case DT_I8:
	case DT_UI8:
	case DT_INT:
	case DT_UINT:
	case DT_VOID:
	case DT_LONG:
	case DT_DATE:
		memcpy(dest, pv, da->cbElements);
		break;
	case DT_SSTR:
	{
		*((SString *)dest) = SafeStringCopy(*(SString*)pv);
		break;
	}
	case DT_DATAITEM:
	{
		DATAITEM * di = new DATAITEM;
		DataItemClear(di);
		DataItemCopy(di, (const DATAITEM *)pv);
		*(DATAITEM **)dest = di;
		break;
	}
	case DT_PTR:
	case DT_LPSTR:
	case DT_INT_PTR:
	case DT_UINT_PTR:
	case DT_DOUBLE_PTR:
	default:
		memcpy(dest, pv, sizeof(void*));
		break;
	}

	return RET_OK;
}

void DataArrayGetDatatype(DATAARRAY * da, DATATYPE * pvt)
{
	*pvt=da->dt;
}

long DataArrayPtrOfIndex(DATAARRAY * da, unsigned long * rgIndices, void ** ppvData)
{
	void * pv = ((unsigned char *)da->pvData) + (da->cbElements* *rgIndices);
	*ppvData = &pv;

	return RET_ERROR;
}

void DataArrayGetElements(DATAARRAY * da, unsigned long * lElements)
{
	*lElements = da->cElements;
}

wchar_t * DataArraytoString(DATAARRAY * da)
{
	wchar_t * buffer = 0;

	return buffer;
}

unsigned long DataArrayGetSize(DATAARRAY * da) 
{
	unsigned long size = 0;

	if(da->dt == DT_SSTR)
	{      
		SString ss;                
		for(unsigned long i = 0; i < da->cElements; i++)
		{
			ss = (SString)((unsigned char *)da->pvData) + (da->cbElements * i);
			if(ss==0)
				break;                         
			size += SafeStringByteLen(ss);
		}
	}
	else if(da->dt == DT_DATAITEM)
	{      
		DATAITEM * di;                
		for(unsigned long i = 0; i < da->cElements; i++)
		{
			void * src = ((unsigned char *)da->pvData) + (da->cbElements * i);
			di = *(DATAITEM **)src;
			if(di==0)
				break;                         
			size += DataItemGetItemSize(di);
		}
	}
	else
	{
		switch(da->dt)
		{
		case DT_EMPTY:
		case DT_NULL:
			break;
		case DT_I2:
			size = sizeof(short);
			break;
		case DT_I4:
			size = sizeof(int);
			break;
		case DT_R4:
			size = sizeof(float);
			break;
		case DT_R8:
			size = sizeof(double);
			break;
		case DT_DATE:
			size = sizeof(double);
			break;
		case DT_SSTR:
			size = sizeof(wchar_t *);
			break;
		case DT_ERROR:
			size = sizeof(long);
			break;
		case DT_BOOL:
			size = sizeof(int);
			break;
		case DT_DECIMAL:
			size = sizeof(DECIMAL);
			break;
		case DT_I1:
			size = sizeof(char);
			break;
		case DT_UI1:
			size = sizeof(unsigned char);
			break;
		case DT_UI2:
			size = sizeof(unsigned int);
			break;
		case DT_UI4:
			size = sizeof(unsigned long);
			break;
		case DT_I8:
			size = sizeof(int64_t);
			break;
		case DT_UI8:
			size = sizeof(__u64);
			break;
		case DT_INT:
			size = sizeof(int);
			break;
		case DT_UINT:
			size = sizeof(unsigned int);
			break;
		case DT_VOID:
			size = sizeof(void *);
			break;
		case DT_LONG:
			size = sizeof(long);
			break;
		case DT_PTR:
		case DT_LPSTR:
		case DT_INT_PTR:
		case DT_UINT_PTR:
		case DT_DOUBLE_PTR:
		case DT_VECTOR:
		case DT_ARRAY:
		default:
			size = sizeof(void *);
			break;
		}
	}
	return size*da->cElements;
}

