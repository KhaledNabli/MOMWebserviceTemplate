package com.sas.mom.Entity;

import java.util.List;

public class VarianceDetail {

	private String pk;
	private String name;
	private String targetGroup;
	private String offerNm;
	private String validFrom;
	private String validTo;
	private List<String> rabattIds;
	private String ean;
	private String eycCode;
	private String pbCode;
	private String marketingCd;
	private VarianceHead head;
	private String targetStrategy;

	public VarianceDetail() {
		super();
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

	public String getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}

	public String getOfferNm() {
		return offerNm;
	}

	public void setOfferNm(String offerNm) {
		this.offerNm = offerNm;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public List<String> getRabattIds() {
		return rabattIds;
	}
	
	public String getRabattIdTxt(){
		String result = "";
		if(rabattIds!=null) {
			
			for(int i=0; i< rabattIds.size();i++) {
				if(i==0){
					result += rabattIds.get(i);
				}
				else {
					result += ", " + rabattIds.get(i);
				}
			}
		}
		return result;
	}

	public void setRabattIds(List<String> rabattIds) {
		this.rabattIds = rabattIds;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getEycCode() {
		return eycCode;
	}

	public void setEycCode(String eycCode) {
		this.eycCode = eycCode;
	}

	public String getPbCode() {
		return pbCode;
	}

	public void setPbCode(String pbCode) {
		this.pbCode = pbCode;
	}

	public String getMarketingCd() {
		return marketingCd;
	}

	public void setMarketingCd(String marketingCd) {
		this.marketingCd = marketingCd;
	}

	public VarianceHead getHead() {
		return head;
	}

	public void setHead(VarianceHead head) {
		this.head = head;
	}

	@Override
	public String toString() {
		return "Variance [pk=" + pk + ", name=" + name + ", targetGroup=" + targetGroup + ", offerNm=" + offerNm
				+ ", validFrom=" + validFrom + ", validTo=" + validTo + ", rabattIds=" + rabattIds + ", ean=" + ean
				+ "]";
	}

	public void setTargetstrategy(String strategy) {
		targetStrategy = strategy;
	}
	
	public String getTargetStrategy() {
		return targetStrategy;
	}

}
