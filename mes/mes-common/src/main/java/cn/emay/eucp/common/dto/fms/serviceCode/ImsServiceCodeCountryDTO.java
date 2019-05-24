package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImsServiceCodeCountryDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long serviceCodeId;//服务号id
	private BigDecimal price;//价钱
	private String countryCode;// 国家代码
	private String chineseName;// 中文
	private String countryNumber;//国家编码
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getServiceCodeId() {
		return serviceCodeId;
	}
	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCountryNumber() {
		return countryNumber;
	}
	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}
	
}
