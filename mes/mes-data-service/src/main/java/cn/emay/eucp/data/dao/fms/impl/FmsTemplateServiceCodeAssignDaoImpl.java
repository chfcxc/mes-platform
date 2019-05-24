package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsTemplateServiceCodeAssignDao;

@Repository
public class FmsTemplateServiceCodeAssignDaoImpl extends PojoDaoImpl<FmsTemplateServiceCodeAssign> implements FmsTemplateServiceCodeAssignDao {

	@Override
	public Page<FmsTemplateDto> findTempLetPage(String title, String content, List<String> appId, Long businessTypeId, Integer saveType, Long contentTypeId, Integer messageType, Integer submitType,
			Integer auditState, Date startTime, Date endTime, int start, int limit, Integer templateType) {
		StringBuffer sqlBuffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sqlBuffer.append("select a.*,b.content,b.template_type,b.submit_time from fms_template_service_code_assign a,fms_template b where a.template_id = b.id ");
		if (null != title) {
			sqlBuffer.append(" and a.template_name like ?");
			params.add("%" + title + "%");
		}
		if (templateType != -1) {
			sqlBuffer.append(" and b.template_type = ?");
			params.add(templateType);
		}
		if (null != content) {
			sqlBuffer.append(" and b.content = ?");
			params.add(content);
		}
		if (null != appId && appId.size() > 0) {
			int size = appId.size();
			sqlBuffer.append(" and a.app_id in( ");
			for (int i = 0; i < size; i++) {
				if (i == size - 1) {
					sqlBuffer.append("?");
				} else {
					sqlBuffer.append("?,");
				}
				params.add(appId.get(i));
			}
			sqlBuffer.append(")");
		}
		if (null != businessTypeId && businessTypeId != 0L) {
			sqlBuffer.append(" and a.business_type_id = ?");
			params.add(businessTypeId);
		}
		if (null != saveType) {
			sqlBuffer.append(" and a.save_type = ?");
			params.add(saveType);
		}
		if (null != contentTypeId && contentTypeId != 0L) {
			sqlBuffer.append(" and a.content_type_id = ?");
			params.add(contentTypeId);
		}
		if (null != messageType) {
			sqlBuffer.append(" and b.template_type = ?");
			params.add(messageType);
		}
		if (null != submitType) {
			sqlBuffer.append(" and a.send_type = ?");
			params.add(submitType);
		}
		if (null != auditState) {
			sqlBuffer.append(" and a.audit_state = ?");
			params.add(auditState);
		}
		if (null != startTime) {
			sqlBuffer.append(" and a.create_time >= ?");
			params.add(startTime);
		}
		if (null != endTime) {
			sqlBuffer.append(" and a.create_time <= ?");
			params.add(endTime);
		}
		sqlBuffer.append(" order by b.submit_time desc");
		return this.findSqlForPageForMysql(FmsTemplateDto.class, sqlBuffer.toString(), params, start, limit);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByAudiState(Integer auditState, int currentPage, int pageSize) {
		if (auditState == null) {
			return null;
		}
		String sql = "select a.* from fms_template_service_code_assign a where a.audit_state =" + auditState.intValue();
		return this.findSqlForListForMysql(FmsTemplateServiceCodeAssign.class, sql, null, currentPage, pageSize);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_template_service_code_assign where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and create_time >= ?";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsTemplateServiceCodeAssign.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findNeedRepeatReoportList() {
		String sql = "SELECT DISTINCT b.* FROM fms_service_code_channel a ,fms_template_service_code_assign b WHERE a.app_id=b.app_id and b.audit_state=1 and a.last_update_time > b.last_update_time";
		return this.findSqlForListObj(FmsTemplateServiceCodeAssign.class, sql, null);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByAppIdAndTemplate(String appId, String templateId) {
		String sql = " select * from fms_template_service_code_assign where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != appId) {
			sql += " and app_id = ?";
			parameters.add(appId);
		}
		if (null != templateId) {
			sql += " and template_id = ?";
			parameters.add(templateId);
		}
		return this.findSqlForListObj(FmsTemplateServiceCodeAssign.class, sql, parameters);
	}
}
