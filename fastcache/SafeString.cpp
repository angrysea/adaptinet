#include <cwchar>
#include "SafeString.h"

wchar_t * SafeString(const wchar_t * pSrc) 
{
	wchar_t * sstring = 0;
	if(pSrc!=0) 
	{
		sstring = SafeString(pSrc, wcslen(pSrc));
	}
	return sstring;
}

wchar_t * SafeString(int len) 
{
	return SafeString((const wchar_t *)0, len);
}

wchar_t * SafeString(const wchar_t * pSrc, unsigned int len) 
{
	wchar_t * sstring = 0;
	if(pSrc==0) 
	{
		if(len>0) 
		{
			unsigned int bytesMax = len+1;
			void * buffer = new wchar_t[bytesMax + 4];
			*((int*)buffer) = len;
			sstring = (wchar_t *)buffer + sizeof(int);
			bytesMax*=sizeof(wchar_t);
			memset(sstring, 0x00, bytesMax);
		}
	}
	else if(len>0) 
	{
		unsigned int srcLen = wcslen(pSrc);
		unsigned int max = len > srcLen ? srcLen : len;
		unsigned int bytesMax = max+1;
		void * buffer = new wchar_t[bytesMax + 4];
		bytesMax*=sizeof(wchar_t);
		*((int*)buffer) = len;
		sstring = (wchar_t *)buffer + sizeof(int);
		memset(sstring, 0x00, bytesMax);
		memcpy(sstring, pSrc, bytesMax);
	}
	return sstring;
}

wchar_t * SafeString(const char * pSrc) 
{
	wchar_t * sstring = 0;

	if(pSrc!=0) 
	{
		sstring = SafeString(pSrc, strlen(pSrc));
	}

	return sstring;
}

wchar_t * SafeString(const char * pSrc, unsigned int len) 
{
	wchar_t * sstring = 0;
	if(pSrc==0) 
	{
		if(len>0) 
		{
			sstring = SafeString((const wchar_t *)0, len);
		}
	}
	else if(len>0) 
	{
		unsigned int srcLen = strlen(pSrc);
		unsigned int max = len > srcLen ? srcLen : len;
		unsigned int bytesMax = len+1;
		void * buffer = new wchar_t[bytesMax + 4];
		bytesMax*=sizeof(wchar_t);
		*((int*)buffer) = len;
		sstring = (wchar_t *)buffer + sizeof(int);
		memset(sstring, 0x00, bytesMax);
		mbstowcs(sstring, pSrc, max);
	}
	return sstring;
}

SString SafeStringCopy(SString pSrc)
{
	SString sstring = 0;
	if(pSrc!=0) 
	{
		sstring = SafeString(pSrc, SafeStringLen(pSrc));
	}

	return sstring;
}


int SafeStringByteLen(wchar_t * sstring)
{
	int len = 0;
	if(sstring!=0) 
	{
		len = *((int *)sstring - sizeof(int));
		len = ((len+1)*sizeof(wchar_t)) + sizeof(int);
	}
	return len;
}

int SafeStringLen(wchar_t * sstring)
{
	int len = 0;
	if(sstring!=0) 
	{
		len = *((int *)sstring - sizeof(int));
	}
	return len;
}


void SafeStringFree(wchar_t * pSrc)
{
	if(pSrc!=0) 
	{
		delete[] (pSrc - sizeof(int));
		pSrc=0;
	}
}

char * GetAnsiString(wchar_t * pSrc) 
{
	size_t size = wcslen(pSrc) + 1;

	char * buf = new char[size];

	size = wcstombs(buf, pSrc, size);
	if (size == (size_t) -1)
	    return 0;

	return buf;
}

wchar_t * SafeStringToString(wchar_t * pSrc) 
{
	size_t size = wcslen(pSrc);

	wchar_t * buf = new wchar_t[size+1];

	wcscpy(buf, pSrc);

	return buf;
}
