package com.sas.mom.Entity;

import java.util.List;

public class VarianceHead {
	private String pk;
	private String name;
	private String strategie;
	private String targetGroup;
	private int offerCount;
	private int plannedVolume;
	private int actualVolume;
	private String desc;
	private List<VarianceDetail> varianceDetails;

	public VarianceHead() {
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrategie() {
		return strategie;
	}

	public void setStrategie(String strategie) {
		this.strategie = strategie;
	}

	public String getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}

	public int getPlannedVolume() {
		return plannedVolume;
	}

	public void setPlannedVolume(int plannedVolume) {
		this.plannedVolume = plannedVolume;
	}

	public int getActualVolume() {
		return actualVolume;
	}

	public void setActualVolume(int actualVolume) {
		this.actualVolume = actualVolume;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(int offerCount) {
		this.offerCount = offerCount;
	}
	
	
	public List<VarianceDetail> getVarianceDetails() {
		return varianceDetails;
	}
	
	public void setVarianceDetails(List<VarianceDetail> varianceDetails) {
		this.varianceDetails = varianceDetails;
	}
	
	
	@Override
	public String toString() {
		return "VarianceHead [pk=" + pk + ", name=" + name + ", strategie=" + strategie + ", targetGroup="
				+ targetGroup + ", offerCount=" + offerCount + ", plannedVolume=" + plannedVolume + ", actualVolume="
				+ actualVolume + ", desc=" + desc + "]";
	}

}
