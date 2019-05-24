package cn.emay.eucp.inter.framework.constant;

import cn.emay.util.PropertiesUtil;

public class InterfaceConstant {

	public static int GET_REPORT_MAX_NUMBER = PropertiesUtil.getIntProperty("get.report.max.number", "eucp.service.properties", 500);

	public static int IMS_CONTENT_MAX_LENGTH = PropertiesUtil.getIntProperty("ims.content.max.length", "eucp.service.properties", 500);

}
