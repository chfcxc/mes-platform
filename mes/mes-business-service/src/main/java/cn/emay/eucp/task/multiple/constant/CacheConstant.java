package cn.emay.eucp.task.multiple.constant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.task.multiple.dto.BaseSectionNumberStore;
import cn.emay.eucp.task.multiple.dto.BlackListStore;
import cn.emay.eucp.task.multiple.dto.PortableNumberStore;
import cn.emay.eucp.task.multiple.util.BadWordFilter;

public class CacheConstant {

	/**
	 * 通道信息缓存
	 */
	public static Map<Long, FmsChannel> channelMap = new ConcurrentHashMap<Long, FmsChannel>();

	/**
	 * 服务号信息缓存
	 */
	public static Map<String, FmsServiceCode> serviceCodeMap = new ConcurrentHashMap<String, FmsServiceCode>();
	/**
	 * 服务号参数缓存
	 */
	public static Map<String, FmsServiceCodeParam> serviceCodeParamMap = new ConcurrentHashMap<String, FmsServiceCodeParam>();

	/**
	 * 路由信息缓存
	 */
	public static Map<String, String> serviceCodeChannelMap = new ConcurrentHashMap<String, String>();

	/**
	 * 强黑词组 <黑字典组id，黑词集合>
	 */
	public static Set<String> systemBlackDic = new HashSet<String>();

	/**
	 * 黑词组map <黑字典组id，黑词树>
	 */
	public static BadWordFilter systemBlackDicGroupTreeFilter = null;

	/**
	 * 通道第三方信息缓存
	 */
	public static Map<String, Object> channelInfoMap = new ConcurrentHashMap<String, Object>();

	public static BaseSectionNumberStore baseSectionNumberStore;

	public static PortableNumberStore portableNumberStore = new PortableNumberStore();

	public static BlackListStore blackListStore = new BlackListStore();

	public static Map<Long, FmsBusinessType> businessMap = new ConcurrentHashMap<Long, FmsBusinessType>();

}
