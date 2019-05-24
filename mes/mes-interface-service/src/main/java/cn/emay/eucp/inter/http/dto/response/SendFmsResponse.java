package cn.emay.eucp.inter.http.dto.response;

import java.io.Serializable;

/**
 * 单条发送返回结果
 * 
 * @author Adorkable
 *
 */
public class SendFmsResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile;
	private String fmsId;
	private String customFmsId;

	public SendFmsResponse(String mobile, String fmsId, String customFmsId) {
		this.mobile = mobile;
		this.fmsId = fmsId;
		this.customFmsId = customFmsId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

}
