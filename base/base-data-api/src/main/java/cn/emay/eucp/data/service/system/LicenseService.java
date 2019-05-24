package cn.emay.eucp.data.service.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.license.LicenseDTO;
import cn.emay.eucp.common.dto.license.LicenseDownLoadDTO;
import cn.emay.eucp.common.moudle.db.system.License;


/**
 * 
 * @author lijunjian
 */
public interface LicenseService {

	/**
	 * 分页查询
	 */
	Page<LicenseDTO> findByPage(String operatorName,String mac,Integer termOfValidity, Date startDate,Date endDate, int start, int limit);
	
	/**
	 * 新增
	 * @param license
	 */
	void addLicense(License license);
	
	/**
	 * 修改
	 * @param license
	 */
	void updateLicense(License license);
	
	License findById(Long id);
	
	/**
	 * 下载
	 * @param id
	 * @return
	 */
	LicenseDownLoadDTO downloadFindById(Long id);
	
	LicenseDTO findInfoById(Long id);
}
