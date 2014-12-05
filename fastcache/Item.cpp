#include <cfloat>
#include <cmath>
#include <cwchar>

#include "Item.h"

#define _GOOD_QUALITY 0xc0
#define _BAD_QUALITY 0x00
#define _UNCERTAIN_QUALITY 0x40


Item::Item() :
	m_hServerItem( 0l ),
	m_hClientItem( 0l ),
	m_bActive( false ),
	m_Quality( 0 ),
	m_bDirty( true ),
	m_LastWriteError( RET_OK ),
	m_AsyncMask( 0 ),
	m_vtCanonicalDataType( DT_EMPTY ),
	m_bstrGuid( 0 ),
	m_BeenUpdated( false ),
	m_TimeStamp( 0 )
{
	DataItemInit( &m_varValue );
}

long Item::Init(DATATYPE dt)
{
	long hr = RET_OK;

	try
	{
		DataItemInit( &m_varValue );
		m_vtCanonicalDataType = dt;
		m_Quality = _GOOD_QUALITY;
		time(&m_TimeStamp);
		m_bActive = true;
	}
	catch ( long e )
	{
		hr = e;
	}
	catch ( ... )
	{
		hr = RET_UNEXPECTED;
	}

	return hr;
}

long Item::GetAttributeITEM(ITEMATTRIBUTES * ITEM )
{
	ITEM->bActive = m_bActive;
	ITEM->dwAccessRights = READABLE | WRITEABLE;
	ITEM->dwBlobSize = 0;
	ITEM->pBlob = 0;
	ITEM->vtCanonicalDataType = m_vtCanonicalDataType;
	ITEM->dwEUType = NOENUM;
	ITEM->vEUInfo.dt = DT_EMPTY;
 
	return RET_OK;
}

long Item::IsDirty(bool * bDirtyFlag)
{
	if (m_BeenUpdated == false)
		*bDirtyFlag = false;
	else
		*bDirtyFlag = m_bDirty;

	m_bDirty = false;

	return RET_OK;
}

long Item::SetValue(DATAITEM data)
{
	long hr = RET_OK;

	if ( (isEqual(data, m_varValue) == true ? false : true) )
	{
		m_bDirty = true;
	}

	if (m_bDirty == false && m_BeenUpdated == true)
	{
		return RET_OK;
	}
   
	m_BeenUpdated = true;

	// In this implementation, the canonical data type is meaningless. It can change
	// on the fly what format data for any ITEM is returned in. We must attempt to
	// coerce whatever data type just sent us to the "requested" data type that
	// was specified when this ITEM was added to the group in AddITEMs().   
	try
	{
		if (data.dt != m_varValue.dt && m_varValue.dt != DT_EMPTY && m_vtCanonicalDataType != DT_EMPTY )
		{
			// data types don't match; try to coerce.
			DATAITEM CrashDummy;
			DataItemInit(&CrashDummy);
			DataItemCopy(&CrashDummy, &data);
			hr = DataItemChangeType(&CrashDummy, CrashDummy.dt);
			if (hr != RET_OK)
			{
				m_Quality = _BAD_QUALITY;
				throw hr;
			}
			else
			{
				DataItemClear(&CrashDummy);
				m_Quality = _GOOD_QUALITY;
				DataItemCopy(&m_varValue, &data);
				if (hr != RET_OK)
				{
					m_Quality = _BAD_QUALITY;
					throw hr;
				}
			}
		}
		else 
		{
			DataItemCopy(&m_varValue, &data);
			if (hr == RET_OK)
			{
				m_Quality = _GOOD_QUALITY;
			}
			else
			{
				m_Quality = _BAD_QUALITY;
			}
		}

		// We must set quality to BAD if the value of a
		// floating point number is NaN.
		if (m_vtCanonicalDataType == DT_R4)
		{
			if (isnan(m_varValue.fltVal) == 1)
			{
				m_Quality = _BAD_QUALITY;
			}
		}

		if (m_vtCanonicalDataType == DT_R8)
		{
			if (isnan(m_varValue.dblVal) == 1)
			{
				m_Quality = _BAD_QUALITY;
			}
		}
	}
	catch (long e)
	{
		hr = e;
	}

	time(&m_TimeStamp);

	return hr;
}

long Item::SetDataType(DATATYPE v)
{
	long hr = RET_OK;
   
	if( v != DT_EMPTY )
	{
		if( v != m_vtCanonicalDataType )
		{
			// Attempt to change to requested type
			DATAITEM data;
			DataItemInit( &data );
			hr = DataItemChangeType(&data, v);
			DataItemClear( &data );
		}
	}
	return hr;
}

