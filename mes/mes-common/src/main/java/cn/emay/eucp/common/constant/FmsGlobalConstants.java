package cn.emay.eucp.common.constant;

/** 全局常量定义
 * 
 * @author dejun */
public interface FmsGlobalConstants {

	// 页面参数
	public final static int DEFAULT_PAGE_START = 0;
	public final static int DEFAULT_PAGE_HB_START = 1;
	public final static int DEFAULT_PAGE_LIMIT = 20;

	// 通道启动命令
	public final static String CHANNEL_COMMAND_START = "start";
	// 通道停止命令
	public final static String CHANNEL_COMMAND_STOP = "stop";
	// 导出默认分页查询记录数
	public final static int EXPORT_DEFAULT_PAGE_LIMIT = 1000;
	// 导出内存中缓存记录数
	public final static int EXPORT_CACHE_SIZE = 1000;
	// 接收参数日期的时间格式
	public final static String PARAMETER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String[] CHANNELS = { RedisConstants.SEND_FMS_CHANNEL_QUEUE };

}
