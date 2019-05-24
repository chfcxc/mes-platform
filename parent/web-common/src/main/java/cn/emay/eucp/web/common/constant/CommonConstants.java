package cn.emay.eucp.web.common.constant;

import cn.emay.util.PropertiesUtil;

public class CommonConstants {

	/**
	 * 验证码COOKIE name 前缀
	 */
	public final static String CAPTCHA_COOKIE_PER = "CAPTCHA_";

	/**
	 * PASSPORT地址
	 */
	public final static String PASSPORT_PATH = PropertiesUtil.getProperty("web.passport.path", "eucp.service.properties");

	/**
	 * PASSPORT地址
	 */
	public final static String LocalPASSPORT_PATH = PropertiesUtil.getProperty("local.web.passport.path", "eucp.service.properties");
	/**
	 * 當前WEB所属系统
	 */
	public final static String LOCAL_SYSTEM = PropertiesUtil.getProperty("local.system", "eucp.service.properties");

	/**
	 * WEB静态文件地址
	 */
	public final static String WEB_STATIC_PATH = PropertiesUtil.getProperty("web.static.path", "eucp.service.properties");

	public final static String WEB_BUSINESS_CODE = PropertiesUtil.getProperty("system.business.code", "eucp.service.properties");

	/**
	 * 特殊符号转义
	 */
	public final static String DOUBLE_QUOTATION_MARKS = "@EMAY" + (int) '"' + "PAGE@";

	public final static String SINGLE_QUOTATION_MARKS = "@EMAY" + (int) '\'' + "PAGE@";

	public final static String LESS_THAN_MARKS = "@EMAY" + (int) '<' + "PAGE@";

	public final static String MORE_THAN_MARKS = "@EMAY" + (int) '>' + "PAGE@";

	public final static String LOGIC_AND_MARKS = "@EMAY" + (int) '&' + "PAGE@";

	public final static String POINT_MARKS = "@EMAY" + (int) '`' + "PAGE@";

	public static void main(String[] args) {
		System.out.println("双引号：" + DOUBLE_QUOTATION_MARKS);
		System.out.println("单引号：" + SINGLE_QUOTATION_MARKS);
		System.out.println("小于号：" + LESS_THAN_MARKS);
		System.out.println("大于号：" + MORE_THAN_MARKS);
		System.out.println("逻辑于号：" + LOGIC_AND_MARKS);
		System.out.println("点符号：" + POINT_MARKS);
	}

}
