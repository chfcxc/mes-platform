package cn.emay.eucp.data.dao.system.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.PortableNumberDao;
import cn.emay.util.DateUtil;

@Repository
public class PortableNumberDaoImpl extends PojoDaoImpl<PortableNumber> implements PortableNumberDao {

	@Override
	public List<PortableNumber> findLimit(int currentPage, int pageSize) {
		String sql = "select * from system_portable_number ";
		List<Object> parameters = new ArrayList<Object>();
		return this.findSqlForListForMysql(PortableNumber.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public List<PortableNumber> findByLastUpdateTime(Date date) {
		String sql = "select * from system_portable_number where last_update_time >= ? ";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
		return this.findSqlForListObj(PortableNumber.class, sql, parameters);
	}

	@Override
	public List<PortableNumber> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = "select * from system_portable_number where last_update_time >= ? ";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
		return this.findSqlForListForMysql(PortableNumber.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public Page<PortableNumber> findall(int start, int limit, String mobile) {
		String hql = " select p from PortableNumber p where p.isDelete = :isDelete ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isDelete", false);
		if (!StringUtils.isEmpty(mobile)) {
			hql += " and mobile= :mobile ";
			map.put("mobile", mobile);
		}

		return this.getPageResult(hql, start, limit, map, PortableNumber.class);
	}

	@Override
	public void saveBatchBySql(final List<PortableNumber> entities) {
		String sql = "insert into system_portable_number (mobile,operator_code,is_delete) values (?,?,?)";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PortableNumber entity = entities.get(i);
				ps.setString(1, entity.getMobile());
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
	public void updateBatchBySql(final List<PortableNumber> entities) {
		String sql = "update system_portable_number set is_delete = 0,operator_code = ? where mobile=?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PortableNumber entity = entities.get(i);
				ps.setString(1, entity.getOperatorCode());
				ps.setString(2, entity.getMobile());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});
	}

	@Override
	public void deleteBatchBySql(final List<String> mobiles) {
		String sql = "update system_portable_number set is_delete = ? where mobile= ? ";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setBoolean(1, true);
				ps.setString(2, mobiles.get(i));
			}

			@Override
			public int getBatchSize() {
				return mobiles.size();
			}
		});
	}

	@Override
	public Set<String> findByMobiles(List<String> mobiles) {
		String hql = "select a.mobile from PortableNumber a where a.mobile in (:mobiles)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobiles", mobiles);
		Set<String> resultSet = new HashSet<String>();
		List<String> list = this.getListResult(String.class, hql, params);
		resultSet.addAll(list);
		return resultSet;
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		String sql = "delete from system_portable_number where is_delete = 1 and last_update_time < ?";
		return this.getJdbcTemplate().update(sql, DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public List<PortableNumber> findPageById(long startId, long endId) {
		String sql = "select id, mobile, operator_code, is_delete  from system_portable_number  where  id >= ? and id <= ?   ";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(startId);
		parameters.add(endId);
		return this.findSqlForListObj(PortableNumber.class, sql, parameters);
	}
	
	@Override
	public Map<String, PortableNumber> findPortableNumberByMobiles(List<String> mobiles) {
		Map<String, PortableNumber> map = new HashMap<String, PortableNumber>();
		String hql = "from PortableNumber where mobile in (:mobiles)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobiles", mobiles);
		List<PortableNumber> list = this.getListResult(PortableNumber.class, hql, params);
		if (list != null && list.size() > 0) {
			for (PortableNumber portableNumber : list) {
				map.put(portableNumber.getMobile(), portableNumber);
			}
		}
		return map;
	}
}
