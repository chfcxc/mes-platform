package cn.emay.eucp.inter.http.dto;

import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;

public class CheckResult<T>{
	private EucpInterFmsReponseCode code;
	private T data;
	private String log;
	
	public CheckResult(EucpInterFmsReponseCode code, T data, String log) {
		this.code = code;
		this.setData(data);
		this.log = log;
	}
	
	public EucpInterFmsReponseCode getCode() {
		return code;
	}

	public void setCode(EucpInterFmsReponseCode code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}
