package cn.emay.eucp.common.constant;

/**
 * 全局常量定义
 * 
 * @author hqbzl
 */
public interface RedisConstants {

	/** 服务号Hash */
	public static final String FMS_SERVICE_CODE_HASH = "FMS_SERVICE_CODE_HASH";
	/** 服务号参数Hash */
	public static final String FMS_SERVICE_CODE_PARAM_HASH = "FMS_SERVICE_CODE_PARAM_HASH";

	/** 客户状态报告队列 */
	public static final String FMS_REPORT_QUEUE = "FMS_REPORT_QUEUE_";

	/** 客户状态报告接收队列 */
	public static final String FMS_REPORT_RECEIVE_RECORD_QUEUE = "FMS_REPORT_RECEIVE_RECORD_QUEUE";
	/** 服务号余额Hash */
	public static final String FMS_SERVICE_CODE_BALANCE_HASH = "FMS_SERVICE_CODE_BALANCE_HASH";

	/** 模板保存队列 */
	public static final String FMS_TEMPLATE_SAVE_QUEUE = "FMS_TEMPLATE_SAVE_QUEUE";

	/** 详情Hash */
	public static final String FMS_MESSAGE_HASH = "FMS_MESSAGE_HASH";
	/** 发送队列 */
	public static final String FMS_MESSAGE_QUEUE = "FMS_MESSAGE_QUEUE";
	/** appId 与模板id的关系hash **/
	public static final String FMS_APPID_TEMPLATEID_HASH = "FMS_APPID_TEMPLATEID_HASH";
	/** 模板id与channelID的报备结果hash **/
	public static final String FMS_TEMPLATEID_CHANNELID_HASH = "FMS_TEMPLATEID_CHANNELID_HASH";
	/** appId与channelID的报备结果hash **/
	public static final String FMS_APPID_CHANNELID_HASH = "FMS_APPID_CHANNELID_HASH";
	/** 模板Hash */
	public static final String FMS_TEMPLATE_HASH = "FMS_TEMPLATE_HASH";
	/** 模板待报备队列 */
	public static final String FMS_TEMPLATE_WAITING_REPORT_QUEUE = "FMS_TEMPLATE_WAITING_REPORT_QUEUE";
	/** 详情入库批次队列 */
	public static final String FMS_SAVE_DB_QUEUE = "FMS_SAVE_DB_QUEUE";
	public static final String SEND_FMS_HASH = "SEND_FMS_HASH";

	public static final String FMS_SAVE_DB_QUEUE_FAIL = "FMS_SAVE_DB_QUEUE_FAIL";
	/** 详情更新批次队列 */
	public static final String FMS_UPDATE_DB_QUEUE = "FMS_UPDATE_DB_QUEUE";

	/*
	 * 通道心跳hash
	 */
	public static final String FMS_CHANNEL_HEARTBEAT_HASH = "FMS_CHANNEL_HEARTBEAT_HASH";

	/*
	 * 通道命令hash
	 */
	public static final String FMS_CHANNEL_COMMAND_HASH = "FMS_CHANNEL_COMMAND_HASH";

	/*
	 * 通道hash
	 */
	public static final String FMS_CHANNEL_HASH = "FMS_CHANNEL_HASH";

	/*
	 * 通道自定义参数hash
	 */
	public static final String FMS_CHANNEL_PARAM_HASH = "FMS_CHANNEL_PARAM_HASH";

	/** 页面redis生成流水号 */
	public static final String CENTER_COUNT_SERIAL_NUMBER = "CENTER_COUNT_SERIAL_NUMBER";
	/** 页面发送hash */
	public static final String FMS_PAGE_SEND_HASH = "FMS_PAGE_SEND_HASH";
	/** 页面发送队列 */
	public static final String FMS_PAGE_SEND_QUEUE = "FMS_PAGE_SEND_QUEUE";
	/** 通道模板报备队列 */
	public static final String CHANNEL_REPORT_TEMPLATE_QUEUE = "CHANNEL_REPORT_TEMPLATE_QUEUE_";
	/** 待对比状态报告hash */
	public static final String FMS_WAIT_REPORT_HASH = "FMS_WAIT_REPORT_HASH_";
	/** 状态报告超时检测zset */
	public static final String FMS_WAIT_REPORT_ZSET = "FMS_WAIT_REPORT_ZSET_";
	/** 通道报备响应和更新状态队列 */
	public static final String CHANNEL_RESPONSE_TEMPLATE_QUEUE = "CHANNEL_RESPONSE_TEMPLATE_QUEUE";
	/** 通道模板报备超时zset */
	public static final String FMS_WAIT_AUDIT_REPORT_ZSET = "FMS_WAIT_AUDIT_REPORT_ZSET";
	/** 模板待对比状态报告hash */
	public static final String FMS_WAIT_AUDIT_REPORT_HASH = "FMS_WAIT_AUDIT_REPORT_HASH";
	/** 通道待发送队列 */
	public static final String SEND_FMS_CHANNEL_QUEUE = "SEND_FMS_CHANNEL_QUEUE";
	/** 调度队列(页面发送相关，兼容定时) */
	public static final String FMS_SCHEDULING_QUEUE = "FMS_SCHEDULING_QUEUE";
	/** 页面发送待发送队列 */
	public static final String FMS_PAGE_SEND_WAITING_SEND_QUEUE = "FMS_PAGE_SEND_WAITING_SEND_QUEUE";
	/** 页面发送待发送批次号队列 */
	public static final String FMS_PAGESEND_SERIALNUMBER_QUEUE = "FMS_PAGESEND_SERIALNUMBER_QUEUE";
}
