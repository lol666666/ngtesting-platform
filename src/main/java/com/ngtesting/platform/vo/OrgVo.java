package com.ngtesting.platform.vo;


public class OrgVo extends BaseVo {
	private static final long serialVersionUID = -7115478651798848319L;
	private String name;
    private String website;
    private Boolean defaultOrg;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public Boolean getDefaultOrg() {
		return defaultOrg;
	}
	public void setDefaultOrg(Boolean defaultOrg) {
		this.defaultOrg = defaultOrg;
	}

	
}
