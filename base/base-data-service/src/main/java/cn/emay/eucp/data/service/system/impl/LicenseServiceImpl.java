package cn.emay.eucp.data.service.system.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.license.LicenseDTO;
import cn.emay.eucp.common.dto.license.LicenseDownLoadDTO;
import cn.emay.eucp.common.moudle.db.system.License;
import cn.emay.eucp.data.dao.system.LicenseDao;
import cn.emay.eucp.data.service.system.LicenseService;


/**
 * 
 * @author lijunjian
 */
@Service("licenseService")
public class LicenseServiceImpl implements LicenseService {
	
	@Resource
	LicenseDao licenseDao;

	@Override
	public Page<LicenseDTO> findByPage(String operatorName,String mac,Integer termOfValidity, Date startDate,Date endDate, int start, int limit) {
		return licenseDao.findByPage(operatorName,mac,termOfValidity,startDate, endDate, start, limit);
	}

	@Override
	public void addLicense(License license) {
		licenseDao.save(license);
	}

	@Override
	public License findById(Long id) {
		return licenseDao.findById(id);
	}

	@Override
	public void updateLicense(License license) {
		licenseDao.update(license);
	}

	@Override
	public LicenseDownLoadDTO downloadFindById(Long id) {
		return licenseDao.downloadFindById(id);
	}

	@Override
	public LicenseDTO findInfoById(Long id) {
		return licenseDao.findInfoById(id);
	}

	
}
