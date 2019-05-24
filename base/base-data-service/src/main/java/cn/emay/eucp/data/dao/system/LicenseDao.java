package cn.emay.eucp.data.dao.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.license.LicenseDTO;
import cn.emay.eucp.common.dto.license.LicenseDownLoadDTO;
import cn.emay.eucp.common.moudle.db.system.License;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * 
 * @author lijunjian
 */
public interface LicenseDao extends BaseSuperDao<License> {

	/**
	 * 分页查询
	 */
	Page<LicenseDTO> findByPage(String operatorName,String mac,Integer termOfValidity,Date startDate,Date endDate,int start,int limit);
	
	/**
	 * 下载
	 * @param id
	 * @return
	 */
	LicenseDownLoadDTO downloadFindById(Long id);
	
	LicenseDTO findInfoById(Long id);
}
