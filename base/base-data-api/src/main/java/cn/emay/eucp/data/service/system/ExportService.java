package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Export;

public interface ExportService {

	/**
	 * 保存导出信息
	 * 
	 * @param userId
	 * @param module
	 * @param queryCriteria
	 */
	void saveSmsExport(Long userId, String module, String queryCriteria, String exportFileName, String systemFor, String businessType);

	/**
	 * 查询创建时间最早的导出信息
	 * 
	 * @return
	 */
	Export findSmsExport(String systemFor, String businessType);

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	void update(Export entity);

	/**
	 * 分页查询我的导出
	 * 
	 * @param start
	 * @param limit
	 * @param userId
	 * @return
	 */
	Page<Export> findPage(int start, int limit, Long userId, String systemFor, String businessType);

	/**
	 * 根据id查询导出信息
	 * 
	 * @param id
	 * @return
	 */
	Export findById(Long id);

	/**
	 * 查询某个时间之前的导出信息
	 * 
	 * @param date
	 * @return
	 */
	List<Export> findSmsExportBeforeDate(Date date, String systemFor, String businessType);

	/**
	 * 批量删除
	 * 
	 * @param entities
	 */
	void deleteBatch(List<Export> entities);

	/**
	 * 根据条件批量更新
	 * 
	 * @param date
	 * @param state
	 */
	Integer updateBatchSmsExport(Date date, int state, String systemFor, String businessType);

}
