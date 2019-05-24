package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.data.dao.system.PortableNumberDao;
import cn.emay.eucp.data.service.system.PortableNumberService;

@Service("portableNumberService")
public class PortableNumberServiceImpl implements PortableNumberService {
	@Resource
	private PortableNumberDao portableNumberDao;

	@Override
	public List<PortableNumber> findLimit(int currentPage, int pageSize) {
		return portableNumberDao.findLimit(currentPage, pageSize);
	}

	@Override
	public List<PortableNumber> findByLastUpdateTime(Date date) {
		return portableNumberDao.findByLastUpdateTime(date);
	}

	@Override
	public List<PortableNumber> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return portableNumberDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public Page<PortableNumber> findall(int start, int limit, String mobile) {
		Page<PortableNumber> page = portableNumberDao.findall(start, limit, mobile);
		return page;
	}

	@Override
	public void save(PortableNumber entity) {
		portableNumberDao.save(entity);
	}

	@Override
	public void addBatch(List<PortableNumber> entities) {
		portableNumberDao.saveBatchBySql(entities);
	}

	@Override
	public void updateBatch(List<PortableNumber> entities) {
		portableNumberDao.updateBatchBySql(entities);
	}

	@Override
	public void deleteBatch(List<String> mobiles) {
		portableNumberDao.deleteBatchBySql(mobiles);
	}

	@Override
	public Set<String> findByMobiles(List<String> mobiles) {
		return portableNumberDao.findByMobiles(mobiles);
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		return portableNumberDao.deleteByLastUpdateTime(date);
	}

	@Override
	public List<PortableNumber> findPageById(long startId, long endId) {
		return portableNumberDao.findPageById(startId, endId);
	}
	
	@Override
	public Map<String, PortableNumber> findPortableNumberByMobiles(List<String> mobiles) {
		return portableNumberDao.findPortableNumberByMobiles(mobiles);
	}
}
