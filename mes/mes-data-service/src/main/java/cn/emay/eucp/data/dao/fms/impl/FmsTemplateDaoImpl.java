package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsTemplateDao;

@Repository
public class FmsTemplateDaoImpl extends PojoDaoImpl<FmsTemplate> implements FmsTemplateDao {

	@Override
	public List<FmsTemplate> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_template where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time >= ?";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsTemplate.class, sql, parameters, currentPage, pageSize);
	}

}
