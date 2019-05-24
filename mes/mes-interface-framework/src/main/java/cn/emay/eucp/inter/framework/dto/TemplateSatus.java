package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

/**
 * 模板每个运营商通道报备状态
 * 
 * @author dinghaijiao
 *
 */
public class TemplateSatus implements Serializable {

	public static final int REPORT_DOIND = 0;// 报备中(提交报备,报备中)
	public static final int REPORT_OK = 1;// 报备成功
	public static final int REPORT_ERROR = 2;// 报备失败
	public static final int NOT_SUPPORT = 3;// 不支持 (不支持,报备超时,未配置通道,未报备)

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cmcc;
	private Integer cucc;
	private Integer ctcc;

	public TemplateSatus(Integer cmcc, Integer cucc, Integer ctcc) {
		this.cmcc = cmcc;
		this.cucc = cucc;
		this.ctcc = ctcc;

	}

	public TemplateSatus() {
	}

	public Integer getCmcc() {
		return cmcc;
	}

	public void setCmcc(Integer cmcc) {
		this.cmcc = cmcc;
	}

	public Integer getCucc() {
		return cucc;
	}

	public void setCucc(Integer cucc) {
		this.cucc = cucc;
	}

	public Integer getCtcc() {
		return ctcc;
	}

	public void setCtcc(Integer ctcc) {
		this.ctcc = ctcc;
	}

}
