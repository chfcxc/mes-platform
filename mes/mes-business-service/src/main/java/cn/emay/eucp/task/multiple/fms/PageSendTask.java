package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.mt.SendFmsData;
import cn.emay.eucp.common.dto.fms.page.PageSendDto;
import cn.emay.eucp.common.dto.fms.page.PageSendParsingDto;
import cn.emay.eucp.common.support.OnlyIdGenerator;
import cn.emay.eucp.task.multiple.constant.CommonConstanct;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.ConcurrentPeriodTask;

/**
 * 
 * @项目名称：eucp-fms-business-service  @类描述：   页面发送任务 @创建人：Adorkable  
 * @创建时间：2019年5月8日 下午4:39:00   @修改人：Adorkable  
 * @修改时间：2019年5月8日 下午4:39:00   @修改备注：
 */
public class PageSendTask implements ConcurrentPeriodTask {

	private static Logger logger = Logger.getLogger(PageSendTask.class);
	// private FmsBatchService fmsBatchService = BeanFactoryUtils.getBean(FmsBatchService.class);
	private static RedisClient redis = (RedisClient) BeanFactoryUtils.getBean("redis");

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		String serialNumber = redis.rpop(RedisConstants.FMS_PAGESEND_SERIALNUMBER_QUEUE);
		if (serialNumber == null) {
			return TaskResult.notDoBusinessResult();
		}

		PageSendDto sendDto = redis.hget(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber, PageSendDto.class);
		if (sendDto == null) {
			redis.zrem(RedisConstants.FMS_SCHEDULING_QUEUE, serialNumber);
			return TaskResult.notDoBusinessResult();
		}
		List<PageSendParsingDto> list = new ArrayList<PageSendParsingDto>();
		try {
			for (;;) {
				PageSendParsingDto dto = redis.rpop(RedisConstants.FMS_PAGE_SEND_WAITING_SEND_QUEUE + serialNumber, PageSendParsingDto.class);
				if (null == dto) {
					redis.hdel(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber);
					redis.zrem(RedisConstants.FMS_SCHEDULING_QUEUE, serialNumber);
					/*
					 * FmsBatch entity = fmsBatchService.findBySerialNumber(sendDto.getBatchNumber()); entity.setState(FmsBatch.SEND_FINISH); fmsBatchService.update(entity);
					 */
					break;
				}
				list.add(dto);
				if (list.size() == CommonConstanct.pageSendBatchNumber) {
					saveFms(serialNumber, sendDto, list);
					list.clear();
				}
			}
			if (list.size() > 0) {
				saveFms(serialNumber, sendDto, list);
				list.clear();
			}
		} catch (Exception e) {
			if (list.size() > 0) {// 回滚
				redis.lpush(RedisConstants.FMS_PAGE_SEND_WAITING_SEND_QUEUE + serialNumber, 0, list.toArray());
				redis.hset(RedisConstants.FMS_PAGE_SEND_HASH, serialNumber, sendDto, 0);
				redis.zadd(RedisConstants.FMS_SCHEDULING_QUEUE, System.currentTimeMillis(), serialNumber);
			}
			logger.error("页面发送失败，流水号：" + serialNumber, e);
		}

		return TaskResult.doBusinessSuccessResult();
	}

	/**
	 * 
	 * @Title: saveNormalFms @Description: 入redis @param @param serialNumber @param @param sendDto @param @param list @return void @throws
	 */
	private void saveFms(String serialNumber, PageSendDto sendDto, List<PageSendParsingDto> list) {
		SendFmsData smsdata = new SendFmsData();
		smsdata.setTitle(sendDto.getTitle());
		smsdata.setAppId(sendDto.getAppId());
		smsdata.setTemplateId(sendDto.getTemplateId());
		smsdata.setBatchMobileNumber(1);
		smsdata.setBatchNo(OnlyIdGenerator.genOblyBId(CommonConstanct.INTERFACE_CODE));
		smsdata.setInterfaceServiceNo(CommonConstanct.INTERFACE_CODE);
		smsdata.setSendType(SendFmsData.SEND_TYPE_PAGE);
		smsdata.setSubmitTime(sendDto.getSubmitTime());
		smsdata.setSerialNumber(serialNumber);
		smsdata.setTemplateType(sendDto.getTemplateType());
		List<FmsIdAndMobile> fiamList = new ArrayList<FmsIdAndMobile>();
		for (PageSendParsingDto pspdto : list) {
			String fmsId = OnlyIdGenerator.genOnlyId(CommonConstanct.INTERFACE_CODE);
			FmsIdAndMobile dto = new FmsIdAndMobile(fmsId, null, pspdto.getMobile(), JsonHelper.toJsonString(pspdto.getData()));
			fiamList.add(dto);
		}
		smsdata.setFmsIdAndMobiles(fiamList.toArray(new FmsIdAndMobile[fiamList.size()]));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(smsdata.getBatchNo(), smsdata);
		redis.hmset(RedisConstants.FMS_MESSAGE_HASH, map, -1);
		redis.lpush(RedisConstants.FMS_MESSAGE_QUEUE, -1, smsdata.getBatchNo());
	}

	@Override
	public long period() {
		int count = redis.llen(RedisConstants.FMS_PAGESEND_SERIALNUMBER_QUEUE).intValue();
		if (count > 0) {
			return 50;
		}
		return 500;
	}

	@Override
	public int needConcurrent(int alive, int min, int max) {
		int count = redis.llen(RedisConstants.FMS_PAGESEND_SERIALNUMBER_QUEUE).intValue();
		int threadCount = count;
		if (threadCount == 0) {
			threadCount = 1;
		}
		return threadCount;
	}
}
