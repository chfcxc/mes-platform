package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.license.LicenseDTO;
import cn.emay.eucp.common.dto.license.LicenseDownLoadDTO;
import cn.emay.eucp.common.moudle.db.system.License;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.LicenseDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * 
 * @author lijunjian
 */
@Repository
public class LicenseDaoImpl extends PojoDaoImpl<License> implements LicenseDao {

	@Override
	public Page<LicenseDTO> findByPage(String operatorName,String mac,Integer termOfValidity,Date startDate,Date endDate, int start, int limit) {
		List<Object> params=new ArrayList<Object>();
		StringBuffer sql=new StringBuffer(" select sl.id,se.name_cn,se.linkman,se.mobile,sl.product,sl.version,sl.mac,sl.operator_name,sl.create_time,(case when DATEDIFF(sl.end_time,now())>=0 then DATEDIFF(sl.end_time,now())+1 else 0 end) as termOfValidity"
				+ " from system_license sl,system_enterprise se where sl.system_enterprise_id=se.id");
		if(!StringUtils.isEmpty(operatorName)){
			sql.append(" and sl.operator_name like ?");
			params.add("%"+RegularUtils.specialCodeEscape(operatorName)+"%");
		}
		if(!StringUtils.isEmpty(mac)){
			sql.append(" and sl.mac = ?");
			params.add(mac);
		}
		if(termOfValidity.intValue()!=-1){
			if(termOfValidity.intValue()==0){
				sql.append(" and sl.end_time <now()");
			}else{
				sql.append(" and DATEDIFF(sl.end_time,now())+1 = ?");
				params.add(termOfValidity);
			}
		}
		if(null!=startDate){
			sql.append(" and sl.create_time >=?");
			params.add(startDate);
		}
		if(null!=endDate){
			sql.append(" and sl.create_time <=?");
			params.add(endDate);
		}
		sql.append(" order by id desc");
		return this.findSqlForPageForMysql(LicenseDTO.class,sql.toString(), params, start, limit);
	}

	@Override
	public LicenseDownLoadDTO downloadFindById(Long id) {
		List<Object> parameters=new ArrayList<Object>();
		String sql="select se.name_cn,se.linkman,se.mobile,sl.product,sl.version,sl.begin_time,sl.end_time,sl.mac,sl.service_type,sl.create_time from system_license sl,system_enterprise se where sl.system_enterprise_id=se.id and sl.id=?";
		parameters.add(id);
		return this.findSqlForObj(LicenseDownLoadDTO.class, sql, parameters);
	}

	@Override
	public LicenseDTO findInfoById(Long id) {
		List<Object> parameters=new ArrayList<Object>();
		String sql="select sl.id,sl.system_enterprise_id,se.name_cn,se.linkman,se.mobile,sl.product,sl.version,sl.begin_time,sl.end_time,sl.mac,sl.service_type,sl.create_time from system_license sl,system_enterprise se where sl.system_enterprise_id=se.id and sl.id=?";
		parameters.add(id);
		return this.findSqlForObj(LicenseDTO.class, sql, parameters);
	}

	
}
