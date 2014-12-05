#pragma once
#include <vector>
#include <map>

const wchar_t pathsep = L'.';

class InstanceNode;
class InstanceSpider;

class InstanceTree  
{
public:
	InstanceTree()
	{
	}

	~InstanceTree();

	int size() const
	{
	   return items.size();
	}

	void insert(InstanceNode * pNode);
	void insert(const std::wstring & sTag, InstanceNode * pNode);
	bool remove(const std::wstring & sTag);
	bool remove(InstanceNode * pNode);
	InstanceNode * getNode(const std::wstring & sTag);
	void getNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList);
	InstanceNode * findNode(const std::wstring & sTag);
	void findNodes(const std::wstring & sTag, std::vector<InstanceNode * > & theList);
	void process(InstanceSpider * spider, InstanceNode * parent, int levels=999999999);
	void RenamePath(std::wstring strOldName, std::wstring strNewName);
	void flatten(std::vector<InstanceNode * > & theList) const;
	void getLeaves(std::vector<InstanceNode * > & theList);
	int getRoute(const std::wstring & sTag, std::vector<InstanceNode * > & theList);
	int getPath(const std::wstring & sTag, std::wstring & sPath) const;
	int getRoutes(const std::wstring & sTag, std::vector<std::vector<InstanceNode * > * > & theList);
	void clear();

	std::multimap<std::wstring, InstanceNode *>::iterator begin()
	{
	   return items.begin();
	}

	std::multimap<std::wstring, InstanceNode *>::iterator end()
	{
	   return items.end();
	}

private:
	std::multimap<std::wstring, InstanceNode *> items;
};
