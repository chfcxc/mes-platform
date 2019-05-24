package cn.emay.eucp.common.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EucpFmsMessageState {
	// //////////////////////////////////////////////////////////////////
	// **************************最新状态列表 **************************//
	// //////////////////////////////////////////////////////////////////
	SENDING("发送中", "SENDING"), //
	/**/SEND_TO_CHANNELSERVICE("提交至通道服务", "SEND_TO_CHANNELSERVICE"), //
	SEND_TO_OPERATOR("发送到运营商", "SEND_TO_OPERATOR"), //
	FAIL("失败", "FAIL"), //
	/**/SEND_FAIL("发送失败", "SEND_FAIL"), // 和以下的明细失败
	/**/SEND_SUCCESS("状态报告成功", "SEND_SUCCESS"), //
	TIMEOUT("超时", "TIMEOUT"), //
	RESPONSE_SUCCESS("下行响应成功", "RESPONSE_SUCCESS"), //
	// 明细失败
	DELIVRD("成功", "DELIVRD"), //
	ERROR_BALANCE("余额不足", "ERROR_BALANCE"), // 策略服务
	ERROR_MOBILE("号码错误", "ERROR_MOBILE"), // 策略服务
	ERROR_SECTION_NUMBER("号段错误", "ERROR_SECTION_NUMBER"), // 策略服务
	ERROR_NO_CHANNEL("号码无通道", "ERROR_NO_CHANNEL"), // 策略服务
	ERROR_REPEAT_MOBILE("手机号重复", "ERROR_REPEAT_MOBILE"), // 策略服务
	SYSTEM_BLACK_DICT("黑字典策略验证失败", "SYSTEM_BLACK_DICT"), // 策略服务
	SYSTEM_BLACK_GLOBAL("黑名单策略验证失败", "SYSTEM_BLACK_GLOBAL"), // 策略服务
	SERVICE_CODE_INTERCEPT("拦截规则失败", "SERVICE_CODE_INTERCEPT"), // 发送服务
	REPREAT_SEND_FILTER("发送频率验证失败", "REPREAT_SEND_FILTER"), // 发送服务
	RESPONSE_FAIL("下行响应失败", "RESPONSE_FAIL"), // 发送服务
	RESPONSE_TIMEOUT("下行响应超时", "RESPONSE_TIMEOUT"), // 发送服务
	ERROR_OPERATOR_ALLOCATION("运营商分配错误", "ERROR_OPERATOR_ALLOCATION"), // 策略服务异常
	ERROR_STRATEGY_SERVICE("策略服务异常", "ERROR_STRATEGY_SERVICE"), // 策略服务异常
	ERROR_MOBILE_NOT_AREA("号码不在区域内", "ERROR_MOBILE_NOT_AREA"), // 策略服务异常
	ERROPR_TEMPLATE_CHANNEL("模板无绑定通道", "ERROPR_TEMPLATE_CHANNEL"), // 策略服务异常
	ERROPR_TEMPLATE_CHANNEL_STATE("模板绑定通道状态错误", "ERROPR_TEMPLATE_CHANNEL_STATE"), // 策略服务异常
	ERROPR_CONTNET_LENGTH("内容过长", "ERROPR_CONTNET_LENGTH"), // 策略服务异常
	ERROPR_CONTNET_PARAM_LENGTH("内容参数过长", "ERROPR_CONTNET_PARAM_LENGTH");// 策略服务异常
	// //////////////////////////////////////////////////////////////////
	// **************************最新状态列表 **************************//
	// //////////////////////////////////////////////////////////////////
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	/**
	 * enum lookup map
	 */
	private static final Map<String, String> LOOK_UP = new HashMap<String, String>();

	static {
		for (EucpFmsMessageState s : EnumSet.allOf(EucpFmsMessageState.class)) {
			LOOK_UP.put(s.getCode(), s.getName());
		}
	}

	private EucpFmsMessageState(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (EucpFmsMessageState oc : EucpFmsMessageState.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (EucpFmsMessageState oc : EucpFmsMessageState.values()) {
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

	public static Map<String, String> lookup() {
		return LOOK_UP;
	}
}
