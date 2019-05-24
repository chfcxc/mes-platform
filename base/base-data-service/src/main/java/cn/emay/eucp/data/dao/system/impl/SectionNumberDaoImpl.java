package cn.emay.eucp.data.dao.system.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.Province;
import cn.emay.eucp.common.moudle.db.system.SectionNumber;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.SectionNumberDao;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.util.DateUtil;

/**
 * cn.emay.eucp.common.moudle.db.system.SectionNumber Dao implement
 * 
 * @author frank
 */
@Repository
public class SectionNumberDaoImpl extends PojoDaoImpl<SectionNumber> implements SectionNumberDao {

	@Override
	public Page<SectionNumber> findByNumberAndoperatorCode(String number, String operatorCode, String provinceCode, int start, int limit) {
		String hql = "from SectionNumber where 1=1 ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (number != null) {
			hql += " and number like :number ";
			params.put("number", "%" + RegularUtils.specialCodeEscape(number) + "%");
		}
		if (operatorCode != null && operatorCode.trim().length() != 0) {
			hql += " and operatorCode = :operatorCode ";
			params.put("operatorCode", RegularUtils.specialCodeEscape(operatorCode));
		}
		if (provinceCode != null && provinceCode.trim().length() != 0) {
			hql += " and provinceCode = :provinceCode ";
			params.put("provinceCode", RegularUtils.specialCodeEscape(provinceCode));
		}
		hql += " and isDelete=0 order by id desc";
		return this.getPageResult(hql, start, limit, params, SectionNumber.class);
	}

	@Override
	public void deleteSectionNumber(Long id) {
		String hql = "update SectionNumber set isDelete=1 where id=:ids";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("ids", id);
		this.execByHql(hql, params);
	}

	@Override
	public boolean findNumber(String number, Long id) {
		String hql = "from SectionNumber  where number=:number and isDelete=0  ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("number", number);
		if (id != null && id != 0l) {
			hql += " and id!=:id";
			params.put("id", id);
		}
		List<SectionNumber> list = this.getListResult(SectionNumber.class, hql, params);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void saveBatchBySql(final List<SectionNumber> sectionNumber) {
		String sql = "INSERT INTO system_section_number (number,province_code,province_name,city,operator_code,is_delete) " + "VALUES ( ?, ?, ?, ?, ?, ?)";

		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SectionNumber sectionnumbers = sectionNumber.get(i);
				ps.setString(1, sectionnumbers.getNumber());
				ps.setString(2, Province.findCodeByName(sectionnumbers.getProvinceName()));
				ps.setString(3, sectionnumbers.getProvinceName());
				ps.setString(4, sectionnumbers.getCity());
				ps.setString(5, sectionnumbers.getOperatorCode());
				ps.setBoolean(6, sectionnumbers.getIsDelete());
			}

			@Override
			public int getBatchSize() {
				return sectionNumber.size();
			}

		});
	}

	@Override
	public List<SectionNumber> findByLastUpdateTime(Date date) {
		String sql = "select * from system_section_number where last_update_time >= ? ";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
		return this.findSqlForListObj(SectionNumber.class, sql, parameters);
	}

	@Override
	public List<SectionNumber> findLimit(int start, int limit) {
		String sql = "select * from system_section_number ";
		List<Object> parameters = new ArrayList<Object>();
		return this.findSqlForListForMysql(SectionNumber.class, sql, parameters, start, limit);
	}

	@Override
	public int deleteByLastUpdateTime(Date date) {
		String sql = "delete from system_section_number where is_delete=1 and last_update_time<?";
		return this.getJdbcTemplate().update(sql, DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void deleteFindEntity(SectionNumber sectionNumber) {
		String hql = " delete from SectionNumber where  number = :number and provinceCode = :provinceCode and  provinceName = :provinceName and"
				+ " city = :city  and operatorCode = :operatorCode and isDelete = :isDelete";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", sectionNumber.getNumber());
		params.put("provinceCode", sectionNumber.getProvinceCode());
		params.put("provinceName", sectionNumber.getProvinceName());
		params.put("city", sectionNumber.getCity());
		params.put("operatorCode", sectionNumber.getOperatorCode());
		params.put("isDelete", true);
		this.execByHql(hql, params);
	}

	@Override
	public List<SectionNumber> findSectionNumbersById(int startId, int endId) {
		String sql = "select id, number, province_code, province_name, city, operator_code, is_delete  from system_section_number  where  id >= ? and id <= ?   ";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(startId);
		parameters.add(endId);
		return this.findSqlForListObj(SectionNumber.class, sql, parameters);
	}

	@Override
	public Map<String, SectionNumber> findSectionNumbersByNumber(List<String> numberList) {
		Map<String, SectionNumber> map = new HashMap<String, SectionNumber>();
		String hql = "from SectionNumber where number in (:number)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", numberList);
		List<SectionNumber> list = this.getListResult(SectionNumber.class, hql, params);
		if (list != null && list.size() > 0) {
			for (SectionNumber sectionNumber : list) {
				map.put(sectionNumber.getNumber(), sectionNumber);
			}
		}
		return map;
	}

	@Override
	public Set<String> findByNumbers(List<String> numbers) {
		String hql = "select s.number from SectionNumber s where s.isDelete =:isDelete and  s.number in (:numbers)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isDelete", false);
		params.put("numbers", numbers);
		Set<String> resultSet = new HashSet<String>();
		List<String> list = this.getListResult(String.class, hql, params);
		resultSet.addAll(list);
		return resultSet;
	}
}