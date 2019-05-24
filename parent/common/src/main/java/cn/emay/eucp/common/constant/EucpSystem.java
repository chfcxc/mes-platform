package cn.emay.eucp.common.constant;

public enum EucpSystem {

	管理系统("管理系统", "MANAGE"), //
	客户系统("客户系统", "CLIENT"), //
	运维系统("运维系统", "OPERS"),  //
	销售系统("销售系统", "SALES"),  //
	代理商系统("代理商系统", "AGENT")  //

	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private EucpSystem(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (EucpSystem oc : EucpSystem.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (EucpSystem oc : EucpSystem.values()) {
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
