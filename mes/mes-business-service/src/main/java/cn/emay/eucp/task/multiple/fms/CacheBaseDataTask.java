package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.data.service.fms.FmsBlackDictionaryService;
import cn.emay.eucp.data.service.fms.FmsBlacklistService;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsChannelInfoService;
import cn.emay.eucp.data.service.fms.FmsChannelService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeChannelService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeParamService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.system.BaseSectionNumberService;
import cn.emay.eucp.data.service.system.PortableNumberService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.eucp.task.multiple.constant.ServiceConstant;
import cn.emay.eucp.task.multiple.dto.BaseSectionNumberStore;
import cn.emay.eucp.task.multiple.dto.BlackListStore;
import cn.emay.eucp.task.multiple.dto.PortableNumberStore;
import cn.emay.eucp.task.multiple.util.BadWordFilter;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class CacheBaseDataTask implements PeriodTask {
	private Logger logger = Logger.getLogger(CacheBaseDataTask.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static BaseSectionNumberService baseSectionNumberService = BeanFactoryUtils.getBean(BaseSectionNumberService.class);
	private static PortableNumberService portableNumberService = BeanFactoryUtils.getBean(PortableNumberService.class);
	private static FmsBlackDictionaryService fmsBlackDictionaryService = BeanFactoryUtils.getBean(FmsBlackDictionaryService.class);
	private static FmsBlacklistService fmsBlacklistService = BeanFactoryUtils.getBean(FmsBlacklistService.class);
	private static FmsChannelService fmsChannelService = BeanFactoryUtils.getBean(FmsChannelService.class);
	private static FmsChannelInfoService fmsChannelInfoService = BeanFactoryUtils.getBean(FmsChannelInfoService.class);
	private static FmsServiceCodeService fmsServiceCodeService = BeanFactoryUtils.getBean(FmsServiceCodeService.class);
	private static FmsServiceCodeParamService fmsServiceCodeParamService = BeanFactoryUtils.getBean(FmsServiceCodeParamService.class);
	private static FmsServiceCodeChannelService fmsServiceCodeChannelService = BeanFactoryUtils.getBean(FmsServiceCodeChannelService.class);
	private static FmsBusinessTypeService fmsBusinessTypeService = BeanFactoryUtils.getBean(FmsBusinessTypeService.class);
	public static long lastUpdateTime;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		try {
			long startTime = System.currentTimeMillis();
			long thisUpdateTime = startTime;
			int currentPage = 1;
			int pageSize = 10000;
			logger.info("基础缓存任务开始");
			logger.info("业务类型缓存任务开始");
			List<FmsBusinessType> fmsBusinessTypes = fmsBusinessTypeService.findList();
			Map<Long, FmsBusinessType> businessMap = new ConcurrentHashMap<Long, FmsBusinessType>();
			for (FmsBusinessType fmsBusinessType : fmsBusinessTypes) {
				businessMap.put(fmsBusinessType.getId(), fmsBusinessType);
			}
			CacheConstant.businessMap = businessMap;
			logger.info("业务类型缓存初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startTime) + "ms】");
			long startBaseSectionNumber = System.currentTimeMillis();
			BaseSectionNumberStore bst = new BaseSectionNumberStore();
			List<BaseSectionNumber> baseSectionNumbers = baseSectionNumberService.findAll();
			for (BaseSectionNumber bsn : baseSectionNumbers) {
				if (!bsn.getIsDelete()) {
					bst.save(bsn);
				}
			}
			CacheConstant.baseSectionNumberStore = bst;
			long endBaseSectionNumberInfoInit = System.currentTimeMillis();
			logger.info("基础号段缓存初始化结束。。。。。。。耗时：【" + (endBaseSectionNumberInfoInit - startBaseSectionNumber) + "ms】");
			// 携号转网号码加载
			// total = 0;
			long startTimePortableNumber = System.currentTimeMillis();
			PortableNumberStore portableNumberStore = CacheConstant.portableNumberStore;
			List<PortableNumber> pns = new ArrayList<PortableNumber>();
			while (true) {
				pns = portableNumberService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null == pns || pns.size() == 0) {
					break;
				}
				portableNumberStore.save(true, pns);
				currentPage++;
			}
			CacheConstant.portableNumberStore = portableNumberStore;
			long endBaseSectionNumberInfoInitPortableNumber = System.currentTimeMillis();
			logger.info("携号转网初始化结束。。。。。。。耗时：【" + (endBaseSectionNumberInfoInitPortableNumber - startTimePortableNumber) + "ms】");

			// 黑字典初始化
			long startTimeMmsBlackDictionary = System.currentTimeMillis();
			currentPage = 1;
			Boolean flag = false;
			Set<String> blackDicContentSet = CacheConstant.systemBlackDic;
			List<FmsBlackDictionary> blackDictionary = null;
			do {
				blackDictionary = fmsBlackDictionaryService.findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(currentPage, pageSize, new Date(lastUpdateTime - 30 * 1000), null);
				if (null == blackDictionary || blackDictionary.size() == 0) {
					break;
				}
				for (FmsBlackDictionary dto : blackDictionary) {
					String lowerCase = dto.getContent().toLowerCase();
					if (dto.getIsDelete().intValue() == FmsBlacklist.IS_DELETE_TRUE) {
						blackDicContentSet.remove(lowerCase);
					} else {
						blackDicContentSet.add(lowerCase);
					}
				}
				currentPage++;
				flag = true;
			} while (pageSize == blackDictionary.size());

			if (flag) {
				BadWordFilter badWordFilter = null;
				if (blackDicContentSet.size() > 0) {
					badWordFilter = new BadWordFilter(ServiceConstant.rules, blackDicContentSet);
				}
				CacheConstant.systemBlackDic = blackDicContentSet;
				CacheConstant.systemBlackDicGroupTreeFilter = badWordFilter;
			}
			long endBaseSectionNumberInfoInitMmsBlackDictionary = System.currentTimeMillis();
			logger.info("黑字典初始化结束。。。。。。。耗时：【" + (endBaseSectionNumberInfoInitMmsBlackDictionary - startTimeMmsBlackDictionary) + "ms】");
			// 黑名单
			currentPage = 1;
			long startblackListStore = System.currentTimeMillis();
			BlackListStore blackListStore = CacheConstant.blackListStore;
			List<FmsBlacklist> blacklist = null;
			do {
				blacklist = fmsBlacklistService.findFmsBlacklistByLastUpdateTime(currentPage, pageSize, new Date(lastUpdateTime - 30 * 1000));
				if (null == blacklist || blacklist.size() == 0) {
					break;
				}
				List<Long> repeatListIds = new ArrayList<Long>();
				Set<String> repeatList = new HashSet<String>();
				for (FmsBlacklist bk : blacklist) {
					if (bk.getIsDelete() == FmsBlacklist.IS_DELETE_TRUE) {
						blackListStore.delete(bk.getMobile());
					} else {
						if (!repeatList.contains(bk.getMobile())) {
							blackListStore.save(true, bk.getMobile());
							repeatList.add(bk.getMobile());
						} else {
							repeatListIds.add(bk.getId());
						}
					}
				}
				currentPage++;
				repeatList.clear();
				CacheConstant.blackListStore = blackListStore;
			} while (pageSize == blacklist.size());
			logger.info("黑名单初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startblackListStore) + "ms】");
			// 通道信息加载
			currentPage = 1;
			long startchannel = System.currentTimeMillis();
			List<FmsChannel> channelList;
			Map<Long, FmsChannel> channelMap = CacheConstant.channelMap;
			for (;;) {
				channelList = fmsChannelService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null != channelList && channelList.size() > 0) {
					for (FmsChannel channel : channelList) {
						channelMap.put(channel.getId(), channel);
						redis.hset(RedisConstants.FMS_CHANNEL_HASH, String.valueOf(channel.getId()), channel, -1);
					}
				} else {
					break;
				}
				currentPage++;
			}
			logger.info("黑名单初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startchannel) + "ms】");
			// 第三方通道参数信息
			currentPage = 1;
			long startchannelinfo = System.currentTimeMillis();
			Map<String, Object> channelInfoMap = CacheConstant.channelInfoMap;
			Map<String, List<FmsChannelInfo>> lastUpdateChannelInfoMap = new ConcurrentHashMap<String, List<FmsChannelInfo>>();
			List<FmsChannelInfo> channelInfos;
			for (;;) {
				channelInfos = fmsChannelInfoService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null != channelInfos && channelInfos.size() > 0) {
					for (FmsChannelInfo channelInfo : channelInfos) {
						if (lastUpdateChannelInfoMap.containsKey(String.valueOf(channelInfo.getChannelId()))) {
							lastUpdateChannelInfoMap.get(String.valueOf(channelInfo.getChannelId())).add(channelInfo);
						} else {
							List<FmsChannelInfo> clist = new ArrayList<FmsChannelInfo>();
							clist.add(channelInfo);
							lastUpdateChannelInfoMap.put(String.valueOf(channelInfo.getChannelId()), clist);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
			if (!lastUpdateChannelInfoMap.isEmpty()) {
				for (Map.Entry<String, List<FmsChannelInfo>> entry : lastUpdateChannelInfoMap.entrySet()) {
					channelInfoMap.put(entry.getKey(), entry.getValue());
				}
				CacheConstant.channelInfoMap = channelInfoMap;
				redis.hmset(RedisConstants.FMS_CHANNEL_PARAM_HASH, channelInfoMap, -1);
			}
			logger.info("第三方通道参数信息初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startchannelinfo) + "ms】");
			// 服务号通道信息加载
			currentPage = 1;
			long startservicdecodechannel = System.currentTimeMillis();
			List<FmsServiceCodeChannel> fmsServiceCodeChannels = null;
			for (;;) {
				fmsServiceCodeChannels = fmsServiceCodeChannelService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null != fmsServiceCodeChannels && fmsServiceCodeChannels.size() > 0) {
					Set<String> appIdSet = new HashSet<String>();
					Map<String, String> map = new HashMap<String, String>();
					for (FmsServiceCodeChannel fmsServiceCodeChannel : fmsServiceCodeChannels) {
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",0,CMCC");
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",0,CUCC");
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",0,CTCC");
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",1,CMCC");
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",1,CUCC");
						appIdSet.add(fmsServiceCodeChannel.getAppId() + ",1,CTCC");
						map.put(fmsServiceCodeChannel.getAppId() + "," + fmsServiceCodeChannel.getTemplateType() + "," + fmsServiceCodeChannel.getOperatorCode(),
								String.valueOf(fmsServiceCodeChannel.getChannelId()));
					}
					for (String key : appIdSet) {
						CacheConstant.serviceCodeChannelMap.remove(key);
					}
					redis.hdel(RedisConstants.FMS_APPID_CHANNELID_HASH, appIdSet.toArray(new String[appIdSet.size()]));
					CacheConstant.serviceCodeChannelMap.putAll(map);
					Map<String, Object> redisMap = new HashMap<String, Object>();
					redisMap.putAll(map);
					redis.hmset(RedisConstants.FMS_APPID_CHANNELID_HASH, redisMap, -1);
				} else {
					break;
				}
				currentPage++;
			}

			logger.info("服务号通道信息初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startservicdecodechannel) + "ms】");
			// 服务号信息加载
			currentPage = 1;
			long startservicdecode = System.currentTimeMillis();
			Map<String, FmsServiceCode> serviceCodeMap = CacheConstant.serviceCodeMap;
			List<FmsServiceCode> serviceCodeList;
			for (;;) {
				serviceCodeList = fmsServiceCodeService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null != serviceCodeList && serviceCodeList.size() > 0) {
					for (FmsServiceCode fmsServiceCode : serviceCodeList) {
						String appId = fmsServiceCode.getAppId();
						serviceCodeMap.put(appId, fmsServiceCode);
						redis.hset(RedisConstants.FMS_SERVICE_CODE_HASH, appId, fmsServiceCode, -1);
					}
				} else {
					break;
				}
				currentPage++;
			}
			logger.info("服务号信息初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startservicdecode) + "ms】");
			// 服务号参数加载
			currentPage = 1;
			long startservicdecodeParams = System.currentTimeMillis();
			Map<String, FmsServiceCodeParam> serviceCodeParamMap = CacheConstant.serviceCodeParamMap;
			List<FmsServiceCodeParam> serviceCodeParamList;
			for (;;) {
				serviceCodeParamList = fmsServiceCodeParamService.findByLastUpdateTime(new Date(lastUpdateTime - 30 * 1000), currentPage, pageSize);
				if (null != serviceCodeParamList && serviceCodeParamList.size() > 0) {
					for (FmsServiceCodeParam fmsServiceCodeParam : serviceCodeParamList) {
						String appId = fmsServiceCodeParam.getAppId();
						serviceCodeParamMap.put(appId, fmsServiceCodeParam);
						redis.hset(RedisConstants.FMS_SERVICE_CODE_PARAM_HASH, appId, fmsServiceCodeParam, -1);
					}
				} else {
					break;
				}
				currentPage++;
			}

			logger.info("服务号参数初始化结束。。。。。。。耗时：【" + (System.currentTimeMillis() - startservicdecodeParams) + "ms】");
			long endTime = System.currentTimeMillis();
			logger.info("基础数据缓存任务结束。耗时：【" + (endTime - startTime) + "ms】");
			lastUpdateTime = thisUpdateTime;
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			logger.error("【CacheBaseDataTask】--", e);
			return TaskResult.doBusinessFailResult();
		}

	}

	@Override
	public long period() {
		return 60 * 1000L;// 30s更新一次
	}
}
