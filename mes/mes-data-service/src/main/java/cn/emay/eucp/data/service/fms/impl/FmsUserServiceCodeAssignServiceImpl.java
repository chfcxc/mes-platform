package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeDao;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;

@Service("fmsUserServiceCodeAssignService")
public class FmsUserServiceCodeAssignServiceImpl implements FmsUserServiceCodeAssignService {
	@Resource
	private FmsUserServiceCodeAssignDao fmsUserServiceCodeAssignDao;
	@Resource
	private FmsServiceCodeDao fmsServiceCodeDao;

	@Override
	public List<FmsServiceCode> findByAssignUserId(Long userId) {
		return fmsUserServiceCodeAssignDao.findByAssignUserId(userId);
	}

	@Override
	public Result addFmsUserSCAssign(Long userId, Long enterpriseId, String serviceCodeIds) {
		if (StringUtils.isEmpty(serviceCodeIds)) {
			return Result.badResult("闪推服务号不能为空");
		}
		String[] scArr = serviceCodeIds.split(",");
		Set<Long> scIdSet = new HashSet<Long>();
		for (String sc : scArr) {
			scIdSet.add(Long.valueOf(sc));
		}
		if (scIdSet.size() == 0) {
			return Result.badResult("闪推服务号不能为空");
		}
		List<FmsServiceCode> dtoList = fmsServiceCodeDao.findByIds(enterpriseId, new ArrayList<Long>(scIdSet));
		if (null == dtoList || dtoList.size() != scIdSet.size()) {
			return Result.badResult("关联闪推服务号不正确");
		}
		fmsUserServiceCodeAssignDao.deleteUserSCAssignByProperty("userId", userId);
		fmsUserServiceCodeAssignDao.saveBatchUserSCAssign(new ArrayList<Long>(scIdSet), userId);
		return Result.rightResult();
	}

	@Override
	public Map<String, Object> findBinding(Long userId, Long enterpriseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<FmsServiceCode> allEffectiveList = new ArrayList<FmsServiceCode>();// 所有有效服务号
		List<FmsServiceCode> bindingList = new ArrayList<FmsServiceCode>();// 已绑定服务号
		List<FmsServiceCode> notBoundList = new ArrayList<FmsServiceCode>();// 未绑定服务号
		List<FmsServiceCode> allList = fmsServiceCodeDao.findByEnterId(enterpriseId, -1);
		List<FmsUserServiceCodeAssign> userAssignList = fmsUserServiceCodeAssignDao.findUserSCAssignByProperty("userId", userId);
		for (FmsServiceCode temp : allList) {
			boolean isBinding = false;
			for (FmsUserServiceCodeAssign userAssign : userAssignList) {
				if (temp.getId().longValue() == userAssign.getServiceCodeId().longValue()) {
					bindingList.add(temp);
					allEffectiveList.add(temp);
					isBinding = true;
					break;
				}
			}
			if (!isBinding && temp.getState() == FmsServiceCode.STATE_ENABLE) {
				allEffectiveList.add(temp);
				notBoundList.add(temp);
			}
		}
		map.put("notBoundList", notBoundList);
		map.put("bindingList", bindingList);
		map.put("allEffectiveList", allEffectiveList);
		return map;
	}

	@Override
	public Result modifyfmsUserSCAssign(Long userId, Long enterpriseId, String fmsServiceCodeIds) {
		if (StringUtils.isEmpty(fmsServiceCodeIds)) {
			// 解除用户与服务号的所有关联关系
			fmsUserServiceCodeAssignDao.deleteUserSCAssignByProperty("userId", userId);
			return Result.rightResult();
		}

		String[] scArr = fmsServiceCodeIds.split(",");
		Set<Long> scIdSet = new HashSet<Long>();
		for (String sc : scArr) {
			scIdSet.add(Long.valueOf(sc));
		}
		List<FmsServiceCode> dtoList = fmsServiceCodeDao.findByIds(enterpriseId, new ArrayList<Long>(scIdSet));
		if (null == dtoList || dtoList.size() != scIdSet.size()) {
			return Result.badResult("关联多媒体短信服务号不正确");
		}
		fmsUserServiceCodeAssignDao.deleteUserSCAssignByProperty("userId", userId);
		fmsUserServiceCodeAssignDao.saveBatchUserSCAssign(new ArrayList<Long>(scIdSet), userId);
		return Result.rightResult();
	}

	@Override
	public Result deleteImsBind(Long userId, Long serviceCodeId) {
		FmsUserServiceCodeAssign entity = fmsUserServiceCodeAssignDao.getMmsUserSCAssign(userId, serviceCodeId);
		if (entity == null) {
			return Result.badResult("用户没有关联该服务号，无法进行解除关联操作");
		}
		fmsUserServiceCodeAssignDao.delete(entity);
		return Result.rightResult();
	}

	@Override
	public List<FmsServiceCode> findByAssignUserId(Long userId, int start, int end) {
		return fmsUserServiceCodeAssignDao.findByAssignUserId(userId, start, end);
	}

}
