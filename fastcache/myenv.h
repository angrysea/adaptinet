/*
 * myenv.h
 *
 *  Created on: Dec 4, 2014
 *      Author: anthony
 */

#ifndef MYENV_H_
#define MYENV_H_


extern JNIEnv * myEnv;
extern jclass sClass;
extern jclass dosCls;
extern jmethodID dosCons;
extern jmethodID dosWriteInt;
extern jmethodID dosWriteBytes;
extern jclass disCls;
extern jmethodID disCons;
extern jmethodID disReadInt;
extern jmethodID disReadBytes;
extern jclass dataItemClass;
extern jmethodID dataItemCons;
extern jclass dataArrayClass;
extern jmethodID dataArrayCons;

#endif /* MYENV_H_ */
