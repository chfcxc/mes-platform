package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.google.gson.reflect.TypeToken;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.EucpFmsMessageState;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.dto.fms.mt.SendFmsData;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.eucp.task.multiple.constant.CommonConstanct;
import cn.emay.eucp.task.multiple.constant.DataLazyLoading;
import cn.emay.eucp.task.multiple.dto.BaseSectionNumberStore;
import cn.emay.eucp.task.multiple.dto.BlackListStore;
import cn.emay.eucp.task.multiple.dto.PortableNumberStore;
import cn.emay.eucp.task.multiple.dto.StrategyCheckDTO;
import cn.emay.eucp.task.multiple.util.BadWordFilter;
import cn.emay.eucp.task.multiple.util.FmsUtil;
import cn.emay.eucp.task.multiple.util.StringUtil;
import cn.emay.eucp.util.RegularCheckUtils;
import cn.emay.store.memory.MemoryMap;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.ConcurrentPeriodTask;
import cn.emay.util.PropertiesUtil;

public class MessageStrategyTask implements ConcurrentPeriodTask {
	private Logger logger = Logger.getLogger(MessageStrategyTask.class);
	private RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private int handleBatchNumber = 500;
	private long period = 50L;
	private static DataLazyLoading dataLazyLoading;

	static {
		int paramsCacheTime = PropertiesUtil.getIntProperty("paramsCacheTime", "eucp.service.properties", 60);
		dataLazyLoading = new DataLazyLoading(paramsCacheTime, new MemoryMap(60));
	}

