#include "InstanceNode.h"
#include "InstanceTree.h"
#include "InstanceSpider.h"

InstanceTree::~InstanceTree()
{
	for(std::multimap<std::wstring, InstanceNode *>::const_iterator it = items.begin(); it != items.end(); it++)
	{
		InstanceNode * child =(*it).second;
		delete child;
	}
}

void InstanceTree::insert(const std::wstring & sTag, InstanceNode * pNode)
{
	std::wstring::size_type start = 0;

	if((start = sTag.find(pathsep, 0)) == std::wstring::npos)
	{
		pNode->setName(sTag);
		items.insert(InstanceNodesValueType(sTag, pNode));
	}
	else
	{
		InstanceNodesIterator it;
		std::wstring name = sTag.substr(0, start);
		it = items.find(name);
		if(it != items.end())
		{
			(*it).second->insert(sTag.substr(++start), pNode);
		}
		else
		{
			InstanceNode * pParentNode = new InstanceNode();
			pParentNode->setName(name);
			items.insert(InstanceNodesValueType(name, pParentNode));
			pParentNode->insert(sTag.substr(++start), pNode);
		}
	}
}

bool InstanceTree::remove(const std::wstring & sTag)
{
	std::wstring::size_type start = 0;

	try
	{
		InstanceNodesIterator it;
		if((start=sTag.find(pathsep,0))==std::wstring::npos)
		{
			it = items.find(sTag);
			if(it != items.end())
			{
				items.erase(it);
				return true;
			}
			else
			{
				throw false;
			}
		}
		else
		{
			it = items.find(sTag.substr(0, start));
			if(it != items.end())
			{
				return (*it).second->remove(sTag.substr(++start));
			}
		}
	}
	catch(bool)
	{
	}
	return false;
}

bool InstanceTree::remove(InstanceNode * pNode)
{
	try
	{
		InstanceNodesIterator it;

		it = items.find(pNode->getName());
		if(it!=items.end())
		{
			items.erase(it);
			return true;
		}
	}
	catch(long)
	{
	}
	return false;
}

InstanceNode * InstanceTree::getNode(const std::wstring & sTag)
{
	std::wstring::size_type start = 0;
	InstanceNode * pNode = 0;

	try
	{
		InstanceNodesIterator it;
		if((start = sTag.find(pathsep, 0)) == std::wstring::npos)
		{
			it = items.find(sTag);
			if(it != items.end())
			{
				pNode =(*it).second;
			}
			else
			{
				throw false;
			}
		}
		else
		{
			it = items.find(sTag.substr(0, start));
			if(it != items.end())
			{
				pNode =(*it).second->getNode(sTag.substr(++start));
			}
		}
	}
	catch(bool)
	{
	}

	return pNode;
}

void InstanceTree::getNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
{
	std::wstring::size_type start = 0;

	try
	{
		InstanceNodesIterator it;
		if((start=sTag.find(pathsep, 0)) == std::wstring::npos)
		{
			it = items.find(sTag);
			while(it!=items.end())
			{
				theList.push_back((*it).second);
			}
		}

		it = items.find(sTag.substr(0, start));
		while(it!=items.end())
		{
			(*it).second->getNodes(sTag.substr(++start),theList);
		}
	}
	catch(long)
	{
	}
}

InstanceNode * InstanceTree::findNode(const std::wstring & sTag)
{
	InstanceNode * pNode = 0;

	try
	{
		InstanceNodesIterator it;

		it = items.find(sTag);
		if(it != items.end())
		{
			pNode =(*it).second;
		}
		else
		{
			it = items.begin();
			while(it != items.end())
			{
				if((pNode =(*it).second->findNode(sTag)) != 0)
				{
					break;
				}
				it++;
			}
		}
	}
	catch(long)
	{
	}

	return pNode;
}

void InstanceTree::findNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
{
	try
	{
		InstanceNodesIterator it;

		it = items.find(sTag);
		while(it != items.end())
		{
			theList.push_back((*it).second);
		}

		it = items.begin();
		while(it!=items.end())
		{
			(*it).second->findNodes(sTag,theList);
			it++;
		}
	}
	catch(long)
	{
	}

}

