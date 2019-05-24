package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Export;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface ExportDao extends BaseSuperDao<Export> {

	/**
	 * 查询创建时间最早的导出信息
	 * 
	 * @return
	 */
	Export findSmsExport(String systemFor, String businessType);

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
	 * 查询某个时间之前的导出信息
	 * 
	 * @param date
	 * @return
	 */
	List<Export> findSmsExportBeforeDate(Date date, String systemFor, String businessType);

	/**
	 * 根据条件批量更新
	 * 
	 * @param date
	 * @param state
	 * @return
	 */
	Integer updateBatchSmsExport(Date date, int state, String systemFor, String businessType);

}