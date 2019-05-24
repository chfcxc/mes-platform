package cn.emay.channel.framework.dto;

import java.util.HashMap;
import java.util.Map;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.util.PropertiesUtil;

/** @author yudejun
 * @version 创建时间:2018年11月20日 下午2:55:57 类说明 */
public class Constants {

	public final static String CHANNEL_TYPE = PropertiesUtil.getProperty("channel.type", "channel.properties");

	public final static String SEND_SERVICE_HOST = PropertiesUtil.getProperty("send.service.host", "channel.properties");

	public final static String MMS_FILE_PATH = PropertiesUtil.getProperty("mms_file_path", "channel.properties");

	public final static int REPORT_TIMEOUT_DAY = PropertiesUtil.getIntProperty("timeout.day", "channel.properties", 2);
	/** 报备审核超时时间 **/
	public final static int AUDIT_REPORT_TIMEOUT_DAY = PropertiesUtil.getIntProperty("timeoutaudit.day", "channel.properties", 5);

	public final static int QUERY_REPORT_NUM = PropertiesUtil.getIntProperty("query.report.num", "channel.properties", 500);

	public final static String CHANNEL_START = "START";

	public final static String CHANNEL_STOP = "STOP";

	public final static String DELIVRD = "DELIVRD";

	public final static String FAIL = "FAIL";
	/** redis链接 */
	public static RedisClient REDIS;

	// 协议类型
	public final static String MM7 = "MM7";

	public static final String MARK = "#";

	public static final String REPREAT_SEND_FILTER = "REPREAT_SEND_FILTER";

	/** 待发送FQ文件队列 */
	public static String WAITSENDQUEUE = "WaitSendQueue";
	/** 下发响应FQ文件队列 */
	public static String RESPONSEMTQUEUE = "ResponseMtQueue";
	/** 上行FQ文件队列 */
	public static String MOQUEUE = "MoQueue";
	/** 状态报告待比对FQ文件队列 */
	public static String REPORTQUEUE = "ReportQueue";
	/** 审核状态报告待比对FQ文件队列 */
	public static String AUDITREPORTQUEUE = "AuditReportQueue";

	public static String SEND_MESSAGE_RESPONSE = "sendMessageResponse";
	public static String SEND_MESSAGE_REPORT = "sendMessageReport";

	public static String REPORT_TIMEOUT_STATE = "REPORT_TIMEOUT";
	public static String REPORT_TIMEOUT_DESC = "状态报告超时";

	public static Long REPEAT_FILTER_TASK_SLEEP = 500L;

	public static String HAS_RESPONSE_HASH = "HAS_RESPONSE_HASH_";
	public static String HAS_RESPONSE_ZSET = "HAS_RESPONSE_ZSET_";
	public final static String NO_DATA = "NODATA";

	public final static String NO_SMS_CHANNEL_INFO = "NOSmsChannelInfo";
	public final static Long REPEAT_COMPARISON_REPORT = 30L;

	public static final String RESPONSE_TIMEOUT = "RESPONSE_TIMEOUT";
	public static final String RESPONSE_FAIL = "RESPONSE_FAIL";
	public static final String RESPONSE_SUCCESS = "RESPONSE_SUCCESS";

	public static final String STATISTIC_SMS_MESSAGE_SEND_OUT_QUEUE_COUNT_KEY = "SMS_MESSAGE_SEND_OUT_NUM";
	/** 短信监控常量 **/
	public static final String MONITOR_SPLIT = "#";
	// 校正比例
	public static int CORRECT_PROPORTION = 0;
	// 校正状态报告超时时间
	public static int CORRECT_TIME = 0;

	public static int IS_OPEN = 0;

	public static void main(String[] args) {
		System.out.println(2);
		Map<Long, Long> enterpriseMap = new HashMap<Long, Long>();
		System.out.println(enterpriseMap.keySet());
	}

}
