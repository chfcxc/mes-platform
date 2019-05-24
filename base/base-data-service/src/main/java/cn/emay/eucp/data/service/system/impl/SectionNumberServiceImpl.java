package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.Operator;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;
import cn.emay.eucp.data.dao.system.SectionNumberDao;
import cn.emay.eucp.data.service.system.SectionNumberService;

/**
 * cn.emay.eucp.common.moudle.db.system.SectionNumber Service implement
 * 
 * @author frank
 */
@Service("sectionNumberService")
public class SectionNumberServiceImpl implements SectionNumberService {

	@Resource
	private SectionNumberDao sectionNumberDao;

	@Resource
	private RedisClient redis;

	@Override
	public Page<SectionNumber> findByNumberAndoperatorCode(String number, String operatorCode, String provinceCode, int start, int limit) {
		return sectionNumberDao.findByNumberAndoperatorCode(number, operatorCode, provinceCode, start, limit);
	}

	@Override
	public Result addSectionNumber(SectionNumber sectionNumber) {
		sectionNumberDao.save(sectionNumber);
		return Result.rightResult();
	}

	@Override
	public Result updateSectionNumber(SectionNumber sectionNumber) {
		sectionNumberDao.update(sectionNumber);
		return Result.rightResult();
	}

	@Override
	public SectionNumber findById(Long id) {
		return sectionNumberDao.findById(id);
	}

	@Override
	public Result deleteSectionNumber(Long id) {
		SectionNumber sectionNumber = sectionNumberDao.findById(id);
		if (null != sectionNumber) {
			sectionNumberDao.deleteFindEntity(sectionNumber);
		}
		sectionNumberDao.deleteSectionNumber(id);
		return Result.rightResult();
	}

	@Override
	public boolean findNumber(String number, Long id) {
		return sectionNumberDao.findNumber(number, id);
	}

	@Override
	public List<SectionNumber> findAll() {
		return sectionNumberDao.findAll();
	}

	@Override
	public List<SectionNumber> findByLastUpdateTime(Date date) {
		return sectionNumberDao.findByLastUpdateTime(date);
	}

	@Override
	public List<SectionNumber> findLimit(int start, int limit) {
		return sectionNumberDao.findLimit(start, limit);
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		return sectionNumberDao.deleteByLastUpdateTime(date);
	}

	@Override
	public void saveBatchBySql(List<SectionNumber> list) {
		sectionNumberDao.saveBatchBySql(list);
	}

	@Override
	public Result verify(String number, String provinceCode, String provinceName, String city, String operatorCode) {
		String regex = "^1\\d{6}$";
		if (StringUtils.isEmpty(number)) {
			return Result.badResult("号段不能为空");
		}
		if (!number.matches(regex)) {
			return Result.badResult("请输入以1开头的7位数字");
		}
		if (StringUtils.isEmpty(provinceCode)) {
			return Result.badResult("请选择省份");
		}
		if (provinceName == null) {
			return Result.badResult("输入省份不存在");
		}
		if (StringUtils.isEmpty(operatorCode)) {
			return Result.badResult("运营商不能为空");
		}
		if (Operator.findNameByCode(operatorCode) == null) {
			return Result.badResult("运营商选择错误");
		}
		if (city != null && city.length() > 10) {
			return Result.badResult("城市名字不能超过10字");
		}
		return Result.rightResult();
	}

	@Override
	public List<SectionNumber> findSectionNumbersById(int startId, int endId) {
		return sectionNumberDao.findSectionNumbersById(startId, endId);
	}

	@Override
	public void redisSaveDownloadError(String downloadKey, List<String[]> errors) {
		redis.set(downloadKey, errors, 60 * 60 * 5);
	}

	@Override
	public String redisGetDownloadErrorInfo(String downloadKey) {
		return redis.get(downloadKey);
	}

	@Override
	public Map<String, SectionNumber> findSectionNumbersByNumber(List<String> numberList) {
		return sectionNumberDao.findSectionNumbersByNumber(numberList);
	}

	@Override
	public Set<String> findByNumbers(List<String> numbers) {
		return sectionNumberDao.findByNumbers(numbers);
	}
}