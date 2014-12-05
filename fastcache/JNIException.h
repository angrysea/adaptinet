#pragma once

class JNIException
{
public:
	JNIException(int error) : errorCode(error)
	{
	}

	int getErrorCode()
	{
		return errorCode;
	}

public:
	static int lastError;

private:
	int errorCode;
};