long Item::GetValue(DATASOURCE ds, DATAITEM * data, unsigned short * qual, time_t * time)
{
	long hr = RET_OK;

	if ( qual != 0) 
	{
		switch(ds)
		{
			case DS_CACHE:
				*qual = m_bActive == true ? m_Quality : QUAL_NOTACTIVE;
				break;

			case DS_DEVICE:
			default:
   				*qual = m_Quality;
				break;
		}
	}

	if( time != 0 ) 
	{
		*time = m_TimeStamp;
	}

	if( data != 0 )
	{
		DataItemCopy(data, &m_varValue);
		if ((data->dt != DT_EMPTY) && (data->dt != m_vtCanonicalDataType))
		{
			hr = DataItemChangeType(data, m_vtCanonicalDataType);

			if ( hr !=RET_OK )
			{
				// This should never occur since m_vtRequested is only set after its
				// value is checked. 
				*qual = 0x00; // bad quality
			}
		}
	}

	return hr;
}

bool Item::isEqual( DATAITEM var1, DATAITEM var2 )
{
	bool bRet = false;
   
	if ( var1.dt != var2.dt )
	{
		return false;
	}

	try
	{
		if ( ( var1.dt & DT_ARRAY ) != 0 || ( var2.dt & DT_ARRAY ) != 0 )
		{
			long nElements1, nElements2;

			DATAARRAY * psaVar1 = D_ARRAY(&var1);
			DATAARRAY * psaVar2 = D_ARRAY(&var2);

			DataArrayGetElements(psaVar1, &nElements1);
			DataArrayGetElements(psaVar2, &nElements2);
			if ( nElements1 != nElements2 )
				throw false;

			DATAITEM * pVarITEM1, * pVarITEM2;
			DataArrayLock( psaVar1 ); 
			DataArrayLock( psaVar2 ); 

			unsigned long index[1];
			for (unsigned long i=0; i<nElements1; i++ )
			{
				index[0] = i;           

				if ( DataArrayPtrOfIndex( psaVar1, index, (void**)&pVarITEM1 )!=RET_OK ) 
				{
					DataArrayUnlock( psaVar1 ); 
					DataArrayUnlock( psaVar2 ); 
					throw true;
				}
				if ( DataArrayPtrOfIndex( psaVar2, index, (void**)&pVarITEM2 )!=RET_OK ) 
				{
					DataArrayUnlock( psaVar1 ); 
					DataArrayUnlock( psaVar2 ); 
					throw true;
				}

				if ( ( bRet = isEqual( *pVarITEM1, *pVarITEM2 ) ) == false )
				{
					DataArrayUnlock( psaVar1 ); 
					DataArrayUnlock( psaVar2 ); 
					return bRet;
				}
			}
			DataArrayUnlock( psaVar1 ); 
			DataArrayUnlock( psaVar2 ); 
			return true;
		}
	}
	catch ( bool e )
	{
		return e;
	}

	switch ( var1.dt )
	{
	case DT_I4:
		bRet = ( var1.lVal == var2.lVal );
		break;

	case DT_UI1:
		bRet = ( var1.bVal == var2.bVal );
		break;

	case DT_I2:
		bRet = ( var1.iVal == var2.iVal );
		break;

	case DT_R4:
		bRet = ( var1.fltVal == var2.fltVal );
		break;

	case DT_R8:
		bRet = ( var1.dblVal == var2.dblVal );
		break;

	case DT_BOOL:
		bRet = ( var1.boolVal == var2.boolVal );
		break;

	case DT_DATE:
		bRet = difftime(var1.date, var2.date)==0;
		break;

	case DT_SSTR:
		bRet = wcscmp( var1.sstringVal, var2.sstringVal ) == false ? true : false;
		break;

	case DT_I1:
		bRet = ( var1.cVal == var2.cVal );
		break;

	case DT_UI2:
		bRet = ( var1.uiVal == var2.uiVal );
		break;

	case DT_UI4:
		bRet = ( var1.ulVal == var2.ulVal );
		break;

	case DT_INT:
		bRet = ( var1.intVal == var2.intVal );
		break;

	case DT_UINT:
		bRet = ( var1.uintVal == var2.uintVal );
		break;

	default:
		break;
	}

	if ( bRet == false )
	{
		//"Value 1 Type is %i Value 2 type %i \n", var1.dt, var2.dt );
	}

	return bRet;
}

