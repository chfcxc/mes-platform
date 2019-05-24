package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface BaseSectionNumberDao extends BaseSuperDao<BaseSectionNumber> {

	/**
	 * 分页查询
	 * 
	 * @param number
	 * @param operatorCode
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<BaseSectionNumber> findBaseSectionNumber(String number, String operatorCode, int start, int limit);

	/**
	 * 根据ID删除
	 */
	void deleteById(Long id);

	/**
	 * 
	 * 根据number进行查找
	 * 
	 * @param number
	 * @return
	 */
	boolean findByNumber(String number, Long id);

	int deleteByLastUpdateTime(Date date);
	
	public void saveBatchBySql(List<BaseSectionNumber> entities);
	
	List<BaseSectionNumber> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize);
}
