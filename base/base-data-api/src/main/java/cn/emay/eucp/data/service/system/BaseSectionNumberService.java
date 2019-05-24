package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;

/**
 * 基础号段管理服务
 * 
 * @author Frank
 * 
 */
public interface BaseSectionNumberService {
	/**
	 * 查询全部
	 * 
	 * 
	 */
	Page<BaseSectionNumber> findBaseSectionNumber(String number, String operatorCode, int start, int limit);

	/**
	 * 增加基础号段
	 * 
	 * 
	 */
	Result addBaseSectionNumber(BaseSectionNumber baseSectionNumber);

	/**
	 * 
	 * 根据id进行查询
	 * 
	 * @param id
	 * @return
	 */
	BaseSectionNumber findById(Long id);

	/**
	 * 
	 * 修改
	 * 
	 * @param baseSectionNumber
	 */
	Result updateBaseSectionNumber(BaseSectionNumber baseSectionNumber);

	/**
	 * 
	 * 删除
	 * 
	 * @param id
	 */
	Result deleteBaseSectionNumber(Long id);

	/**
	 * 
	 * 根据number进行查找
	 * 
	 * @param number
	 * @return
	 */
	boolean findByNumber(String number, Long id);

	/**
	 * 查询全部
	 */
	List<BaseSectionNumber> findAll();

	/**
	 * @Title: deleteByLastUpdateTime
	 * @Description: 按照最后更新时间删除
	 * @param @param date
	 * @return void
	 */
	int deleteByLastUpdateTime(Date date);
	
	/**
	 *  批量插入  
	  * @throws TODO
	 */
	void addBatch(List<BaseSectionNumber> entities);
	
	List<BaseSectionNumber> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize);
}