	@Override
	public TaskResult exec(Map<String, String> arg0) {
		long start = System.currentTimeMillis();
		int num = 0;
		String rollbackBatchNo = null;

		try {
			while (num <= handleBatchNumber) {
				String batchNo = redis.rpop(RedisConstants.FMS_MESSAGE_QUEUE);
				if (StringUtil.isEmpty(batchNo)) {
					return TaskResult.doBusinessFailResult();
				}
				rollbackBatchNo = batchNo;
				SendFmsData fmsData = redis.hget(RedisConstants.FMS_MESSAGE_HASH, batchNo, SendFmsData.class);
				if (null == fmsData) {
					logger.error("【MessageStrategyTask】批次号为：" + batchNo + "的数据为空！");
					return TaskResult.doBusinessFailResult();
				}
				// 判断发送所用服务号是否存在
				FmsServiceCode serviceCode = CacheConstant.serviceCodeMap.get(fmsData.getAppId());
				if (null == serviceCode) {
					logger.error("【MessageStrategyTask】批次号为：" + batchNo + "的服务号为空！");
					return TaskResult.doBusinessFailResult();
				}
				// 获取发送业务类型
				FmsBusinessType businessType = CacheConstant.businessMap.get(serviceCode.getBusinessTypeId());
				if (null == businessType) {
					logger.error("【MessageStrategyTask】批次号为：" + batchNo + "的业务类型为空！");
					return TaskResult.doBusinessFailResult();
				}
				// 判断模板是否存在 从缓存里面取模板信息
				String templateId = fmsData.getTemplateId();
				// FmsTemplateRedisDto templateDto = redis.hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
				FmsTemplateRedisDto templateDto = dataLazyLoading.getTemplate(templateId);
				if (null == templateDto) {
					logger.error("【MessageStrategyTask】批次号为：" + batchNo + "的模板为空！");
					return TaskResult.doBusinessFailResult();
				}

				List<FmsMessage> messageList = new ArrayList<FmsMessage>();
				FmsIdAndMobile[] idAndMobiles = fmsData.getFmsIdAndMobiles();
				Map<String, String> mobileContentmap = new HashMap<String, String>();
				Set<String> contetnSet = new HashSet<String>();
				Map<String, String> contentParamMap = new HashMap<String, String>();
				for (FmsIdAndMobile fmsIdAndMobile : idAndMobiles) {
					FmsMessage message = FmsUtil.buildMessage(fmsData, serviceCode, fmsIdAndMobile, templateDto);
					if (message.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
						contentParamMap.put(fmsIdAndMobile.getMobile() + "|" + message.getContent(), fmsIdAndMobile.getContent());
					}
					mobileContentmap.put(fmsIdAndMobile.getMobile(), fmsIdAndMobile.getContent());
					contetnSet.add(message.getContent());
					messageList.add(message);
				}

				StrategyCheckDTO strategyDto = FmsUtil.buildStrategyDto(fmsData, serviceCode);
				strategyDto.setAllMessageList(messageList);

				// 检测手机号是否正确
				checkMobileStrategy(strategyDto);
				// 未识别、重复号码生成失败状态报告
				List<FmsReportDTO> fmsReportList = new ArrayList<FmsReportDTO>();
				for (FmsMessage fmsmessage : strategyDto.getFailMessageList()) {
					FmsReportDTO report = FmsUtil.buildReport(fmsmessage);
					fmsReportList.add(report);
				}
				// 每个变量内容不能超过20个字
				for (FmsMessage fms : strategyDto.getRightMmsMessageMap().values()) {
					if (fms.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
						String contentParam = contentParamMap.get(fms.getMobile() + "|" + fms.getContent());
						Map<String, String> map = JsonHelper.fromJson(new TypeToken<Map<String, String>>() {
						}, contentParam);
						String contentparam = "";
						for (Object object : map.keySet()) {
							contentparam = map.get(object).toString();
							if (contentparam.length() > 20) {
								FmsUtil.setErrorMsg(fms, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_CONTNET_PARAM_LENGTH.getCode());
								FmsReportDTO report = FmsUtil.buildReport(fms);
								fmsReportList.add(report);
								strategyDto.getRightMmsMessageMap().remove(fms.getMobile() + "|" + fms.getContent());
								strategyDto.getFailMessageList().add(fms);
							}
						}
					}
				}
				// 黑词检测 普通直接检测 个性拼内容参数
				Boolean isBlackDic = false;
				BadWordFilter blackDicGroupTreeFilter = CacheConstant.systemBlackDicGroupTreeFilter;
				Map<String, Boolean> blackmap = new HashMap<String, Boolean>();
				if (null != blackDicGroupTreeFilter) {
					for (String content : contetnSet) {
						isBlackDic = blackDicGroupTreeFilter.filter(content.toLowerCase());
						blackmap.put(content, isBlackDic);
					}
				}
				for (FmsMessage fms : strategyDto.getRightMmsMessageMap().values()) {
					isBlackDic = blackmap.get(fms.getContent());
					if (isBlackDic) {
						// 添加黑词打印
						Set<String> blackSet = blackDicGroupTreeFilter.filterBackDic(fms.getContent().toLowerCase());
						logger.info("batchNo为:" + fms.getBatchNumber() + "存在黑词：" + blackSet);
						FmsUtil.setErrorMsg(fms, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.SYSTEM_BLACK_DICT.getCode());
						FmsReportDTO report = FmsUtil.buildReport(fms);
						fmsReportList.add(report);
						if (fms.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile() + "|" + fms.getContent());
						} else {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
						}
						strategyDto.getFailMessageList().add(fms);
					}
				}
				// 是否携号转网
				PortableNumberStore portableNumberStore = CacheConstant.portableNumberStore;
				List<FmsMessage> noportablelist = new ArrayList<FmsMessage>();
				if (null != portableNumberStore) {
					for (FmsMessage fmsMessage : strategyDto.getRightMmsMessageMap().values()) {
						PortableNumber sectionInfo = portableNumberStore.getSectionInfo(fmsMessage.getMobile());
						if (null != sectionInfo) {
							fmsMessage.setOperatorCode(sectionInfo.getOperatorCode());
							fmsMessage.setOperatorId(sectionInfo.getId().toString());
						} else {
							noportablelist.add(fmsMessage);
						}
					}
				}
				// 判断运营商是否正确
				BaseSectionNumberStore sectionNumberStore = CacheConstant.baseSectionNumberStore;
				if (null != sectionNumberStore) {
					checkOperatorCode(noportablelist, sectionNumberStore, strategyDto, fmsReportList);
				}
				// 获取通道id
				checkChannel(strategyDto);

				// 判断模板是否有通道可用
				Map<String, String> templateRepormap = new HashMap<String, String>();
				for (FmsMessage fmsMessage : strategyDto.getRightMmsMessageMap().values()) {
					String template = fmsMessage.getTemplateId() + "," + fmsMessage.getChannelId() + "," + fmsMessage.getOperatorCode();
					// FmsTemplateReportResult fmsTemplateReportResult = redis.hget(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, template, FmsTemplateReportResult.class);
					FmsTemplateReportResult fmsTemplateReportResult = dataLazyLoading.getFmsTemplateReportResult(template);
					if (null != fmsTemplateReportResult) {
						if (fmsTemplateReportResult.getState() != FmsTemplate.REPORT_OK) {
							FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_TEMPLATE_CHANNEL_STATE.getCode());
							FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
							fmsReportList.add(report);
							if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
								strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
							} else {
								strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
							}
							strategyDto.getFailMessageList().add(fmsMessage);
						}
						templateRepormap.put(template, fmsTemplateReportResult.getChannelTemplateId());
					} else {
						FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_TEMPLATE_CHANNEL.getCode());
						FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
						fmsReportList.add(report);
						if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
							strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
						} else {
							strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
						}
						// strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
						strategyDto.getFailMessageList().add(fmsMessage);
					}
				}
				// 黑字典
				BlackListStore blackListStore = CacheConstant.blackListStore;
				if (null != blackListStore) {
					for (FmsMessage fmsMessage : strategyDto.getRightMmsMessageMap().values()) {
						boolean isblackmobile = blackListStore.isBlackMobile(fmsMessage.getMobile());
						if (isblackmobile) {
							FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.SYSTEM_BLACK_GLOBAL.getCode());
							FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
							fmsReportList.add(report);
							if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
								strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
							} else {
								strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
							}
							// strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
							strategyDto.getFailMessageList().add(fmsMessage);
						}

					}
				}
				// 扣费
				for (FmsMessage fms : strategyDto.getRightMmsMessageMap().values()) {
					Long price = 1L;
					Long balance = redis.hIncrBy(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, fmsData.getAppId(), -price);
					if (balance.longValue() >= 0) {
						if (null == fms.getChannelId() || fms.getChannelId().longValue() == 0L) {
							FmsUtil.setErrorMsg(fms, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_NO_CHANNEL.getCode());
							FmsReportDTO report = FmsUtil.buildReport(fms);
							fmsReportList.add(report);
							if (fms.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
								strategyDto.getRightMmsMessageMap().remove(fms.getMobile() + "|" + fms.getContent());
							} else {
								strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
							}
							// strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
							strategyDto.getFailMessageList().add(fms);
						}

					} else {
						redis.hIncrBy(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, fmsData.getAppId(), price);
						FmsUtil.setErrorMsg(fms, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_BALANCE.getCode());
						FmsReportDTO report = FmsUtil.buildReport(fms);
						fmsReportList.add(report);
						// strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
						if (fms.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile() + "|" + fms.getContent());
						} else {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
						}
						strategyDto.getFailMessageList().add(fms);
					}
				}
				// 判断发送字数长度
				Map<Long, FmsChannel> channelMap = CacheConstant.channelMap;
				for (FmsMessage fmsMessage : strategyDto.getRightMmsMessageMap().values()) {
					int contentlength = fmsMessage.getContent().length();
					FmsChannel fmsChannel = channelMap.get(fmsMessage.getChannelId());
					if ("CMCC".equals(fmsMessage.getOperatorCode())) {
						if (null != fmsChannel.getCmccLimit()) {
							Integer cmccLimit = fmsChannel.getCmccLimit();
							if (cmccLimit.intValue() < contentlength) {
								FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_CONTNET_LENGTH.getCode());
								FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
								fmsReportList.add(report);
								if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
								} else {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								}
								// strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								strategyDto.getFailMessageList().add(fmsMessage);
							}
						}
					} else if ("CUCC".equals(fmsMessage.getOperatorCode())) {
						if (null != fmsChannel.getCuccLimit()) {
							Integer cuccLimit = fmsChannel.getCuccLimit();
							if (cuccLimit.intValue() < contentlength) {
								FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_CONTNET_LENGTH.getCode());
								FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
								fmsReportList.add(report);
								// strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
								} else {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								}
								strategyDto.getFailMessageList().add(fmsMessage);
							}
						}
					} else if ("CTCC".equals(fmsMessage.getOperatorCode())) {
						if (null != fmsChannel.getCtccLimit()) {
							Integer ctccLimit = fmsChannel.getCtccLimit();
							if (ctccLimit.intValue() < contentlength) {
								FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROPR_CONTNET_LENGTH.getCode());
								FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
								fmsReportList.add(report);
								if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
								} else {
									strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								}
								// strategyDto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
								strategyDto.getFailMessageList().add(fmsMessage);
							}
						}
					}
				}
				// 判断运营商是否分配成功
				for (FmsMessage fms : strategyDto.getRightMmsMessageMap().values()) {
					FmsChannel fmsChannel = CacheConstant.channelMap.get(fms.getChannelId());
					String providers = fmsChannel.getProviders();
					String operatorCode = "";
					if ("CMCC".equals(fms.getOperatorCode())) {
						operatorCode = "1";
					} else if ("CUCC".equals(fms.getOperatorCode())) {
						operatorCode = "2";
					} else if ("CTCC".equals(fms.getOperatorCode())) {
						operatorCode = "3";
					}
					if (!providers.contains(operatorCode)) {
						FmsUtil.setErrorMsg(fms, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_OPERATOR_ALLOCATION.getCode());
						FmsReportDTO report = FmsUtil.buildReport(fms);
						fmsReportList.add(report);
						// strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
						if (fms.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile() + "|" + fms.getContent());
						} else {
							strategyDto.getRightMmsMessageMap().remove(fms.getMobile());
						}
						strategyDto.getFailMessageList().add(fms);
					}
				}
				// 通道是否需要报备
				Map<String, FmsMessage> rightMmsMessageMap = strategyDto.getRightMmsMessageMap();
				pushChannelLevelQueue(rightMmsMessageMap, serviceCode, fmsData.getBatchNo(), fmsData.getSerialNumber(), templateDto, mobileContentmap, templateRepormap);
				// 是否需要状态报告，需要则入状态报告队列

				FmsServiceCodeParam fmsServiceCodeParam = CacheConstant.serviceCodeParamMap.get(serviceCode.getAppId());
				if (null != fmsServiceCodeParam) {
					if (fmsServiceCodeParam.getGetReportType() != null && fmsServiceCodeParam.getGetReportType().intValue() == 1 && fmsReportList.size() > 0) {
						redis.lpush(RedisConstants.FMS_REPORT_QUEUE + serviceCode.getAppId(), -1, fmsReportList.toArray());
					}
				}
				redis.hset(RedisConstants.FMS_MESSAGE_HASH, batchNo, strategyDto.getAllMessageList().toArray(), -1);
				redis.lpush(RedisConstants.FMS_SAVE_DB_QUEUE, batchNo, -1);
				num += strategyDto.getAllMessageList().size();
				logger.info("【MessageStrategyTask】批次号为：" + batchNo + "，包含" + messageList.size() + "条短信，执行策略任务总辑耗时：" + (System.currentTimeMillis() - start) + "ms");
				strategyDto = null;
				fmsReportList.clear();
				rollbackBatchNo = null;
				mobileContentmap.clear();
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【MessageStrategyTask】出现异常", e);
			// 异常回滚
			if (!StringUtils.isEmpty(rollbackBatchNo)) {
				redis.lpush(RedisConstants.FMS_MESSAGE_QUEUE, rollbackBatchNo, -1);
				rollbackBatchNo = null;
			}
			return TaskResult.doBusinessFailResult();
		} finally {
			if (redis.llen(RedisConstants.FMS_MESSAGE_QUEUE) > 0L) {
				period = 10L;
			} else {
				period = 50L;
			}
		}
	}

	@Override
	public int needConcurrent(int arg0, int arg1, int arg2) {
		return redis.llen(RedisConstants.FMS_MESSAGE_QUEUE).intValue() / handleBatchNumber;
	}

	@Override
	public long period() {
		return period;
	}

	private void checkMobileStrategy(StrategyCheckDTO dto) {
		List<FmsMessage> list = dto.getAllMessageList();
		List<FmsMessage> faillist = new ArrayList<FmsMessage>();
		Map<String, FmsMessage> rightFmsMessageMap = new ConcurrentHashMap<String, FmsMessage>();
		for (FmsMessage fmsMessage : list) {
			String mobile = fmsMessage.getMobile();
			String repeatMobile = "";
			if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
				repeatMobile = fmsMessage.getMobile() + "|" + fmsMessage.getContent();
			} else {
				repeatMobile = fmsMessage.getMobile();
			}
			if (mobile == null || mobile.length() < CommonConstanct.FMS_MOBILE_MIN_LENGTH) {
				FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_MOBILE.getCode());
				faillist.add(fmsMessage);
				continue;
			}
			if (RegularCheckUtils.checkMobile(mobile)) {
				if (rightFmsMessageMap.containsKey(repeatMobile)) { // 校验重复号码
					FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_REPEAT_MOBILE.getCode());
					faillist.add(fmsMessage);
				} else {
					rightFmsMessageMap.put(repeatMobile, fmsMessage);
				}
			} else {
				FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_MOBILE.getCode());
				faillist.add(fmsMessage);
			}

		}
		dto.setFailMessageList(faillist);
		dto.setRightMmsMessageMap(rightFmsMessageMap);
	}

	private void checkOperatorCode(List<FmsMessage> noportablelist, BaseSectionNumberStore sectionNumberStore, StrategyCheckDTO dto, List<FmsReportDTO> fmsReportList) {
		Map<String, FmsMessage> rightfmsMessageMap = dto.getRightMmsMessageMap();
		for (FmsMessage fmsMessage : rightfmsMessageMap.values()) {
			BaseSectionNumber sectionInfo = sectionNumberStore.getSectionInfo(fmsMessage.getMobile());
			if (null != sectionInfo) {
				fmsMessage.setOperatorCode(sectionInfo.getOperatorCode());
				fmsMessage.setOperatorId(sectionInfo.getId().toString());
			} else {
				FmsUtil.setErrorMsg(fmsMessage, EucpFmsMessageState.FAIL.getCode(), EucpFmsMessageState.ERROR_SECTION_NUMBER.getCode());
				FmsReportDTO report = FmsUtil.buildReport(fmsMessage);
				fmsReportList.add(report);
				if (fmsMessage.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
					dto.getRightMmsMessageMap().remove(fmsMessage.getMobile() + "|" + fmsMessage.getContent());
				} else {
					dto.getRightMmsMessageMap().remove(fmsMessage.getMobile());
				}
				dto.getFailMessageList().add(fmsMessage);
			}
		}
	}

	private void checkChannel(StrategyCheckDTO dto) {
		Map<String, FmsMessage> rightfmsMessageMap = dto.getRightMmsMessageMap();
		for (FmsMessage fms : rightfmsMessageMap.values()) {
			String channelId = CacheConstant.serviceCodeChannelMap.get(fms.getAppId() + "," + fms.getTemplateType() + "," + fms.getOperatorCode());
			if (null != channelId) {
				fms.setChannelId(Long.valueOf(channelId));
			} else {
				fms.setChannelId(0L);
			}
		}
	}

	private void pushChannelLevelQueue(Map<String, FmsMessage> rightMmsMessageMap, FmsServiceCode serviceCode, String batchNo, String serialNumber, FmsTemplateRedisDto templateDto,
			Map<String, String> mobileContentmap, Map<String, String> templateRepormap) {
		Map<Long, List<FmsSendDto>> sendMap = new HashMap<Long, List<FmsSendDto>>();
		for (FmsMessage fms : rightMmsMessageMap.values()) {
			FmsChannel fmsChannel = CacheConstant.channelMap.get(fms.getChannelId());
			FmsServiceCodeParam fmsServiceCodeParam = CacheConstant.serviceCodeParamMap.get(fms.getAppId());
			String template = fms.getTemplateId() + "," + fms.getChannelId() + "," + fms.getOperatorCode();
			String channelTemplateId = templateRepormap.get(template);
			FmsSendDto sendDto = FmsUtil.buildFmsSendDto(fms, fmsServiceCodeParam, batchNo, null, serialNumber, fmsChannel.getIsNeedReport(), templateDto, mobileContentmap, channelTemplateId);
			if (sendMap.containsKey(fms.getChannelId())) {
				sendMap.get(fms.getChannelId()).add(sendDto);
			} else {
				List<FmsSendDto> list = new ArrayList<FmsSendDto>();
				list.add(sendDto);
				sendMap.put(fms.getChannelId(), list);
			}
		}
		// 入通道优先级队列
		for (Entry<Long, List<FmsSendDto>> send : sendMap.entrySet()) {
			redis.lpush(RedisConstants.SEND_FMS_CHANNEL_QUEUE + send.getKey(), -1, send.getValue().toArray());
		}
	}
}
