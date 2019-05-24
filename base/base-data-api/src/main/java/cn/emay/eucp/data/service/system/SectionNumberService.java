package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;

/**
 * cn.emay.eucp.common.moudle.db.system.SectionNumber Service Super
 * 
 * @author frank
 */
public interface SectionNumberService {

	/**
	 * 
	 * 基础号段查询
	 * 
	 * @param number
	 * @param operatorCode
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<SectionNumber> findByNumberAndoperatorCode(String number, String operatorCode, String provinceCode, int start, int limit);

	/**
	 * 增加精确号段
	 * 
	 * @param sectionNumber
	 */
	Result addSectionNumber(SectionNumber sectionNumber);

	/**
	 * 修改精确号段
	 * 
	 * @param sectionNumber
	 */
	Result updateSectionNumber(SectionNumber sectionNumber);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	SectionNumber findById(Long id);

	/**
	 * 删除
	 * 
	 * 
	 */
	Result deleteSectionNumber(Long id);

	/**
	 * 
	 * 根据号段查找
	 */
	boolean findNumber(String number, Long id);

	/**
	 * @Title: findAll
	 * @Description: 查询全部
	 * @param @return
	 * @return List<SectionNumber>
	 */
	List<SectionNumber> findAll();

	/**
	 * @Title: findLimit
	 * @Description: 号段分页查询
	 * @param @param start
	 * @param @param limit
	 * @param @return
	 * @return List<SectionNumber>
	 */
	List<SectionNumber> findLimit(int currentPage, int pageSize);

	/**
	 * @Title: findByLastUpdateTime
	 * @Description: 按最后更新时间查询用于增量更新
	 * @param @param date
	 * @param @return
	 * @return List<SectionNumber>
	 */
	List<SectionNumber> findByLastUpdateTime(Date date);

	/**
	 * @Title: deleteByLastUpdateTime
	 * @Description: 按照最后更新时间删除
	 * @param @param date
	 * @return void
	 */
	int deleteByLastUpdateTime(Date date);

	void saveBatchBySql(List<SectionNumber> list);

	Result verify(String number, String provinceCode, String provinceName, String city, String operatorCode);

	List<SectionNumber> findSectionNumbersById(int startId, int endId);
	
	void redisSaveDownloadError(String downloadKey, List<String[]> errors);

	String redisGetDownloadErrorInfo(String downloadKey);

	// 根据号段查询对象
	Map<String, SectionNumber> findSectionNumbersByNumber(List<String> numberList);
	
	// 根据手机号查询
	Set<String> findByNumbers(List<String> numbers);

}