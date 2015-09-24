package com.sas.mom.Entity;

import java.util.List;

public class MarketingActivity {
	private String id;
	private String cd;
	private String name;
	private List<String> vertriebsBereiche;
	// private int plannedVolume;
	private String startDate;
	private String endDate;
	private String lastUpdate;
	private String path;

	public MarketingActivity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getVertriebsBereiche() {
		return vertriebsBereiche;
	}

	public void setVertriebsBereiche(List<String> vertriebsBereiche) {
		this.vertriebsBereiche = vertriebsBereiche;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "MarketingActivity [id=" + id + ", cd=" + cd + ", name=" + name + ", vertriebsBereiche="
				+ vertriebsBereiche + ", startDate=" + startDate + ", endDate=" + endDate + ", lastUpdate="
				+ lastUpdate + ", path=" + path + "]";
	}
	
	
	
}
