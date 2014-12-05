#include <wchar.h>
#include "DataItem.h"

void DataItemInit(DATAITEM * di)
{
	di->dt = DT_EMPTY;
	DECIMAL_SETZERO(di->decVal);
	di->byref = 0;
}

void DataItemClear(DATAITEM * di)
{
	if( (di->dt & DT_ARRAY ) == DT_ARRAY )
	{
		if(di->parray!=0)
			DataArrayFree(di->parray);
		//delete di->parray;
	}
	else
	{
		switch(di->dt)
		{
		case DT_EMPTY:
		case DT_NULL:
			break;
		case DT_I2:
			di->iVal=0;
			break;
		case DT_I4:
			di->intVal=0;
			break;
		case DT_R4:
			di->fltVal=0;
			break;
		case DT_R8:
			di->dblVal=0.0;
			break;
		case DT_DATE:
			di->date=0;
			break;
		case DT_ERROR:
			di->scode=0;
			break;
		case DT_BOOL:
			di->boolVal=false;
			break;
		case DT_DECIMAL:
			di->pdecVal=0;
			break;
		case DT_I1:
			di->cVal=0;
			break;
		case DT_UI1:
			di->bVal=0;
			break;
		case DT_UI2:
			di->uintVal=0;
			break;
		case DT_UI4:
			di->ulVal=0;
			break;
		case DT_I8:
			di->llVal=0;
			break;
		case DT_UI8:
			di->ullVal=0;
			break;
		case DT_INT:
			di->intVal=0;
			break;
		case DT_UINT:
			di->uintVal=0;
			break;
		case DT_VOID:
			di->byref=0;
			break;
		case DT_LONG:
			di->scode=0;
			break;
		case DT_SSTR:
			if(di->sstringVal!=0) {
				SafeStringFree(di->sstringVal);
				di->sstringVal = 0;
			}
			break;
		case DT_PTR:
			//delete di->byref;
			di->byref=0;
			break;
		case DT_LPSTR:
			if(di->pcVal!=0)
			{
				delete di->pcVal;
				di->pcVal=0;
			}
			break;
		case DT_INT_PTR:
			if(di->pintVal!=0)
			{
				delete di->pintVal;
				di->pintVal = 0;
			}
			break;
		case DT_UINT_PTR:
			if(di->puintVal!=0)
			{
				delete di->puintVal;
				di->puintVal = 0;
			}
			break;
		//case DT_FILETIME:
		//	break;
		case DT_BYREF:
			break;
		default:
			break;
		}
	}
	di->dt = DT_EMPTY;
}

unsigned long DataItemGetItemSize(DATAITEM * pData)
{
	unsigned long bytesUsed = sizeof(DATAITEM);
	bytesUsed += GetSizeOfItem(pData);
	return bytesUsed;
}

unsigned long GetSizeOfItem(DATAITEM * pData)
{
	unsigned long bytesUsed = 0;

	DATATYPE vartype = pData->dt;

	// Case SString
	if( vartype == DT_SSTR )
	{
		bytesUsed += (SafeStringByteLen(pData->sstringVal) + 2);
	}
	// Case Array
	else if((vartype & DT_ARRAY) == DT_ARRAY)
	{
		DATAARRAY * psaSource = pData->parray;
		bytesUsed += sizeof( DATAARRAY );
		bytesUsed += psaSource->cbElements * psaSource->cElements;

		if( (vartype & ~DT_ARRAY) == DT_SSTR)
		{
			SString * theString;
			for(unsigned long i = 0; i < psaSource->cElements; i++)
			{
				DataArrayPtrOfIndex(psaSource, &i, (void**)&theString);
				if(*theString==0)
					continue;
				bytesUsed += (SafeStringByteLen(*theString) + 2);
			}
		}
		else if((vartype & ~DT_ARRAY) == DT_DATAITEM)
		{
			DATAITEM * theDataItem=0;
			for(unsigned long i = 0; i < psaSource->cElements; i++)
			{
				DataArrayPtrOfIndex(psaSource, &i, (void**)&theDataItem);
				if( theDataItem==0)
					break;
				bytesUsed += GetSizeOfItem(theDataItem);
			}
		}
	}
	return bytesUsed;
}

bool DataItemChangeType(DATAITEM * source, DATATYPE dt) 
{

	if(source->dt==dt)
	{
		return true;
	}

	if((source->dt & DT_ARRAY ) == DT_ARRAY) 
	{
		DATAARRAY * da = source->parray;
		if(da->dt==dt)
		{
			return true;
		}
	}

	switch(source->dt)
	{
	case DT_EMPTY:
	case DT_NULL:
		break;
	case DT_I2:
		break;
	case DT_I4:
		break;
	case DT_R4:
		break;
	case DT_R8:
		break;
	case DT_DATE:
		break;
	case DT_SSTR:
		break;
	case DT_ERROR:
		break;
	case DT_BOOL:
		break;
	case DT_DATAITEM:
		break;
	case DT_DECIMAL:
		break;
	case DT_I1:
		break;
	case DT_UI1:
		break;
	case DT_UI2:
		break;
	case DT_UI4:
		break;
	case DT_I8:
		break;
	case DT_UI8:
		break;
	case DT_INT:
		break;
	case DT_UINT:
		break;
	case DT_VOID:
		break;
	case DT_LONG:
		break;
	case DT_PTR:
		break;
	case DT_LPSTR:
		break;
	case DT_INT_PTR:
		break;
	case DT_UINT_PTR:
		break;
	//case DT_FILETIME:
	//	break;
	default:
		break;
	}

	return true;
}

