package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

public class CustomFmsIdAndMobile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customFmsId;
	
	private String mobile;
	
	private String content;

	public CustomFmsIdAndMobile(){
		
	}
	
	public CustomFmsIdAndMobile(String customFmsId, String mobile, String content) {
		this.customFmsId = customFmsId;
		this.mobile = mobile;
		this.content = content;
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
