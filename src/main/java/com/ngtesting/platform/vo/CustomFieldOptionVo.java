package com.ngtesting.platform.vo;

public class CustomFieldOptionVo extends BaseVo {
	private static final long serialVersionUID = 4904548137077167076L;

	private String value;
	private String label;
	private String descr;
	private Integer order;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
