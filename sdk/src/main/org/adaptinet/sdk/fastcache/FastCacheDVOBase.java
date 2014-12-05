package org.adaptinet.sdk.fastcache;

import org.adaptinet.sdk.adaptinetex.FastCacheException;

public interface FastCacheDVOBase {
	public void putToCache(String key, CacheServer server)
			throws FastCacheException;

	public void getFromCache(String key, CacheServer server)
			throws FastCacheException;

	public DataArray writeCache() throws FastCacheException;

	public void readCache(DataArray in) throws FastCacheException;

}
