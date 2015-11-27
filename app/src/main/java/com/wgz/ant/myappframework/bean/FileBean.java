package com.wgz.ant.myappframework.bean;


public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;
	@TreeNodePhone
	private String phone;

	public FileBean(int _id, int parentId, String name,String phone)
	{
		super();
		this.phone=phone;
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
	}

}
