package cn.emay.eucp.common.dto.fms.base;

import java.io.Serializable;

public class SectiomNumberDTO implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String countryCode;// 国家代码
	private String chineseName;// 中文
	private String abbreviation;// 缩写
	private String sectionNumber;//号段
	private Integer numberLength;//长度 0不校验
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public String getSectionNumber() {
		return sectionNumber;
	}
	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}
	public Integer getNumberLength() {
		return numberLength;
	}
	public void setNumberLength(Integer numberLength) {
		this.numberLength = numberLength;
	}
}
