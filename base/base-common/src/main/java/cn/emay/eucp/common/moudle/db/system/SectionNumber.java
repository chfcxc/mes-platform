package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;

/**
 * 详细号段
 *
 * @author Frank
 *
 */
public class SectionNumber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String number;// 号段
	private String provinceCode;// 省份编码，cn.emay.eucp.common.constant.Province
	private String provinceName;// 省份名称，cn.emay.eucp.common.constant.Province
	private String city;// 城市
	private String operatorCode;// 运营商CODE[CMCC-移动，CUCC-联通，CTCC-电信]，cn.emay.eucp.common.constant.Operator
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}


}
