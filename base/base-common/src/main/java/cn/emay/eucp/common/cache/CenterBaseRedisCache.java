package cn.emay.eucp.common.cache;

public class CenterBaseRedisCache {

	/** 用户重置密码缓存记录 */
	public static final String SYSTEM_USER_RESET_PASSWORD = "SYSTEM_USER_RESET_PASSWORD_";

	/** 保存最新更新信息 */
	public static final String SYSTEM_UPDATE_INFO_NEW = "SYSTEM_UPDATE_INFO_NEW";

	/** 携号转网已经初始化标识 */
	public final static String PORTABLENUMBER_HAD_INITED = "PORTABLENUMBER_HAD_INITED";

	/** 强制更新日期队列 */
	public static final String UPDATE_PORTABLE_NUMBER_QUEUE = "UPDATE_PORTABLE_NUMBER_QUEUE";
	
	/**
	 * 全量权限资源
	 */
	public static final String SYSTEM_RESOURCE_ALL = "SYSTEM_RESOURCE_ALL";

}
