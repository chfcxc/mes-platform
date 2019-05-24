package cn.emay.eucp.inter.framework.core;

import cn.emay.eucp.common.excepiton.EucpRuntimeException;
import cn.emay.store.memory.MemoryMap;
import cn.emay.store.redis.RedisClient;
import cn.emay.utils.properties.PropertiesUtils;

/** 接口框架
 * 
 * @author Frank */
public class FmsInterfaceFrameWork {

	/** 存储 */
	private static FmsInterfaceDataStore store;

	/** 服务 */
	private static FmsInterfaceService service;

	/** 接口编码 */
	private static String INTERFACE_CODE;

	/** 参数缓存时间 */
	private static int paramsCacheTime;

	/** 缓存 */
	private static MemoryMap vc;

	/** 是否已经初始化 */
	private static boolean isInited = false;

	/** redis */
	private static RedisClient redis;

	/** 初始化，使用之前需要初始化
	 * 
	 * @param redis
	 */
	public synchronized static void init(RedisClient redis) {
		if (isInited == true) {
			return;
		}
		if (redis == null) {
			throw new EucpRuntimeException("redis in InterfaceContext must be not null !");
		}
		FmsInterfaceFrameWork.redis = redis;
		INTERFACE_CODE = PropertiesUtils.getProperty("eucp.interface.code", "eucp.service.properties");
		if (INTERFACE_CODE == null) {
			throw new EucpRuntimeException("eucp.interface.code in eucp.service.properties must be not null !");
		}
		paramsCacheTime = PropertiesUtils.getIntProperty("params.cache.time", "eucp.service.properties", 30);
		vc = new MemoryMap(30);
		FmsInterfaceFrameWork.store = new FmsInterfaceDataStore(paramsCacheTime, vc);
		FmsInterfaceFrameWork.service = new FmsInterfaceService();
		isInited = true;
	}

	public synchronized static void destroy() {
		isInited = false;
		FmsInterfaceFrameWork.redis = null;
		if (vc != null) {
			// 此处vc没有关闭
		}
	}

	/** 存储 */
	public static FmsInterfaceDataStore getDataStore() {
		return store;
	}

	/** 服务 */
	public static FmsInterfaceService getService() {
		return service;
	}

	/** 接口编码 */
	public static String getInterfaceCode() {
		return INTERFACE_CODE;
	}

	/** redis
	 * 
	 * @return */
	public static RedisClient getRedis() {
		return redis;
	}

}
