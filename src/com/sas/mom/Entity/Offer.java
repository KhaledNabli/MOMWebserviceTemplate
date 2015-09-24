package com.sas.mom.Entity;


public class Offer {
	private String id;
	private String code;
	private String name;
	private String desc;
	

	public Offer() {
	}


	public Offer(String id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}	
	
	@Override
	public String toString() {
		return "Offer [id=" + id + ", code=" + code + ", name=" + name + "]";
	}
	
	
	
}
