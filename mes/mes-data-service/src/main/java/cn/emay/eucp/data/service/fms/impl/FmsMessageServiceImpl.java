package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsMessagePageDto;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsMessageDao;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsMessageService;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午3:56:43 类说明 */
@Service("fmsMessageService")
public class FmsMessageServiceImpl implements FmsMessageService {
	@Resource
	private FmsMessageDao fmsMessageDao;
	@Resource
	FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsUserServiceCodeAssignDao fmsUserServiceCodeAssignDao;

	@Override
	public void saveBatchByBeans(List<FmsMessage> beans) {
		fmsMessageDao.saveBatchByBeans(beans);
	}

	@Override
	public void updateFmsResponse(List<UpdateFmsDTO> updateFmsDTOs) {
		fmsMessageDao.updateFmsResponse(updateFmsDTOs);
	}

	@Override
	public void updateFmsReport(List<UpdateFmsDTO> updateFmsDTOs) {
		fmsMessageDao.updateFmsReport(updateFmsDTOs);
	}

	@Override
	public Page<FmsMessagePageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, Long businessTypeId, int saveType, Long contentTypeId,
			int start, int limit, Date startTime, Date endTime, List<Long> userIds, int operator) {
		List<Long> contentIds = fmsBusinessTypeDao.findIds(businessTypeId, saveType, contentTypeId);
		Set<Long> set = new HashSet<Long>();
		if (userIds != null && userIds.size() > 0) {
			Long userId = userIds.get(0);
			if (userId != null) {
				List<FmsUserServiceCodeAssign> lists = fmsUserServiceCodeAssignDao.findUserSCAssignByProperty("userId", userId);
				for (FmsUserServiceCodeAssign serviceCodeUser : lists) {
					set.add(serviceCodeUser.getServiceCodeId());
				}
				if (set.isEmpty()) {
					return new Page<FmsMessagePageDto>();
				}
			}
		}
		return fmsMessageDao.findPage(batchNumber, title, serviceCodeId, state, sendType, infoType, contentIds, start, limit, startTime, endTime, set, operator);
	}

	@Override
	public List<FmsServicecodeConsumptionDay> findConsumptionReport(String day, Date d, int currentPage, int pageSize) {
		List<FmsServicecodeConsumptionDay> report = fmsMessageDao.findConsumptionReport(day, currentPage, pageSize);
		return report;
	}

	@Override
	public List<FmsEnterpriseContentTypeDay> findContentTypeReport(String day, Date d, int currentPage, int pageSize) {
		List<FmsEnterpriseContentTypeDay> list = fmsMessageDao.findContentTypeReport(day, currentPage, pageSize);
		for (FmsEnterpriseContentTypeDay fmsEnterpriseContentTypeDay : list) {
			fmsEnterpriseContentTypeDay.setReportTime(d);
		}
		return list;
	}

}
