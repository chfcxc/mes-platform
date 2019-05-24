package cn.emay.eucp.inter.framework.constant;

public enum EucpInterFmsReportCode {
	// 状态码
	DELIVRD("成功", "DELIVRD", new String[] { "DELIVRD" }), //
	TIMEOUT("运营商状态报告超时", "TIMEOUT", new String[] { "TIMEOUT" }), //
	FAIL_BALANCE("余额不足", "FAIL_BALANCE", new String[] { "ERROR_BALANCE" }), //
	FAIL_MOBILE("手机号错误", "FAIL_MOBILE", new String[] { "ERROR_MOBILE" }), //
	FAIL_MOBILE_SUPPORT("手机号不支持", "FAIL_MOBILE_SUPPORT", new String[] { "ERROR_NO_CHANNEL", "ERROR_SECTION_NUMBER" }), //
	FAIL_MOBILE_REPEAT("手机号重复", "FAIL_MOBILE_REPEAT", new String[] { "ERROR_REPEAT_MOBILE" }), //
	FAIL_BLACK_DICT("黑字典失败", "FAIL_BLACK_DICT", new String[] { "SYSTEM_BLACK_DICT" }), //
	FAIL_RESPONSE("运营商响应失败", "FAIL_RESPONSE", new String[] { "RESPONSE_FAIL", "RESPONSE_TIMEOUT" }), //
	FAIL_UNKNOW("未知失败", "FAIL_UNKNOW", new String[] { "ERROR_STRATEGY_SERVICE" }), //
	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private String[] messageStates;

	private EucpInterFmsReportCode(String name, String code, String[] messageStates) {
		this.name = name;
		this.code = code;
		this.messageStates = messageStates;
	}

	public static String findNameByCode(String code) {
		for (EucpInterFmsReportCode oc : EucpInterFmsReportCode.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (EucpInterFmsReportCode oc : EucpInterFmsReportCode.values()) {
			if (oc.getName().equals(name)) {
				return oc.getCode();
			}
		}
		return null;
	}

	public static EucpInterFmsReportCode findByEucpSmsMessageState(String eucpSmsMessageState) {
		for (EucpInterFmsReportCode oc : EucpInterFmsReportCode.values()) {
			if (oc.getMessageStates() == null || oc.getMessageStates().length == 0) {
				continue;
			}
			for (String e : oc.getMessageStates()) {
				if (e.equals(eucpSmsMessageState)) {
					return oc;
				}
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String[] getMessageStates() {
		return messageStates;
	}

}
