#include "jconverters.h"

jstring convert(JNIEnv * env, std::wstring s)
{
    iconv_t cd = iconv_open("UTF-8", "WCHAR_T");

    if (cd == iconv_t(-1))
    {
        std::cout << "Error while initializing iconv: " /*<< errno*/ << std::endl;
        iconv_close(cd);
        return nullptr;
    }

    std::size_t n = s.length() * 2 + 1; // Each character might use up to two CUs.
    const std::size_t norig = n;
    std::size_t m = s.length() * sizeof(std::wstring::value_type);

    std::vector<char> obuf(n);
    char * outbuf = reinterpret_cast<char*>(obuf.data());
    const char * inbuf = reinterpret_cast<const char*>(&s[0]);

    const std::size_t ir = iconv(cd, const_cast<char**>(&inbuf), &m, &outbuf, &n);

    if (ir == std::size_t(-1))
    {
        std::cout << "Error while converting with iconv(): " /* << errno << ":" << EINVAL << */ ", left " << m
            << ", written " << std::dec << norig - n << " bytes." << std::endl;
        iconv_close(cd);
        return nullptr;
    }

    iconv_close(cd);

	return env->NewStringUTF(obuf.data());
}

jstring convert(JNIEnv * env, wchar_t * s, int length)
{
    iconv_t cd = iconv_open("UTF-8", "WCHAR_T");

    if (cd == iconv_t(-1))
    {
        std::cout << "Error while initializing iconv: " /*<< errno*/ << std::endl;
        iconv_close(cd);
        return nullptr;
    }

    std::size_t n = length * 2 + 1; // Each character might use up to two CUs.
    const std::size_t norig = n;
    std::size_t m = length * sizeof(std::wstring::value_type);

    std::vector<char> obuf(n);
    char * outbuf = reinterpret_cast<char*>(obuf.data());
    const char * inbuf = reinterpret_cast<const char*>(s);

    const std::size_t ir = iconv(cd, const_cast<char**>(&inbuf), &m, &outbuf, &n);

    if (ir == std::size_t(-1))
    {
        std::cout << "Error while converting with iconv(): " /* << errno << ":" << EINVAL << */ ", left " << m
            << ", written " << std::dec << norig - n << " bytes." << std::endl;
        iconv_close(cd);
        return nullptr;
    }

    iconv_close(cd);

	return env->NewStringUTF(obuf.data());
}

std::wstring convert(JNIEnv * env, jstring js)
{
	int length = env->GetStringLength(js);
	const jchar * s = env->GetStringChars(js, 0);

    iconv_t cd = iconv_open("WCHAR_T", "UTF-16");

    if (cd == iconv_t(-1))
    {
        std::cout << "Error while initializing iconv: " /*<< errno*/ << std::endl;
        iconv_close(cd);
        return std::wstring();
    }


    std::size_t n = length * sizeof(std::wstring::value_type) + 1; // Each character might use up to two CUs.
    const std::size_t norig = n;
    std::size_t m = length * sizeof(jchar);

    std::vector<wchar_t> obuf(n);
    char * outbuf = reinterpret_cast<char*>(obuf.data());
    const char * inbuf = reinterpret_cast<const char*>(&s[0]);

    const std::size_t ir = iconv(cd, const_cast<char**>(&inbuf), &m, &outbuf, &n);
	env->ReleaseStringChars(js, s);

    if (ir == std::size_t(-1))
    {
        std::cout << "Error while converting with iconv(): " /* << errno << ":" << EINVAL << */ ", left " << m
            << ", written " << std::dec << norig - n << " bytes." << std::endl;
        iconv_close(cd);
        return std::wstring();
    }

    iconv_close(cd);

    return std::wstring(obuf.data());//, (norig - n)/sizeof(std::wstring::value_type));
}
