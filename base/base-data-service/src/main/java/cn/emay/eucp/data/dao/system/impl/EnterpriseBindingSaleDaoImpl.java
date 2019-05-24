package cn.emay.eucp.data.dao.system.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseBindingSaleDTO;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.EnterpriseBindingSaleDao;
import cn.emay.eucp.util.RegularUtils;

@Repository
public class EnterpriseBindingSaleDaoImpl extends PojoDaoImpl<EnterpriseBindingSale> implements EnterpriseBindingSaleDao {

	@Override
	public Page<EnterpriseBindingSale> selectRelationship(List<String> systemEnterpriseIdList, List<String> systemUserIdList, Integer start, Integer limit) {
		StringBuffer sb = new StringBuffer("select * from system_enterprise_binding_sale a where 1=1 ");
		if (null != systemEnterpriseIdList && systemEnterpriseIdList.size() != 0) {
			sb.append(" and a.system_enterprise_id in (" + org.apache.commons.lang3.StringUtils.join(systemEnterpriseIdList.toArray(), ",") + ")");
		}
		if (null != systemUserIdList && systemUserIdList.size() != 0) {
			sb.append(" and a.system_user_id in (" + org.apache.commons.lang3.StringUtils.join(systemUserIdList.toArray(), ",") + ")");
		}
		sb.append(" order by create_time desc,id desc ");
		return findSqlForPageForMysql(EnterpriseBindingSale.class, sb.toString(), null, start, limit);
	}

