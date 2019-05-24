package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsBatchPageDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.fms.FmsBatchDao;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsBatchService;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午3:53:51 类说明 */
@Service("fmsBatchService")
public class FmsBatchServiceImpl implements FmsBatchService {

	@Resource
	FmsBatchDao fmsBatchDao;
	@Resource
	FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsUserServiceCodeAssignDao fmsUserServiceCodeAssignDao;

	@Override
	public Long saveBatch(FmsBatch fmsBatch) {
		fmsBatchDao.save(fmsBatch);
		return fmsBatch.getId();
	}

	@Override
	public Page<FmsBatchPageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, Long businessTypeId, int saveType, Long contentTypeId, int start,
			int limit, Date startTime, Date endTime, List<Long> userIds) {
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
					return new Page<FmsBatchPageDto>();
				}
			}
		}
		return fmsBatchDao.findPage(batchNumber, title, serviceCodeId, state, sendType, infoType, contentIds, start, limit, startTime, endTime, set);
	}

	@Override
	public FmsBatch findBySerialNumber(String serialNumber) {
		return fmsBatchDao.findBySerialNumber(serialNumber);
	}

	@Override
	public void update(FmsBatch entity) {
		fmsBatchDao.update(entity);
	}

}
