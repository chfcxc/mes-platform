package cn.emay.eucp.common.dto.fms.country;

import java.io.Serializable;

/**
 * 国家码DTO
 * 
 * @author Administrator
 */
public class ImsCountryDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String chineseName;// 中文
	
	private String countryNumber;
	
	private  String countryCode;

	public ImsCountryDTO(Long id,  String chineseName,String countryNumber,String countryCode) {
		this.id=id;
		this.chineseName=chineseName;
		this.countryNumber=countryNumber;
		this.countryCode=countryCode;
	}
	public ImsCountryDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getCountryNumber() {
		return countryNumber;
	}

	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
}
