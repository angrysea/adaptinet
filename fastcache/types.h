#pragma once

#include <linux/types.h>
#include <cstring>
#include <jni.h>

#define RET_OK ((long)0L)
#define RET_FALSE ((long)1L)
#define RET_ERROR ((long)2L)
#define RET_FAIL ((long)2L)
#define RET_UNEXPECTED ((long)3L)
#define RET_PARAMNOTFOUND ((long)4L)

#define D_UNION(X, Y)   ((X)->Y)
#define D_ARRAY(X) D_UNION(X, parray)

typedef struct tagDEC {
    unsigned short wReserved;
    union {
        struct {
            unsigned char scale;
            unsigned char sign;
        };
        unsigned short signscale;
    };
    unsigned long Hi32;
    union {
        struct {
            unsigned long Lo32;
            unsigned long Mid32;
        };
        __u64 Lo64;
    };
} DECIMAL;

#define DECIMAL_NEG ((unsigned char)0x80)
#define DECIMAL_SETZERO(dec) \
        {(dec).Lo64 = 0; (dec).Hi32 = 0; (dec).signscale = 0;}

typedef DECIMAL *LPDECIMAL;

typedef unsigned short DATATYPE;

enum DATATYPEENUM
{	
	DT_EMPTY = 0,
	DT_NULL	= 1,
	DT_I2 = 2,
	DT_I4 = 3,
	DT_R4 = 4,
	DT_R8 = 5,
	DT_DATE	= 7,
	DT_SSTR	= 8,
	DT_ERROR = 10,
	DT_BOOL	= 11,
	DT_DATAITEM	= 12,
	DT_DECIMAL = 14,
	DT_I1 = 16,
	DT_UI1 = 17,
	DT_UI2 = 18,
	DT_UI4 = 19,
	DT_I8 = 20,
	DT_UI8 = 21,
	DT_INT = 22,
	DT_UINT = 23,
	DT_VOID = 24,
	DT_LONG = 25,
	DT_PTR = 26,
	DT_LPSTR = 30,
	DT_INT_PTR = 37,
	DT_UINT_PTR	= 38,
	DT_LONG_PTR = 39,
	DT_DOUBLE_PTR = 64,
	DT_VECTOR = 0x1000,
	DT_ARRAY = 0x2000,
	DT_BYREF = 0x4000,
	DT_RESERVED = 0x8000,
	DT_ILLEGAL = 0xffff,
	DT_ILLEGALMASKED = 0xfff,
	DT_TYPEMASK	= 0xfff
};
