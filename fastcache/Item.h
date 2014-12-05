#pragma once

#include "rtds.h"
#include "types.h"
#include "DataItem.h"
#include "SafeString.h"

class Item
{
public:
	Item(void);
	virtual ~Item()
	{
		DataItemClear(&m_varValue);
	}

	long Init(DATATYPE dt);

	long GetAttributeITEM(ITEMATTRIBUTES *ITEM);
	long IsDirty(bool * bDirtyFlag);

	unsigned short getQuality() { return m_Quality; }
	void setQuality( unsigned short wQuality ) { m_BeenUpdated = true; m_Quality = wQuality; }

	long SetValue(DATAITEM var);
	long GetValue(DATASOURCE ds, DATAITEM * var, unsigned short *qual, time_t * time);
	long SetDataType(DATATYPE v);

	long GetActive(bool *bActive)
	{
		*bActive = m_bActive;
		return RET_OK;
	}

	long setGuid(SString bstrGuid)
	{
		m_bstrGuid = bstrGuid;
		return RET_OK;
	}

	SString getGuid()
	{
		return m_bstrGuid;
	}

	long SetActive(bool active)
	{
		m_bActive = active;
		if ( m_bActive != 0 ) 
		{
			SetDirty( true );
		}
		else 
		{
			SetDirty( false );
		}
		return RET_OK;
	}

	long GetDataType(DATATYPE * v)
	{
		*v = m_vtCanonicalDataType;
		return RET_OK;
	}

	long SetDirty(bool bDirtyFlag)
	{
		m_bDirty = bDirtyFlag;
		return RET_OK;
	}

   long GetServerHandle(long * hServer)
   {
	   *hServer = m_hServerItem;
	   return 0;
   }

   long SetServerHandle(long hServer)
   {
	   m_hServerItem = hServer;
	   return 0;
   }

   long GetClientHandle(long * m_hClient)
   {
	   *m_hClient = m_hClientItem;
	   return 0;
   }

   long SetClientHandle( long m_hClient )
   {
	   m_hClientItem = m_hClient;
	   return 0;
   }

	static bool isEqual(DATAITEM var1, DATAITEM var2);
   
private:
	long m_hServerItem;
	long m_hClientItem;
	bool m_bActive;
	unsigned short m_Quality;
	bool m_bDirty;
	long m_LastWriteError;
	unsigned short m_AsyncMask;
	DATATYPE m_vtCanonicalDataType;
	DATAITEM m_varValue;
	SString m_bstrGuid;
	bool m_BeenUpdated;
	time_t m_TimeStamp;
};
