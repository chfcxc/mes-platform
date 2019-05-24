package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.data.dao.system.BaseSectionNumberDao;
import cn.emay.eucp.data.service.system.BaseSectionNumberService;

@Service("baseSectionNumberService")
public class BaseSectionNumberServiceImpl implements BaseSectionNumberService {

	@Resource
	private BaseSectionNumberDao baseSectionNumberDao;

	@Override
	public Page<BaseSectionNumber> findBaseSectionNumber(String number, String operatorCode, int start, int limit) {
		return baseSectionNumberDao.findBaseSectionNumber(number, operatorCode, start, limit);
	}

	@Override
	public Result addBaseSectionNumber(BaseSectionNumber baseSectionNumber) {
		baseSectionNumberDao.save(baseSectionNumber);
		return Result.rightResult();
	}

	@Override
	public BaseSectionNumber findById(Long id) {
		return baseSectionNumberDao.findById(id);
	}

	@Override
	public Result updateBaseSectionNumber(BaseSectionNumber baseSectionNumber) {
		baseSectionNumberDao.update(baseSectionNumber);
		return Result.rightResult();

	}

	@Override
	public Result deleteBaseSectionNumber(Long id) {
		baseSectionNumberDao.deleteById(id);
		return Result.rightResult();
	}

	@Override
	public boolean findByNumber(String number, Long id) {
		return baseSectionNumberDao.findByNumber(number, id);
	}

	@Override
	public List<BaseSectionNumber> findAll() {
		return baseSectionNumberDao.findAll();
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		return baseSectionNumberDao.deleteByLastUpdateTime(date);
	}

	@Override
	public void addBatch(List<BaseSectionNumber> entities) {
		baseSectionNumberDao.saveBatchBySql(entities);
	}

	@Override
	public List<BaseSectionNumber> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize) {
		return baseSectionNumberDao.findByLastUpdateTime(lastUpdateTime, currentPage, pageSize);
	}
}