	@Override
	public List<Map<String, Object>> findRelationships(String enterpriseIds) {
		StringBuffer sb = new StringBuffer("select * from system_enterprise_binding_sale a where 1=1 ");
		if (!RegularUtils.isEmpty(enterpriseIds)) {
			if (!RegularUtils.notExistSpecial(enterpriseIds)) {
				enterpriseIds = RegularUtils.specialCodeEscape(enterpriseIds);
			}
			sb.append(" and a.system_enterprise_id in (" + enterpriseIds + ")");
		}
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public boolean deleteRelationships(String enterpriseIds) {
		boolean ret = false;
		String sql = "delete from system_enterprise_binding_sale where id in (" + enterpriseIds + ")";
		int retNum = jdbcTemplate.update(sql);
		if (retNum >= 0) {
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean deleteSystemEnterpriseBindingSaleTop(String enterpriseIds) {
		boolean ret = false;
		String sql = "delete from system_enterprise_binding_sale_top where system_enterprise_binding_sale_id in (" + enterpriseIds + ")";
		int retNum = jdbcTemplate.update(sql);
		if (retNum >= 0) {
			ret = true;
		}
		return ret;
	}

	@Override
	public List<Map<String, Object>> findAllRelationship(String systemEnterpriseId, String systemUserId) {
		StringBuffer sb = new StringBuffer("select * from system_enterprise_binding_sale a where 1=1 ");
		if (!RegularUtils.isEmpty(systemEnterpriseId)) {
			if (!RegularUtils.notExistSpecial(systemEnterpriseId)) {
				systemEnterpriseId = RegularUtils.specialCodeEscape(systemEnterpriseId);
			}
			sb.append(" and a.system_enterprise_id in (" + systemEnterpriseId + ")");
		}
		if (!RegularUtils.isEmpty(systemUserId)) {
			if (!RegularUtils.notExistSpecial(systemUserId)) {
				systemUserId = RegularUtils.specialCodeEscape(systemUserId);
			}
			sb.append(" and a.system_user_id in (" + systemUserId + ")");
		}
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public void saveBatchSystemEnterpriseBindingSaleTop(final List<EnterpriseBindingSale> entities) {
		String sql = "insert into system_enterprise_binding_sale (system_enterprise_id,system_user_id,last_update_man,create_time,create_man) values (?,?,?,NOW(),?)";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				EnterpriseBindingSale EnterpriseBindingSale = entities.get(i);
				ps.setString(1, EnterpriseBindingSale.getSystemEnterpriseId().toString());
				ps.setString(2, EnterpriseBindingSale.getSystemUserId().toString());
				ps.setString(3, EnterpriseBindingSale.getLastUpdateMan());
				ps.setString(4, EnterpriseBindingSale.getCreateMan());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});
	}

	@Override
	public Page<EnterpriseBindingSale> selectCientManage(String userName, String systemEnterpriseId, Integer start, Integer limit) {
		StringBuffer sb = new StringBuffer(
				"SELECT a.*, ifnull(b.top,0) AS top FROM system_enterprise_binding_sale a LEFT JOIN system_enterprise_binding_sale_top b ON a.id = b.system_enterprise_binding_sale_id and b.create_man = '"
						+ userName + "' WHERE 1=1");
		if (!RegularUtils.isEmpty(systemEnterpriseId)) {
			if (!RegularUtils.notExistSpecial(systemEnterpriseId)) {
				systemEnterpriseId = RegularUtils.specialCodeEscape(systemEnterpriseId);
			}
			sb.append(" and a.system_enterprise_id in (" + systemEnterpriseId + ") ");
		}
		sb.append(" ORDER BY b.top DESC,a.create_time DESC ");
		return findSqlForPageForMysql(EnterpriseBindingSale.class, sb.toString(), null, start, limit);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int stickCientManageFindMaxNum(String userName) {
		String sql = "SELECT MAX(a.`top`) AS maxNum FROM system_enterprise_binding_sale_top a where a.create_man='" + userName + "'";
		return jdbcTemplate.queryForInt(sql);
	}

	@Override
	public List<Map<String, Object>> stickCientManageSelect(String userName, String enterpriseBindingSaleId) {
		String sql = "SELECT * FROM system_enterprise_binding_sale_top a WHERE a.system_enterprise_binding_sale_id=" + enterpriseBindingSaleId + " and a.create_man='" + userName + "'";
		return jdbcTemplate.queryForList(sql.toString());
	}

	@Override
	public boolean stickCientManageUpdate(String userName, String enterpriseBindingSaleId, String newMaxNum) {
		boolean ret = false;
		String sql = "UPDATE system_enterprise_binding_sale_top a SET a.top='" + newMaxNum + "' WHERE a.system_enterprise_binding_sale_id=" + enterpriseBindingSaleId + " and a.create_man='"
				+ userName + "'";
		int retNum = jdbcTemplate.update(sql);
		if (retNum >= 0) {
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean stickCientManageInsert(String userName, String enterpriseBindingSaleId, String newMaxNum) {
		boolean ret = false;
		String sql = "INSERT INTO system_enterprise_binding_sale_top(top,system_enterprise_binding_sale_id,last_update_man,create_time,create_man) values (" + newMaxNum + ","
				+ enterpriseBindingSaleId + ",'" + userName + "',now(),'" + userName + "')";
		int retNum = jdbcTemplate.update(sql);
		if (retNum >= 0) {
			ret = true;
		}
		return ret;
	}

	@Override
	public int[] saveBatchEnterpriseBindingSale(List<Object[]> params) {
		String sql = "INSERT INTO system_enterprise_binding_sale(system_enterprise_id,system_user_id,last_update_man,create_time,create_man) values (?,?,?,now(),?)";
		return this.jdbcTemplate.batchUpdate(sql, params);
	}

	@Override
	public List<Map<String, Object>> findByEnterpriseId(Long id) {
		StringBuffer sql = new StringBuffer(" select u.id,u.realname from system_user u,system_enterprise_binding_sale sb where u.id=sb.system_user_id " + "and sb.system_enterprise_id = " + id);
		return this.jdbcTemplate.queryForList(sql.toString());
	}

	@Override
	public Map<String, Object> findByEnterpriseBindingSaleId(Long id) {
		String sql = "select * from system_enterprise_binding_sale a where a.id=" + id;
		return jdbcTemplate.queryForMap(sql);
	}

	@Override
	public EnterpriseBindingSale findByfieldName(String fieldName, Object value) {
		return this.findByProperty(fieldName, value);
	}

	@Override
	public List<Long> getEnterpriseIdsBySaleId(String systemUserId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select system_enterprise_id  from system_enterprise_binding_sale a where 1=1 ");
		if (!RegularUtils.isEmpty(systemUserId)) {
			if (!RegularUtils.notExistSpecial(systemUserId)) {
				systemUserId = RegularUtils.specialCodeEscape(systemUserId);
			}
			sb.append(" and a.system_user_id in (" + systemUserId + ")");
		}
		return findSqlForSingleColumn(sb.toString(), parameters);
	}

	@Override
	public List<Long> getServiceCodeIdsBySaleId(String systemUserId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT a.system_enterprise_id FROM system_enterprise_binding_sale a WHERE 1=1 ");
		if (!RegularUtils.isEmpty(systemUserId)) {
			if (!RegularUtils.notExistSpecial(systemUserId)) {
				systemUserId = RegularUtils.specialCodeEscape(systemUserId);
			}
			sb.append(" and a.system_user_id in (" + systemUserId + ")");
		}
		return findSqlForSingleColumn(sb.toString(), parameters);
	}

	@Override
	public void deleteByfieldName(String fieldName, Object value) {
		this.deleteByProperty(fieldName, value);
	}

	@Override
	public List<Long> getIdsBySaleId(String systemUserId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT id  FROM system_enterprise_binding_sale where 1=1");
		if (!RegularUtils.isEmpty(systemUserId)) {
			if (!RegularUtils.notExistSpecial(systemUserId)) {
				systemUserId = RegularUtils.specialCodeEscape(systemUserId);
			}
			sb.append(" and system_user_id in (" + systemUserId + ")");
		}
		return findSqlForSingleColumn(sb.toString(), parameters);
	}

	@Override
	public List<Long> getPlatformIdsBySaleId(String systemUserId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT a.system_enterprise_id FROM system_enterprise_binding_sale a WHERE 1=1 ");
		if (!RegularUtils.isEmpty(systemUserId)) {
			if (!RegularUtils.notExistSpecial(systemUserId)) {
				systemUserId = RegularUtils.specialCodeEscape(systemUserId);
			}
			sb.append(" and a.system_user_id in (" + systemUserId + ")");
		}
		return findSqlForSingleColumn(sb.toString(), parameters);
	}
	
	@Override
	public List<EnterpriseBindingSaleDTO> findByEnterIds(List<Long> enterpriseId){
		String sql = "select system_enterprise_id ,system_user_id from system_enterprise_binding_sale where system_enterprise_id in ("+ StringUtils.join(enterpriseId.toArray(), ",")+ ")";
		return this.findSqlForListObj(EnterpriseBindingSaleDTO.class, sql, null);
	}
	
	@Override
	public void saveBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList) {
		List<Object[]> enterSaleList = new ArrayList<Object[]>();
		for (EnterpriseBindingSaleDTO dto : dtoList) {
			enterSaleList.add(new Object[] { dto.getSystemUserId(), dto.getSystemEnterpriseId()});
		}
		String sql = "insert into system_enterprise_binding_sale (system_user_id,system_enterprise_id,create_time) values (?,?,now())";
		this.getJdbcTemplate().batchUpdate(sql, enterSaleList);
	}
	
	@Override
	public void updateBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList) {
		List<Object[]> enterSaleList = new ArrayList<Object[]>();
		for (EnterpriseBindingSaleDTO dto : dtoList) {
			enterSaleList.add(new Object[] { dto.getSystemUserId(), dto.getSystemEnterpriseId()});
		}
		String sql = "update system_enterprise_binding_sale set system_user_id=? where system_enterprise_id=?";
		this.getJdbcTemplate().batchUpdate(sql, enterSaleList);
	}
	
	@Override
	public List<Map<String, Object>> selectAllRelationshipForflow() {
		StringBuffer sb = new StringBuffer("select se.system_enterprise_id as eid,su.realname as sname from system_enterprise_binding_sale se ,system_user su where 1=1 and se.system_user_id=su.id");
		return jdbcTemplate.queryForList(sb.toString());
	}
}
