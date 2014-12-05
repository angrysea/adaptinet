#pragma once
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SString wchar_t *

wchar_t * SafeString(const wchar_t * pSrc);
wchar_t * SafeString(int len);
wchar_t * SafeString(const wchar_t * pSrc, unsigned int len);
wchar_t * SafeString(const char * pSrc);
wchar_t * SafeString(const char * pSrc, unsigned int len); 
SString SafeStringCopy(SString pSrc); 
void SafeStringFree(wchar_t * pSrc);
int SafeStringByteLen(wchar_t * sstring);
int SafeStringLen(wchar_t * sstring);
char * GetAnsiString(wchar_t * pSrc);
wchar_t * SafeStringToString(wchar_t * pSrc);
