#pragma once
class Item;
class InstanceNode;

class InstanceSpider  
{
public:
	InstanceSpider()
	{
	}

	virtual ~InstanceSpider()
	{
	}

	virtual void begin()
	{
	}

	virtual void end()
	{
	}

	virtual void hasAttribute(InstanceNode * instanceItem, Item * item)
	{
	}

	virtual void nextNode(InstanceNode * next, InstanceNode * parent) = 0;
};
