package cn.emay.eucp.common.dto.fms.mt;

import java.io.Serializable;

public class FmsIdAndMobile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fmsId;
	
	private String customFmsId;
	
	private String mobile;
	
	private String content;// 变量内容

	public FmsIdAndMobile() {

	}

	
	public FmsIdAndMobile(String fmsId, String customFmsId, String mobile, String content) {

		this.fmsId = fmsId;
		this.customFmsId = customFmsId;
		this.mobile = mobile;
		this.content = content;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
