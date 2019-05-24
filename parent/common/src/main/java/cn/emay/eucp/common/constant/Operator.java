package cn.emay.eucp.common.constant;

/**
 * 运营商代码
 * 
 * @author Frank
 *
 */
public enum Operator {

	移动("中国移动", "CMCC"), //
	联通("中国联通", "CUCC"), //
	电信("中国电信", "CTCC")  //

	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private Operator(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (Operator oc : Operator.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (Operator oc : Operator.values()) {
			if (oc.getName().equals(name)) {
				return oc.getCode();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