void DataItemCopy(DATAITEM * dest, const DATAITEM * source)
{
	dest->dt = source->dt;

	if( (source->dt & DT_ARRAY ) == DT_ARRAY )
	{
		dest->parray = DataArrayCopy(source->parray);
	}
	else
	{
		switch(source->dt)
		{
		case DT_EMPTY:
		case DT_NULL:
			break;
		case DT_I2:
			dest->iVal=source->iVal;
			break;
		case DT_I4:
			dest->intVal=source->intVal;
			break;
		case DT_R4:
			dest->fltVal=source->fltVal;
			break;
		case DT_R8:
			dest->dblVal=source->dblVal;
			break;
		case DT_DATE:
			//di->date=source->date;
			break;
		case DT_SSTR:
			dest->sstringVal = SafeString(source->sstringVal);
			break;
		case DT_ERROR:
			dest->scode=source->scode;
			break;
		case DT_BOOL:
			dest->boolVal=source->boolVal;
			break;
		case DT_DATAITEM:
			dest->pvarData = source->pvarData;
			break;
		case DT_DECIMAL:
			dest->pdecVal = source->pdecVal;
			break;
		case DT_I1:
			dest->cVal=source->cVal;
			break;
		case DT_UI1:
			dest->bVal=source->bVal;
			break;
		case DT_UI2:
			dest->uintVal=source->uintVal;
			break;
		case DT_UI4:
			dest->ulVal=source->ulVal;
			break;
		case DT_I8:
			dest->llVal=source->llVal;
			break;
		case DT_UI8:
			dest->ullVal=source->ullVal;
			break;
		case DT_INT:
			dest->intVal=source->intVal;
			break;
		case DT_UINT:
			dest->uintVal=source->uintVal;
			break;
		case DT_LONG:
			dest->scode=source->scode;
			break;
		case DT_PTR:
			break;
		case DT_LPSTR:
			strcpy(dest->pcVal, source->pcVal);
			break;
		case DT_INT_PTR:
			dest->pintVal = source->pintVal;
			break;
		case DT_UINT_PTR:
			if(source->puintVal!=0)
			{
				delete source->puintVal;
			}
			dest->puintVal = source->puintVal;
			break;
		//case DT_FILETIME:
		//	break;
		default:
			break;
		}
	}	
}

unsigned long DataItemGetSize(DATATYPE dt) 
{
	unsigned long size = 0;

	switch(dt)
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
	case DT_DATAITEM:
		size = sizeof(DATAITEM);
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
	case DT_ARRAY:
		size = sizeof(DATAARRAY *);
		break;
	case DT_BYREF:
	case DT_PTR:
	case DT_LPSTR:
	case DT_INT_PTR:
	case DT_UINT_PTR:
	case DT_DOUBLE_PTR:
	case DT_VECTOR:
	default:
		size = sizeof(void *);
		break;
	}

	return size;
}

int DataItemUnMarshal(unsigned char * dest, DATAITEM * pData)
{
	int bytesUsed = sizeof(DATAITEM);
	memcpy(dest, pData, sizeof(DATAITEM));
	dest += sizeof(DATAITEM);
	bytesUsed += writeDataItemData( dest, pData );

	return bytesUsed;
}

/********************************************************
 * Writes the data contained in the variant pData to the buffer dest.
 * Returns the number of bytes written to buffer.
 * Note: in certain cases this method is recursive.
 */
