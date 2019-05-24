package cn.emay.eucp.inter.http.constant;

import cn.emay.utils.properties.PropertiesUtils;

public class HttpInterfaceConstant {

	public final static int MAX_NUMBER;

	public final static int CUSTOMIMSID_LENGTH;


	public final static int TEMPLATE_MAX_LENGTH;
	/**
	 * 模板变量最大数量
	 */
	public final static int TEMPLATE_VAR_MAX_NUM;

	public final static int TEMPLATE_VAR_MAX_LENGTH;

	public final static int TEMPLATE_LIMIT_INTERVAL;// 报备模板间隔

	public final static int TEMPLATE_LIMIT_NUM;// 报备模板间隔时间内请求次数限制

	public final static int REPORT_LIMIT_INTERVAL;// 获取状态报告间隔间隔

	public final static int REPORT_LIMIT_NUM;// 获取状态报告间隔时间内请求次数限制

	public final static int BALANCE_LIMIT_INTERVAL;// 获取余额间隔

	public final static int BALANCE_LIMIT_NUM;// 获取余额间隔时间内请求次数限制

	static {
		MAX_NUMBER = PropertiesUtils.getIntProperty("eucp.fms.interface.max.mobile", "eucp.service.properties", 300);
		CUSTOMIMSID_LENGTH = PropertiesUtils.getIntProperty("eucp.fms.interface.customImsId.length", "eucp.service.properties", 64);
		TEMPLATE_MAX_LENGTH = PropertiesUtils.getIntProperty("eucp.fms.interface.template_max_length", "eucp.service.properties", 70);
		TEMPLATE_VAR_MAX_NUM = PropertiesUtils.getIntProperty("eucp.fms.interface.template_var_max_num", "eucp.service.properties", 3);
		TEMPLATE_VAR_MAX_LENGTH = PropertiesUtils.getIntProperty("eucp.fms.interface.template_var_max_length", "eucp.service.properties", 20);
		TEMPLATE_LIMIT_INTERVAL = PropertiesUtils.getIntProperty("eucp.fms.template.limit.interval", "eucp.service.properties", 1);
		TEMPLATE_LIMIT_NUM = PropertiesUtils.getIntProperty("eucp.fms.template.limit.num", "eucp.service.properties", 1);
		REPORT_LIMIT_INTERVAL = PropertiesUtils.getIntProperty("eucp.fms.report.limit.interval", "eucp.service.properties", 1);
		REPORT_LIMIT_NUM = PropertiesUtils.getIntProperty("eucp.fms.report.limit.num", "eucp.service.properties", 10);
		BALANCE_LIMIT_INTERVAL = PropertiesUtils.getIntProperty("eucp.fms.balance.limit.interval", "eucp.service.properties", 1);
		BALANCE_LIMIT_NUM = PropertiesUtils.getIntProperty("eucp.fms.balance.limit.num", "eucp.service.properties", 10);
	}

}
