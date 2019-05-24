package cn.emay.eucp.task.multiple.constant;

import cn.emay.util.PropertiesUtil;

public class CommonConstanct {

	// 页面发送文件路径
	public static String pageSendFilePath = PropertiesUtil.getProperty("page.send.mobile.file.path", "eucp.service.properties");
	// 接口代码
	public static String INTERFACE_CODE = PropertiesUtil.getProperty("eucp.interface.code", "eucp.service.properties");
	// 页面发送批次数量
	public static int pageSendBatchNumber = PropertiesUtil.getIntProperty("page.send.batch.number", "eucp.service.properties", 300);
	// 国家代码最大长度
	public static int countryCodeMaxLength = PropertiesUtil.getIntProperty("country.code.max.length", "eucp.service.properties", 4);
	// 短信内容最大长度
	public static int IMS_CONTENT_MAX_LENGTH = PropertiesUtil.getIntProperty("ims.content.max.length", "eucp.service.properties", 500);
	// 国际短信默认国家报价 单位：厘
	public static Long IMS_DEFAULT_COUNTRY_PRICE = PropertiesUtil.getLongProperty("ims.default.country.price", "eucp.service.properties", 10000L);
	// 默认短信拆分字数
	public static int DEFAULT_IMS_SPLIT_WORD_NUMBER = PropertiesUtil.getIntProperty("default.ims.split.word.number", "eucp.service.properties", 70);
	// 手机号最小长度
	public static int FMS_MOBILE_MIN_LENGTH = PropertiesUtil.getIntProperty("ims.mobile.min.length", "eucp.service.properties", 4);
	// 报表时间
	public static int IMS_REPORT_TIME = PropertiesUtil.getIntProperty("timeout.day", "eucp.service.properties", 2);
	// 国际号码前缀
	public static String IMS_MOBILE_PREFIX = "00";
	// 默认国家编号
	public static String DEFAULT_COUNTRY_NUMBER = "000";
	// 金额保留小数点后位数
	public static int AMOUNT_RETAINED_DECIMAL_POINT = 3;

	// 回滚sql文件路径
	public static String messageSqlFilePath = PropertiesUtil.getProperty("messagesql.save.file.path", "eucp.service.properties");

	public static int PUSH_REPORT_NUM = PropertiesUtil.getIntProperty("push.report.num", "eucp.service.properties", 300);

	public static int serviceCodeThreadNum = PropertiesUtil.getIntProperty("servicecode.thread.num", "eucp.service.properties", 1);

	public static String SUCCESS_CODE = "success";
	public static int FMS_REPORT_TIME = PropertiesUtil.getIntProperty("report.time", "eucp.service.properties", 3);

	public static boolean INIT_TEMPLATE_DATA = PropertiesUtil.getBooleanProperty("init.template.data", "eucp.service.properties", false);

}
