#include "CacheServer.h"
#include "Item.h"
#include "DataItem.h"
#include "JNIException.h"

CacheServer * CacheServer::theCacheServer = 0;
int JNIException::lastError = 0;

CacheServer::~CacheServer()
{
	if(theCacheServer==0) 
	{
		delete theCacheServer;
	}
}

CacheServer * CacheServer::getCacheServer()
{
	if(theCacheServer==0) 
	{
		theCacheServer = new CacheServer;
	}
	return theCacheServer;
}

bool CacheServer::putValue(const std::wstring & key, const DATAITEM & value)
{
	Item * item = new Item;
	item->Init(value.dt);
	item->SetValue(value);
	root.insert(key.c_str(), new InstanceNode(item));

	return true;
}

bool CacheServer::getValue(const std::wstring & key, DATAITEM & dataItem)
{
	unsigned short qual;
	time_t * time = 0;
	DataItemInit(&dataItem);
	InstanceNode * node = root.getNode(key.c_str());
	Item * item = node->getValue();

	item->GetValue(DS_CACHE, &dataItem, &qual, time);

	return true;
}
