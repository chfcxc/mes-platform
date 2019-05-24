package cn.emay.eucp.data.dao.system.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.BaseSectionNumberDao;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.util.DateUtil;

@Repository
public class BaseSectionNumberDaoImpl extends PojoDaoImpl<BaseSectionNumber> implements BaseSectionNumberDao {

	@Override
	public Page<BaseSectionNumber> findBaseSectionNumber(String number, String operatorCode, int start, int limit) {
		String hql = "from BaseSectionNumber where 1=1 ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (number != null) {
			hql += " and number like :number ";
			params.put("number", "%" + RegularUtils.specialCodeEscape(number) + "%");
		}
		if (operatorCode != null && operatorCode.trim().length() != 0) {
			hql += " and operatorCode = :operatorCode ";
			params.put("operatorCode", RegularUtils.specialCodeEscape(operatorCode));
		}
		hql += " and isDelete=0 order by id desc";
		return this.getPageResult(hql, start, limit, params, BaseSectionNumber.class);
	}

	@Override
	public void deleteById(Long id) {
		String hql = "update BaseSectionNumber set isDelete=1 where id=:ids";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("ids", id);
		this.execByHql(hql, params);
	}

	@Override
	public boolean findByNumber(String number, Long id) {
		String hql = "from BaseSectionNumber  where number=:number and isDelete=0  ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("number", number);
		if (id != null && id != 0l) {
			hql += " and id!=:id";
			params.put("id", id);
		}
		List<BaseSectionNumber> list = this.getListResult(BaseSectionNumber.class, hql, params);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		String sql = "delete from system_base_section_number where is_delete=1 and last_update_time<?";
		return this.getJdbcTemplate().update(sql, DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void saveBatchBySql(final List<BaseSectionNumber> entities) {
		String sql = "insert into system_base_section_number (number,operator_code,is_delete) values (?,?,?)";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				BaseSectionNumber entity = entities.get(i);
				ps.setString(1, entity.getNumber());
				ps.setString(2, entity.getOperatorCode());
				ps.setBoolean(3, entity.getIsDelete());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});
	}

	@Override
	public List<BaseSectionNumber> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select * from system_base_section_number where is_delete=0";
		if(null != lastUpdateTime){
			sql += " and last_update_time >= ?";
			parameters.add(DateUtil.toString(lastUpdateTime, "yyyy-MM-dd HH:mm:ss"));
		}
		return this.findSqlForListForMysql(BaseSectionNumber.class, sql, parameters, currentPage, pageSize);
	}

}
