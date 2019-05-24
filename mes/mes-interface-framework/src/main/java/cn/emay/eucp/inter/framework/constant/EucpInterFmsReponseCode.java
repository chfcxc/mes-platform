package cn.emay.eucp.inter.framework.constant;

public enum EucpInterFmsReponseCode {
	/**
	 * 提交成功
	 */
	SUCCESS("提交成功", "SUCCESS"), //
	/**
	 * AppId错误
	 */
	ERROR_APPID("AppId错误", "ERROR_APPID"), //
	/**
	 * 请求参数错误
	 */
	ERROR_PARAMS("请求参数错误", "ERROR_PARAMS"), //
	/**
	 * 不识别的Ip
	 */
	ERROR_CLIENT_IP("不识别的Ip", "ERROR_CLIENT_IP"), //
	/**
	 * 不识别的Ip
	 */
	ERROR_OVER_SPEED("请求超速", "ERROR_OVER_SPEED"), //
	/**
	 * 手机号为空
	 */
	ERROR_MOBILE_EMPTY("手机号为空", "ERROR_MOBILE_EMPTY"), //
	/**
	 * 号码数量过多
	 */
	ERROR_MOBILE_NUMBER("号码数量过多", "ERROR_MOBILE_NUMBER"), //
	/**
	 * 手机号码错误
	 */
	ERROR_MOBILE_ERROR("手机号码错误", "ERROR_MOBILE_ERROR"), //
	/**
	 * 短信内容为空
	 */
	ERROR_CONTENT_EMPTY("短信内容为空", "ERROR_CONTENT_EMPTY"), //
	/**
	 * 短信内容过长
	 */
	ERROR_LONG_CONTENT("短信内容过长", "ERROR_LONG_CONTENT"), //
	/**
	 * 模板ID错误
	 */
	ERROR_TEMPLATE_ID_ERROR("模板ID错误", "ERROR_TEMPLATE_ID_ERROR"), //
	/**
	 * 模板ID为空
	 */
	ERROR_TEMPLATE_ID_EMPTY("模板ID为空", "ERROR_TEMPLATE_ID_EMPTY"), //
	/**
	 * 模板为空
	 */
	ERROR_TEMPLATE_EMPTY("模板为空", "ERROR_TEMPLATE_EMPTY"), //
	/**
	 * 模板超过长度
	 */
	ERROR_TEMPLATE_ABOVE_LENGTH("模板超过长度", "ERROR_TEMPLATE_ABOVE_LENGTH"), //
	/**
	 * 模板变量过多
	 */
	ERROR_TEMPLATE_VAR_MORE("模板变量过多", "ERROR_TEMPLATE_VAR_MORE"), //
	/**
	 * 模板变量重复
	 */
	ERROR_TEMPLATE_VAR_REPEAT("模板变量重复", "ERROR_TEMPLATE_VAR_REPEAT"), //
	/**
	 * 模板变量错误
	 */
	ERROR_TEMPLATE_VAR_ERROR("模板变量错误", "ERROR_TEMPLATE_VAR_ERROR"), //
	/**
	 * 模板变量超长
	 */
	ERROR_TEMPLATE_VAR_ABOVE_LENGTH("模板变量超长", "ERROR_TEMPLATE_VAR_ABOVE_LENGTH"), //
	/**
	 * 该APPID没有支持的通道
	 */
	ERROR_APPID_NO_SUPPORT_CHANNEL("该APPID没有支持的通道", "ERROR_APPID_NO_SUPPORT_CHANNEL"), //
	/**
	 * 自定义消息ID过长
	 */
	ERROR_CUSTOM_IMSID("自定义消息ID过长", "ERROR_CUSTOM_IMSID"), //
	/**
	 * 时间戳错误
	 */
	ERROR_TIMESTAMP("时间戳错误", "ERROR_TIMESTAMP"), //
	/**
	 * 签名错误
	 */
	ERROR_SIGN("签名错误", "ERROR_SIGN"), //
	/**
	 * 请求有效时间错误
	 */
	ERROR_REQUESTVALIDPERIOD("请求有效时间错误", "ERROR_REQUESTVALIDPERIOD"), //
	/**
	 * 请求时间错误
	 */
	ERROR_REQUESTTIME("请求时间错误", "ERROR_REQUESTTIME"),
	/**
	 * 请求时间超时
	 */
	ERROR_REQUESTTIME_TIMEOUT("请求时间超时", "ERROR_REQUESTTIME_TIMEOUT"); //
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private EucpInterFmsReponseCode(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (EucpInterFmsReponseCode oc : EucpInterFmsReponseCode.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (EucpInterFmsReponseCode oc : EucpInterFmsReponseCode.values()) {
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
