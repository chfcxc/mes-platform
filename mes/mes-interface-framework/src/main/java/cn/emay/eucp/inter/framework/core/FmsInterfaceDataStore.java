package cn.emay.eucp.inter.framework.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.mt.SendFmsData;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.FmsReportReceiveRecordDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReportCode;
import cn.emay.store.memory.MemoryMap;

/**
 * 接口数据存储服务
 * 
 * @author Frank
 */
public class FmsInterfaceDataStore {

	private MemoryMap vc;

	private int paramsCacheTime = 30;

	public FmsInterfaceDataStore(int paramsCacheTime, MemoryMap vc) {
		this.paramsCacheTime = paramsCacheTime;
		this.vc = vc;
		if (this.paramsCacheTime < 10) {
			this.paramsCacheTime = 10;
		}
	}

	/**
	 * 根据APPID获取服务号
	 * 
	 * @param appId
	 * @return
	 */
	public FmsServiceCodeDto getServiceCodeByAppId(String appId) {
		String serviceCodeKey = appId;
		String serviceCodeParamKey = appId + "param";
		FmsServiceCodeDto sc = vc.get(serviceCodeKey, FmsServiceCodeDto.class);
		if (sc == null) {
			sc = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_SERVICE_CODE_HASH, appId, FmsServiceCodeDto.class);
			if (sc != null) {
				vc.set(serviceCodeKey, sc, paramsCacheTime);
			} else {
				return null;
			}
		}
		FmsServiceCodeParam scp = vc.get(serviceCodeParamKey, FmsServiceCodeParam.class);
		if (scp == null) {
			scp = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_SERVICE_CODE_PARAM_HASH, appId, FmsServiceCodeParam.class);
			if (scp != null) {
				vc.set(serviceCodeParamKey, scp, paramsCacheTime);
				sc.setIsNeedReport(scp.getGetReportType());
				sc.setIpConfiguration(scp.getIpConfiguration());
			}
		} else {
			sc.setIsNeedReport(scp.getGetReportType());
			sc.setIpConfiguration(scp.getIpConfiguration());
		}
		return sc;
	}

	/** 保存接收到的短信 */
	public void saveSmsDataByRedis(SendFmsData... datas) {
		if (datas == null || datas.length == 0) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Object[] batchNos = new Object[datas.length];
		int i = 0;
		for (SendFmsData data : datas) {
			map.put(data.getBatchNo(), data);
			batchNos[i] = data.getBatchNo();
			i++;
		}
		FmsInterfaceFrameWork.getRedis().hmset(RedisConstants.FMS_MESSAGE_HASH, map, -1);
		FmsInterfaceFrameWork.getRedis().lpush(RedisConstants.FMS_MESSAGE_QUEUE, -1, batchNos);
	}

	/**
	 * 从队列中拿Report
	 * 
	 * @param appId
	 * @return
	 */
	public FmsReportDTO getReport(String appId) {
		FmsReportDTO dto = FmsInterfaceFrameWork.getRedis().rpop(RedisConstants.FMS_REPORT_QUEUE + appId, FmsReportDTO.class);
		if (null != dto) {
			EucpInterFmsReportCode code = EucpInterFmsReportCode.findByEucpSmsMessageState(dto.getState());
			if (code != null) {
				dto.setState(code.getCode());
				dto.setDesc(code.getName());
			} else {
				dto.setState(EucpInterFmsReportCode.FAIL_UNKNOW.getCode());
				dto.setDesc(EucpInterFmsReportCode.FAIL_UNKNOW.getName());
			}
		}
		return dto;
	}

	/**
	 * @Title: rollBackReport
	 * @Description: 状态报告回滚
	 * @param @param appId
	 * @param @return
	 */
	public void rollBackReport(List<FmsReportDTO> reports) {
		if (null != reports && reports.size() > 0) {
			String appId = reports.get(0).getAppId();
			FmsInterfaceFrameWork.getRedis().rpush(RedisConstants.FMS_REPORT_QUEUE + appId, -1, reports.toArray());
		}
	}

	/**
	 * 状态报告获取记录入队列
	 * 
	 * @param dto
	 */
	public void saveReportRecord(FmsReportReceiveRecordDTO dto) {
		if (dto == null) {
			return;
		}
		FmsInterfaceFrameWork.getRedis().lpush(RedisConstants.FMS_REPORT_RECEIVE_RECORD_QUEUE, -1, dto);
	}

	/**
	 * 根据APPID获取服务号余额
	 * 
	 * @param appId
	 * @return
	 */
	public String getServiceCodeByAppIdBalance(String appId) {
		String key = appId + "_BALANCE";
		Long balace = vc.get(key, Long.class);
		if (balace == null) {
			balace = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, appId, Long.class);
			if (balace != null) {
				vc.set(key, balace, paramsCacheTime);
			} else {
				balace = 0L;
			}
		}
		return String.valueOf(balace);
	}

	/**
	 * 判断 这个服务号是否有这个模板id
	 * 
	 * @param appId
	 * @param templateId
	 * @return
	 */
	public Boolean isExsit(String appId, String templateId) {
		String field = appId + "_" + templateId;
		String key = "EXIST_" + field;
		Integer flag = vc.get(key, Integer.class);
		if (flag == null || flag != 1) {
			flag = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_APPID_TEMPLATEID_HASH, field, Integer.class);
			if (flag != null && flag == 1) {
				vc.set(key, flag, paramsCacheTime);
			} else {
				return false;
			}
		}
		return true;
	}

	public FmsTemplateRedisDto getTemplate(String templateId) {
		String key = "TEMPLATE_HASH_" + templateId;
		FmsTemplateRedisDto template = vc.get(key, FmsTemplateRedisDto.class);
		if (template == null) {
			template = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
			if (template != null) {
				vc.set(key, template, paramsCacheTime);
			} else {
				return null;
			}
		}
		return template;
	}

	public Map<String, Long> getChannelId(String appId, int templateType) {
		Map<String, Long> map = new HashMap<String, Long>();
		for(String code:GlobalConstant.OPERATOR_CODES) {
			String key = appId + "," + templateType + "," + code;
			Long channelID = vc.get(key, Long.class);
			if (channelID == null) {
				channelID = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_APPID_CHANNELID_HASH, key, Long.class);
				vc.set(key, channelID, paramsCacheTime);
			}
			map.put(code, channelID);
		}
		return map;
	}

	public FmsTemplateReportResult getTemplateStatus(String templateId, Long channelId, String operatorCode) {
		String field = templateId + "," + channelId + "," + operatorCode;
		String key = "TEMPLATE_STATE_" + field;
		FmsTemplateReportResult result = vc.get(key, FmsTemplateReportResult.class);
		if (result == null) {
			result = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field, FmsTemplateReportResult.class);
			if (result != null) {
				vc.set(key, result, paramsCacheTime);
			}
		}
		return result;
	}
}
