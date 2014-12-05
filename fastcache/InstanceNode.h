#pragma once

#include <stack>
#include <vector>
#include <map>
#include <string>

#include "Item.h"
#include "InstanceTree.h"

class InstanceSpider;

class InstanceNode
{
public:
    InstanceNode() : m_pTree(0),
        value(0)
    {
    }

    InstanceNode(Item * v) : m_pTree(0),
        value(v)
    {
    }

    ~InstanceNode()
    {
        delete value;
    }

	void checkMap()
    {
    	if(m_pTree == 0)
        {
        	m_pTree = new InstanceTree;
        }
    }

    void insert(const std::wstring & sTag, InstanceNode * pNode)
    {
    	checkMap();
        m_pTree->insert(sTag, pNode);
    }

    InstanceNode * getNode(const std::wstring & sTag)
    {
    	InstanceNode * pNode = 0;

        if(m_pTree != 0)
        {
        	pNode = m_pTree->getNode(sTag);
        }
        return pNode;
    }

    void getNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
    {
    	if(m_pTree != 0)
        {
        	m_pTree->getNodes(sTag,theList);
        }
    }

	void process(InstanceSpider * spider, InstanceNode * parent, int levels)
	{
		if(m_pTree != 0)
		{
			m_pTree->process(spider, parent, levels);
		}
	}

	InstanceNode * findNode(const std::wstring & sTag)
	{
		InstanceNode * pNode = 0;

		if(m_pTree != 0)
		{
			pNode = m_pTree->findNode(sTag);
		}
		return pNode;
	}

	void findNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
	{
		if(m_pTree != 0)
		{
			m_pTree->findNodes(sTag,theList);
		}
	}

	bool remove(const std::wstring & sTag)
	{
		bool bRet = false;

		if(m_pTree != 0)
		{
			bRet = m_pTree->remove(sTag);
		}
		return bRet;
	}

	void RenamePath(std::wstring strOldName, std::wstring strNewName)
	{
		if (m_pTree != 0)
		{
			m_pTree->RenamePath(strOldName, strNewName);
		}
	}

	int getRoute(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
	{
		int nRet = 0;
		if(m_pTree != 0)
		{
			nRet = m_pTree->getRoute(sTag, theList);
		}
		return nRet;
	}

	int getPath(const std::wstring & sTag, std::wstring & sPath) const
	{
		int nRet = 0;
		if(m_pTree != 0)
		{
			nRet = m_pTree->getPath(sTag, sPath);
		}
		return nRet;
	}

	int getRoutes(const std::wstring & sTag, std::vector<std::vector<InstanceNode * > * > & theList)
	{
		int nRet = 0;
		if(m_pTree != 0)
		{
			nRet = m_pTree->getRoutes(sTag, theList);
		}
		return nRet;
	}

	void getLeaves(std::vector<InstanceNode * > & theList)
	{
		if(m_pTree != 0)
		{
			m_pTree->getLeaves(theList);
		}
	}

	void flatten(std::vector<InstanceNode * > & theList) const
	{
		if(m_pTree != 0)
		{
			m_pTree->flatten(theList);
		}
	}

	std::multimap<std::wstring, InstanceNode *>::iterator begin()
	{
		checkMap();
		return m_pTree->begin();
	}

	std::multimap<std::wstring, InstanceNode *>::iterator end()
	{
		checkMap();
		return m_pTree->end();
	}

	bool isLeaf() const
	{
		return m_pTree == 0 ? true : false;
	}

	const std::wstring getName() const
	{
		return name;
	}

	void setName(std::wstring newValue)
	{
		name = newValue;
	}

	Item * getValue()
	{
		return value;
	}

	void setValue(Item * newValue)
	{
		value = newValue;
	}

private:
	InstanceTree * m_pTree;
	std::wstring name;
	Item * value;
};

typedef std::stack<InstanceNode *> InstanceNodeStack;
typedef std::vector<InstanceNode *> InstanceNodeVector;
typedef std::vector<InstanceNode *>::iterator InstanceNodeVectorIterator;
typedef std::multimap<std::wstring, InstanceNode *> InstanceNodes;
typedef std::multimap<std::wstring, InstanceNode *>::iterator InstanceNodesIterator;
typedef std::multimap<std::wstring, InstanceNode *>::const_iterator InstanceNodesConstIterator;
typedef std::multimap<std::wstring, InstanceNode *>::value_type InstanceNodesValueType;

