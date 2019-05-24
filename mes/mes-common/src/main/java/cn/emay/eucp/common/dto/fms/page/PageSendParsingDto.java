package cn.emay.eucp.common.dto.fms.page;

import java.io.Serializable;
import java.util.Map;
/**
 * 页面发送文件解析数据dto
 *
 */
public class PageSendParsingDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;

	private Map<String, String> data;

	private String value;

	public PageSendParsingDto(){
		
	}
	
	public PageSendParsingDto(String mobile) {
		this.mobile=mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