void InstanceTree::process(InstanceSpider * spider, InstanceNode * parent, int levels)
{
	InstanceNode * item = 0;
	if(levels!=999999999)
		levels--;

	InstanceNodesIterator it = items.begin();
	while(it!=items.end())
	{
		item = (*it).second;
		spider->nextNode(item,parent);
		if(levels>0)
			item->process(spider,item,levels);
		it++;
	}
}

void InstanceTree::RenamePath(std::wstring strOldName, std::wstring strNewName)
{
	InstanceNode* pNode = findNode(strOldName);
	if (pNode != 0)
	{
		items.erase(strOldName);
		items.insert(InstanceNodesValueType(strNewName, pNode));
	}
}

void InstanceTree::flatten(std::vector<InstanceNode * > & theList) const
{
	for(InstanceNodesConstIterator it = items.begin(); it != items.end(); it++)
	{
		InstanceNode * child =(*it).second;
		theList.push_back(child);
		child->flatten(theList);
	}
}


void InstanceTree::getLeaves(std::vector<InstanceNode * > & theList)
{
	for(InstanceNodesIterator it = items.begin(); it != items.end(); it++)
	{
		InstanceNode * child =(*it).second;

		if(child->isLeaf())
		{
			theList.push_back(child);
			continue;
		}
		child->getLeaves(theList);
	}
}

int InstanceTree::getRoute(const std::wstring & sTag, std::vector<InstanceNode * > & theList)
{
	int nRet = 0;
	try
	{
		InstanceNodesIterator it;

		it = items.find(sTag);
		if(it != items.end())
		{
			theList.push_back((*it).second);
			nRet = 1;
		}
		else
		{
			it = items.begin();
			while(it != items.end())
			{
			if((nRet =(*it).second->getRoute(sTag, theList)) != 0)
			{
				nRet++;
				theList.push_back((*it).second);
				break;
			}
			it++;
			}
		}
	}
	catch(...)
	{
		nRet = 0;
	}

	return 0;
}

int InstanceTree::getPath(const std::wstring & sTag, std::wstring & sPath) const
{
	int nRet = 0;
	try
	{
		InstanceNodesConstIterator it = items.find(sTag);
		if(it != items.end())
		{
			sPath +=(*it).first;
			nRet = 1;
		}
		else
		{
			it = items.begin();
			while(it != items.end())
			{
			if((nRet =(*it).second->getPath(sTag, sPath)) != 0)
			{
				nRet++;
				sPath +=(*it).first;
				sPath += pathsep;
				break;
			}
			it++;
			}
		}
	}
	catch(...)
	{
		nRet = 0;
	}

	return 0;
}

int InstanceTree::getRoutes(const std::wstring & sTag, std::vector<std::vector<InstanceNode * > * > & theList)
{
	int nRet = 0;

	try
	{
		InstanceNodesIterator it;

		it = items.find(sTag);
		if(it != items.end())
		{
			std::vector<InstanceNode * > * pList = new std::vector<InstanceNode * >;
			pList->push_back((*it).second);
			theList.push_back(pList);
			nRet = 1;
		}
		else
		{
			it = items.begin();
			while(it != items.end())
			{
				std::vector<std::vector<InstanceNode * > * > myList;
				if((nRet =(*it).second->getRoutes(sTag, myList)) != 0)
				{
					std::vector<std::vector<InstanceNode * > * >::iterator qit = myList.begin();
					while(qit != myList.end())
					{
						std::vector<InstanceNode * > * pList =(*qit);
						pList->push_back((*it).second);
						theList.push_back(pList);
						qit++;
					}
					nRet++;
					break;
				}
				it++;
			}
		}
	}
	catch(...)
	{
		nRet = 1;
	}

	return 0;
}

void InstanceTree::clear()
{
	try
	{
		std::multimap<std::wstring, InstanceNode *>::iterator it = items.begin();
		while(it!=items.end())
		{
			delete (*it).second;
		}
		items.clear();
	}
	catch(long)
	{
	}
}

