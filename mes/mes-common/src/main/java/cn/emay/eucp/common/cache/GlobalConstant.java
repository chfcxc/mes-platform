package cn.emay.eucp.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;

public class GlobalConstant {
	/**
	 * 模板最大长度
	 */
	public static final int TEMPLATE_MAX_LENGTH = 70;
	/**
	 * 模板变量最大数量
	 */
	public static final int TEMPLATE_VAR_MAX_NUM = 3;
	/**
	 * 模板变量最大长度
	 */
	public static final int TEMPLATE_VAR_MAX_LENGTH = 20;

	public static final String[] OPERATOR_CODES = { "CMCC", "CUCC", "CTCC" };

	public static Map<Integer, String> auditMap = new ConcurrentHashMap<Integer, String>();

	static {
		auditMap.put(FmsTemplate.NOT_REPORT, "未报备");
		auditMap.put(FmsTemplate.NOT_SET_CHANNEL, "未配置通道");
		auditMap.put(FmsTemplate.NOT_SUPPORT, "不支持");
		auditMap.put(FmsTemplate.REPORT_DOIND, "报备中");
		auditMap.put(FmsTemplate.REPORT_ERROR, "报备失败");
		auditMap.put(FmsTemplate.REPORT_OK, "报备成功");
		auditMap.put(FmsTemplate.REPORT_SUBMIT, "提交报备");
		auditMap.put(FmsTemplate.REPORT_TIMEOUT, "报备超时");
	}
}
