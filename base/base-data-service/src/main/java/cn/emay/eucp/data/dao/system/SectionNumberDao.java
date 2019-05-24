package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.SectionNumber Dao super
 * 
 * @author frank
 */
public interface SectionNumberDao extends BaseSuperDao<SectionNumber> {

	Page<SectionNumber> findByNumberAndoperatorCode(String number, String operatorCode, String provinceCode, int start, int limit);

	void deleteSectionNumber(Long id);

	boolean findNumber(String number, Long id);

	public void saveBatchBySql(final List<SectionNumber> sectionNumber);

	List<SectionNumber> findByLastUpdateTime(Date date);

	public List<SectionNumber> findLimit(int start, int limit);

	int deleteByLastUpdateTime(Date date);

	void deleteFindEntity(SectionNumber sectionNumber);

	List<SectionNumber> findSectionNumbersById(int startId, int endId);

	Map<String, SectionNumber> findSectionNumbersByNumber(List<String> numberList);
	
	Set<String> findByNumbers(List<String> numbers);
}