int writeDataItemData(unsigned char * dest, DATAITEM * pData)
{
	long bytesUsed = 0;
	long sizOfData = 0;
	DATATYPE vartype = pData->dt;

	// Case SString
	if( vartype == DT_SSTR )
	{
		sizOfData = writeBstrImage(dest, pData->sstringVal);
		dest += sizOfData;
		bytesUsed += sizOfData;
	}
	// Case Array
	else if((vartype & DT_ARRAY) == DT_ARRAY)
	{
		DATAARRAY * psaSource = pData->parray;
		DATAARRAY * psaDescript = DataArrayAllocDescriptor();
      
		// Copy attributes of source SA
		psaDescript->cbElements   = psaSource->cbElements;      
		psaDescript->cLocks       = 0;
		psaDescript->pvData       = NULL;
      
		// Write SA descriptor to destination
		memcpy(dest, psaDescript, sizeof(DATAARRAY));
		dest += sizeof( DATAARRAY );
		bytesUsed += sizeof( DATAARRAY );
		DataArrayDestroyDescriptor( psaDescript );
      
		void * pData;
		unsigned long bufferSize = psaSource->cbElements * psaSource->cElements;
		// Get a pointer to the source SA data
		DataArrayAccessData( psaSource, &pData );
      
		// Write the data from the source SA to the storage medium
		memcpy(dest, pData, bufferSize);
		dest += bufferSize;
		bytesUsed += bufferSize;
		DataArrayUnaccessData( psaSource ); 
      
		if( (vartype & ~DT_ARRAY) == DT_SSTR)
		{
			SString * theString;                
			for(unsigned long i = 0; i < psaSource->cElements; i++)
			{
				DataArrayPtrOfIndex(psaSource, &i, (void**)&theString);
				if(*theString==0)
					continue;         
				sizOfData = writeBstrImage(dest, *theString);
				dest += sizOfData;
				bytesUsed += sizOfData;
			}
		}
		else if((vartype & ~DT_ARRAY) == DT_DATAITEM)
		{
			DATAITEM * theDataItem=0;
			for(unsigned long i = 0; i < psaSource->cElements; i++)
			{
				DataArrayPtrOfIndex(psaSource, &i, (void**)&theDataItem);
				if( theDataItem==0)
					break;         
				sizOfData = writeDataItemData(dest, theDataItem);
				dest += sizOfData;
				bytesUsed += sizOfData;
			}
		}
	}
	return bytesUsed;
}

/************************************************************
 * Writes the binary image of theString to the buffer dest.
 * Returns the number of bytes written to the buffer.
 */
int writeBstrImage(unsigned char * dest, SString theString)
{
	unsigned short * bp;
	unsigned long len;

	len = SafeStringByteLen(theString);
	bp  = (unsigned short *)theString;
	
	// Write the length of the SString to the stream
	*(unsigned long*)dest = len;
	dest += sizeof(unsigned short);
	
	// Write the characters and the null chars to the stream
	memcpy(dest, bp, len + 2);

	return (sizeof(unsigned short) + len + 2);
}

#define BUF_SIZE 100
wchar_t * DataItemtoString(DATAITEM * di) 
{
	wchar_t * buffer = 0;
	if((di->dt & DT_ARRAY ) == DT_ARRAY) 
	{
		buffer = DataArraytoString(di->parray);
	}
	else if(di->dt==DT_SSTR && di->sstringVal !=0 ) 
	{
		buffer = SafeStringToString(di->sstringVal);
	}
	else if(di->dt==DT_LPSTR)
	{
		int size = strlen(di->pcVal);
		buffer = new wchar_t[size+1];
		size = mbstowcs(buffer, di->pcVal, size);
	}
	else
	{
		buffer = new wchar_t[BUF_SIZE+1];
		memset(buffer, 0x00, BUF_SIZE+1);
		switch(di->dt)
		{
		case DT_EMPTY:
		case DT_NULL:
			break;
		case DT_I2:
			swprintf(buffer, BUF_SIZE, L"%d", di->iVal);
			break;
		case DT_I4:
			swprintf(buffer, BUF_SIZE, L"%d", di->lVal);
			break;
		case DT_R4:
			swprintf(buffer, BUF_SIZE, L"%8.6f", di->fltVal);
			break;
		case DT_R8:
			swprintf(buffer, BUF_SIZE, L"%12.6f", di->dblVal);
			break;
		case DT_DATE:
			break;
		case DT_ERROR:
			swprintf(buffer, BUF_SIZE, L"%d", di->scode);
			break;
		case DT_BOOL:
			wcscpy(buffer, di->boolVal ? L"TRUE" : L"FALSE");
			break;
		case DT_DECIMAL:
			swprintf(buffer, BUF_SIZE, L"%12.6f", di->decVal);
			break;
		case DT_I1:
			swprintf(buffer, BUF_SIZE, L"%d", di->bVal);
			break;
		case DT_UI1:
			swprintf(buffer, BUF_SIZE, L"%u", di->uiVal);
			break;
		case DT_UI2:
			swprintf(buffer, BUF_SIZE, L"%u", di->uiVal);
			break;
		case DT_UI4:
			swprintf(buffer, BUF_SIZE, L"%u", di->ulVal);
			break;
		case DT_I8:
			swprintf(buffer, BUF_SIZE, L"%d", di->llVal);
			break;
		case DT_UI8:
			swprintf(buffer, BUF_SIZE, L"%d", di->ullVal);
			break;
		case DT_INT:
			swprintf(buffer, BUF_SIZE, L"%d", di->intVal);
			break;
		case DT_UINT:
			swprintf(buffer, BUF_SIZE, L"%d", di->uintVal);
			break;
		case DT_VOID:
			break;
		case DT_LONG:
			swprintf(buffer, BUF_SIZE, L"%d", di->lVal);
			break;
		case DT_PTR:
			break;
		case DT_INT_PTR:
			break;
		case DT_UINT_PTR:
			break;
		//case DT_FILETIME:
		//	break;
		default:
			break;
		}
	}
	return buffer;
}

