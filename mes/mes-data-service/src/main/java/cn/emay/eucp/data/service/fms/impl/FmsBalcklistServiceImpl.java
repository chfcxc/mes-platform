package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.base.FmsBlacklistDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.data.dao.fms.FmsBlacklistDao;
import cn.emay.eucp.data.service.fms.FmsBlacklistService;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:33:36 类说明 */
@Service("fmsBlacklistService")
public class FmsBalcklistServiceImpl implements FmsBlacklistService {

	@Resource
	FmsBlacklistDao fmsBlacklistDao;

	@Override
	public Page<FmsBlacklistDto> findPage(String mobile, int start, int limit) {
		return fmsBlacklistDao.findPage(mobile, start, limit);
	}

	@Override
	public void deletebyMobiles(List<String> mobiles) {
		fmsBlacklistDao.deletebyMobiles(mobiles);
	}

	@Override
	public void deletebyMobile(String mobile) {
		fmsBlacklistDao.deletebyMobile(mobile);
	}

	@Override
	public Result save(FmsBlacklist fmsBlacklist) {
		fmsBlacklistDao.save(fmsBlacklist);
		return Result.rightResult();
	}

	@Override
	public FmsBlacklist findbymobile(String mobile, Long id) {
		return fmsBlacklistDao.findbymobile(mobile, id);
	}

	@Override
	public List<FmsBlacklist> findFmsBlacklistByLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime) {
		return fmsBlacklistDao.findFmsBlacklistByLastUpdateTime(currentPage, pageSize, lastUpdateTime);
	}

}
